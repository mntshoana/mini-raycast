package theapp.graphics;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import theapp.core.App;
import theapp.input.InputHandler;

public class DisplayBuffer extends RenderedObject{

    // just make 2 throw away test object
    private RenderedObject testObject;
    private RenderedObject roofFloor;
    private RenderedObject walls;

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

        roofFloor = new RenderedObject(App.width, App.height);
        roofFloor.load(()->{
            RenderedObject object = roofFloor;
            double floorDistance = 15;
            double ceilingDistance = 20;
            double forward = object.controller.z ;
            double rightward = object.controller.x;
            double upward = object.controller.y;
            double rotation = object.controller.rotation;
            double cos = Math.cos(rotation);
            double sine = Math.sin(rotation);

            for (int y = 0; y < object.height; y++ ){
                                           /*adjacent*/       /*opposite*/
                double ceiling = ( y - object.height / 2.0)  / object.height;
                // ceiling ranges from [-1/2 to 1/2) not including 1/2
                // asuming this is the cotangent

                double z; // positive, growing to infinity
                double walkingBob = Math.sin(ticks / Math.PI);
                if (!object.controller.moved)
                    walkingBob = 0.0;
                else if (object.controller.crouched && object.controller.moved)
                    walkingBob = Math.sin(ticks / Math.PI /2) *  0.3;
                else if (object.controller.moved && object.controller.sprint)
                    walkingBob *= Math.sin(ticks / Math.PI * 1.5) *  0.7;
                else if (object.controller.moved)
                    walkingBob *= 0.5;
                if (ceiling > 0)
                    z = (floorDistance + upward +walkingBob) /  ceiling; // invert cotan = tan
                else
                    z = (ceilingDistance - upward -walkingBob) / -ceiling;

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

                    int pixel;
                    if (y <= object.height/2)
                    //pixel = ((xx * 16) & 0xB << 8 | (xx * 16) << 16 ) | (yy *16 << 16);
                    // Apply texture instead
                        pixel = Texture.ceiling.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];
                    else
                    pixel = Texture.floor.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];

                    object.displayMemory[x + object.width * y] = fade(pixel, z);
                    //testOImageFromCode.displayMemory[x+ object.width*(y)] = pixel;
                }
            }
        });

        walls = new RenderedObject(App.width, App.height);
        walls.load(()->{
            Arrays.fill(walls.displayMemory, 0);
            final double cos = Math.cos(walls.controller.rotation);
            final double sin = Math.sin(walls.controller.rotation);

            double xLeft = 0;
            double xRight = 10;
            double zDistance = 2;
            double yHeight = 0;

            double newXLeft = (xLeft - walls.controller.x) * 2;
            double newZDistance4L = (zDistance - walls.controller.z) *2;
            double rotationL = newXLeft * cos
                             - newZDistance4L * sin;

            double yTL = ((-yHeight) + walls.controller.y) * 2;
            double yBL = ((0.5 - yHeight) + walls.controller.y)* 2;
            double rotationLZ = newZDistance4L * cos
                            + newXLeft * sin;

            double newXRight = (xRight  - walls.controller.x) * 2;
            double newZDistance4R = (zDistance - walls.controller.z) *2;
            double rotationR = newXRight * cos
                    - newZDistance4R * sin;

            double yTR = ((-yHeight) + walls.controller.y) * 2;
            double yBR = ((0.5 - yHeight) + walls.controller.y)* 2;
            double rotationRZ = newZDistance4R * cos
                                + newXRight * sin;

            double xPixelLeft = (rotationL / rotationLZ * walls.height + walls.width / 2);
            double xPixelRight = (rotationR / rotationRZ * walls.height + walls.width / 2);

            if (xPixelLeft >= xPixelRight) // do not render negatives
                return;

            int xPixelLeftInt = (int) xPixelLeft;
            int xPixelRightInt = (int) xPixelRight;
            if (xPixelLeftInt < 0 )
                xPixelLeftInt = 0;
            if (xPixelRightInt > App.width)
                xPixelRightInt = App.width;

            int yPixelTopLeftInt =  (int) (yTL / rotationLZ * App.height + App.height /2);
            int yPixelBottomLtInt = (int) (yBL / rotationLZ * App.height + App.height /2);
            int yPixelTopRightInt = (int) (yTR / rotationRZ * App.height + App.height /2);
            int yPixelBottomRInt =  (int) (yBR / rotationRZ * App.height + App.height /2);

            for (int x = xPixelLeftInt; x < xPixelRightInt; x++){
                double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
                double yPixelTop =    yPixelTopLeftInt + (yPixelTopRightInt - yPixelTopLeftInt) * pixelRotation ;
                double yPixelBottom = yPixelBottomLtInt + (yPixelBottomRInt - yPixelBottomLtInt) * pixelRotation;

                int yPixelTopInt = (int) yPixelTop;
                int yPixelBottomInt = (int) yPixelBottom;

                if (yPixelTopInt < 0)
                    yPixelTopInt = 0;
                if (yPixelBottomInt > App.height)
                    yPixelBottomInt = App.height;

                for (int y = yPixelTopInt; y < yPixelBottomInt; y++){
                    walls.displayMemory[x+y*walls.width] = fade(0x232499, zDistance - walls.controller.z); // color wall
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
        draw(roofFloor, 0, 0);
        System.out.println("X: " + input.MouseX + ", Y: " + input.MouseY );

        draw(walls, 0, 0);
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
        input.captureCurrentMousePos();
        roofFloor.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        walls.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        //testObject.reload();
        ticks++;

    }
}
