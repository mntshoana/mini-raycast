package theapp.graphics;

import java.util.Random;

import theapp.core.App;

public class DisplayBuffer extends RenderedObject{
    // just make 2 throw away test object
    private RenderedObject testObject;
    private RenderedObject testOImageFromCode;
    private static double testImageFromCodetimer;
    // a counter to help with color changes over time in a controlled way
    private int runCount;

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
        testImageFromCodetimer = 0.0;
        testOImageFromCode.load(()->{
            for (int y = 0; y < testOImageFromCode.height; y++ ){
                double yDiff = ( y - testOImageFromCode.height / 2.0)  / testOImageFromCode.height; // range of -height/2.5 to -1/2.5
                if (yDiff < 0)
                    yDiff = -yDiff;
                double quotient = 8 / yDiff; // == range between -250/height to -250
                testImageFromCodetimer += 0.00005;

                if (y > testOImageFromCode.height * 45 / 100 && y < testOImageFromCode.height * 55 / 100  )
                    continue;
                for (int x = 0; x < testOImageFromCode.width; x++){
                    double xDiff = (x - testOImageFromCode.width / 2.0) / testOImageFromCode.height; // range of -width/2 to -1/2
                    double z = xDiff * quotient + testImageFromCodetimer;
                    int xx = (int) z  & 0XB;
                    int yy = (int) (quotient  + testImageFromCodetimer) & 0XB;

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
        testOImageFromCode.reload();
        draw(testOImageFromCode, 0, 0);

        // Playing around with sin and cos of a circumference to create an animatioon
        if (runCount % 600 == 0) {
            testObject.reload();
            runCount = 1;
        }

        for (int i = 0; i < 2000; i+=70) {
            int xAnimOffset = (int) (Math.sin(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);
            int yAnimOffset = (int) (Math.cos(((System.currentTimeMillis()-i) % 5000.0 / 5000.0) * Math.PI * 2) * 250);

            runCount++;
            draw(testObject, (App.width - testObject.width) / 2 + xAnimOffset, (App.height - testObject.height) / 2 - yAnimOffset);
        }
    }
}
