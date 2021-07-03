package theapp.graphics;

import java.awt.event.KeyEvent;
import java.util.Random;
import theapp.core.App;
import theapp.input.InputHandler;

public class DisplayBuffer extends RenderedObject{

    // just make 2 throw away test object
    private RenderedObject testObject;
    private RenderedObject testOImageFromCode;

    private InputHandler input;
    private App parent;
    // a counter to help with color changes over time in a controlled way
    private long ticks;

    // Constructor
    public DisplayBuffer(int width, int height, App parent){
        super(width, height);

        input = new InputHandler();
        this.parent = parent;
        parent.addKeyListener(input);
        parent.addFocusListener(input);
        parent.addMouseListener(input);
        parent.addMouseMotionListener(input);
        /*testObject = new RenderedObject(320, 180); // fill left 400 pixels of the screen with random colors

        testObject.load(()->{
            // Fills testObject with random colors
            Random random = new Random();
            int rand = random.nextInt() * random.nextInt(5) / 4;
            for (int j = 0; j < testObject.height; j++) {
                for (int i = 0; i < testObject.width; i++){
                    if (i % 2 == 0) // repeat value for 16 by 16 pixesl (just for the effect of enlarging a pixel )
                        rand = random.nextInt() * random.nextInt(5) / 4;
                    if (j % 3 == 0)
                        testObject.displayMemory[i + (testObject.width * (j))] = rand;
                    else {
                        testObject.displayMemory[i + (testObject.width * (j))] = testObject.displayMemory[i + (testObject.width * (j - 1))];
                        rand = random.nextInt();
                    }
                }
            }
        });*/

        testOImageFromCode = new RenderedObject(App.width, App.height);
        testOImageFromCode.load(()->{
            RenderedObject object = testOImageFromCode;
            double floorDistance = 24;
            double ceilingDistance = 20;
            double forward = object.controller.z ;
            double rightward = object.controller.x;
            double rotation = object.controller.rotation;
            double cos = Math.cos(rotation);
            double sine = Math.sin(rotation);

            for (int y = 0; y < object.height; y++ ){
                                           /*adjacent*/       /*opposite*/
                double ceiling = ( y - object.height / 2.0)  / object.height;
                // ceiling ranges from [-1/2 to 1/2) not including 1/2
                // asuming this is the cotangent

                double z; // positive, growing to infinity
                if (ceiling > 0)
                    z = floorDistance /  ceiling; // invert cotan = tan
                else
                    z = ceilingDistance / -ceiling;

                for (int x = 0; x < object.width; x++){
                                            /*adjacent*/       /*hypotenuse*/
                    double depth = (x - object.width / 2.0) / object.height;
                    // depth ranges from [-0.888888 to 0.8875]
                    depth = depth*z;

                    // clip pixels towards the center that are too far
                    //if (z > 400) {
                        //testOImageFromCode.displayMemory[x + object.width * y] = 0;
                      //  continue;
                    //}

                    int xx = (int) ((depth * cos + z * sine)  + rightward) ;
                    int yy = (int) ((z * cos - depth * sine) + forward) ;

                    int pixel = ((xx * 16) & 0xB << 8 | (xx * 16) << 16 ) | (yy *16 << 16);

                    // Fade using gradient
                    final double fadeDist = 5000.0;
                    int colour = pixel;
                    int brightness = (int)(fadeDist/ z);

                    if (brightness < 0)
                        brightness = 0;
                    if (brightness > 255)
                        brightness = 255;
                    int r = (colour >> 16) & 0xff;
                    r = r * brightness / 255;
                    int g = (colour >> 8) & 0xff;
                    g = g * brightness / 255;
                    int b = colour & 0xff;
                    b = b * brightness / 255;
                    testOImageFromCode.displayMemory[x + object.width * y] = (r <<16) | (g << 8) | b;
                    //testOImageFromCode.displayMemory[x+ object.width*(y)] = pixel;

                }
            }
        });
    }

    // Renders everything to the screen
    public void update(){
        tick();
        // Clear buffer
        for (int i = 0, len = App.width * App.height; i < len; i++)
            displayMemory[i] = 0;

        // Redraw
        // Playing with background like image drawn from code
        testOImageFromCode.reload(input.keyPresses);
        draw(testOImageFromCode, 0, 0);

        /*
        // Playing around with sin and cos of a circumference to create an animatioon
        if (ticks % 200 == 0)
            testObject.reload(input.keyPresses);

        for (int i = 0; i < 2000; i+=70) {
            int xAnimOffset = (int) (Math.sin(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);
            int yAnimOffset = (int) (Math.cos(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);

            draw(testObject, (App.width - testObject.width) / 2 + xAnimOffset, (App.height - testObject.height) / 2 - yAnimOffset);
        }
*/
    }
    private void tick(){
        testOImageFromCode.reload(input.keyPresses);
        //testObject.reload();
        ticks++;

    }
}
