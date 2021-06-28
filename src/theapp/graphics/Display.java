package theapp.graphics;

import java.util.Random;

import theapp.core.App;

public class Display extends RenderedObject{
    private RenderedObject testObject;
    public Display(int width, int height){
        super(width, height);

        // test the class
        testObject = new RenderedObject(320, 180); // fill left 400 pixels of the screen with random colors
        // Fill testObject with random numbwrs
        Random random = new Random();
        for (int i = 0; i < testObject.width*testObject.height; i++){
            testObject.displayMemory[i] = random.nextInt();
        }
    }

    public void render(){
        for (int i = 0, len = App.width * App.height; i < len; i++)
            displayMemory[i] = 0;
        int xAnimOffset = (int) (Math.sin(System.currentTimeMillis() % 1000.0 / 1000 * Math.PI*2) * 50);
        int yAnimOffset = (int) (Math.cos(System.currentTimeMillis() % 1000.0 / 1000 * Math.PI*2) * 50);
        draw(testObject, (App.width - testObject.width)/2+ xAnimOffset, (App.height - testObject.height)/ 2 + yAnimOffset);

    }
}
