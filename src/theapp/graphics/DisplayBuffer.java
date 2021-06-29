package theapp.graphics;

import java.util.Random;

import theapp.core.App;

public class DisplayBuffer extends RenderedObject{

    // just make 2 throw away test object
    private RenderedObject testObject;
    private RenderedObject testOImageFromCode;
    // a counter to help with color changes over time in a controlled way
    private long ticks;

    // Constructor
    public DisplayBuffer(int width, int height){
        super(width, height);

        testObject = new RenderedObject(320, 180); // fill left 400 pixels of the screen with random colors

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
        });

        testOImageFromCode = new RenderedObject(App.width, App.height);
        testOImageFromCode.load(()->{
            RenderedObject object = testOImageFromCode;
            double floorDistance = 16;
            double ceilingDistance = 8;
            double forward = ticks / 5.0;
            double rightward = ticks/ 5.0;
            for (int y = 0; y < object.height; y++ ){
                double ceiling = ( y - object.height / 2.0)  / object.height;
                double z = floorDistance / ( (ceiling > 0) ? -ceiling : ceiling );

                // clip pixels towards the center that are too far
                if (y > object.height * 45 / 100 && y < testOImageFromCode.height * 55 / 100  )
                    continue;

                for (int x = 0; x < object.width; x++){
                    double depth = (x - object.width / 2.0) / object.height;
                    depth = depth*z + ticks;
                    int xx = (int) depth  & 0XB;
                    int yy = (int) (z  + ticks) & 0XB;

                    int pixel = ((xx * 16) << 8 | (xx * 16) << 16 ) | (yy *16 << 16);

                    testOImageFromCode.displayMemory[x+ testOImageFromCode.width*(y)] = pixel;
                }
            }
        });
    }

    // Renders everything to the screen
    public void update(){
        // Clear buffer
        for (int i = 0, len = App.width * App.height; i < len; i++)
            displayMemory[i] = 0;

        // Redraw
        // Playing with background like image drawn from code
        draw(testOImageFromCode, 0, 0);

        // Playing around with sin and cos of a circumference to create an animatioon
        if (ticks % 200 == 0)
            testObject.reload();

        for (int i = 0; i < 2000; i+=70) {
            int xAnimOffset = (int) (Math.sin(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);
            int yAnimOffset = (int) (Math.cos(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);

            draw(testObject, (App.width - testObject.width) / 2 + xAnimOffset, (App.height - testObject.height) / 2 - yAnimOffset);
        }
        tick();
    }
    private void tick(){
        testOImageFromCode.reload();
        //testObject.reload();
        ticks++;
    }
}
