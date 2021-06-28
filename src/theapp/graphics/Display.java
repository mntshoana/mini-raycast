package theapp.graphics;

import java.util.Random;

public class Display extends RenderedObject{
    private RenderedObject testOjbect;
    public Display(int width, int height){
        super(width, height);

        // test the class
        testOjbect = new RenderedObject(400, 720); // fill left 400 pixels of the screen with random colors
        // Fill testObject with random numbwrs
        Random random = new Random();
        for (int i = 0; i < 400*400; i++){
            testOjbect.displayMemory[i] = random.nextInt();
        }
    }

    public void render(){
        draw(testOjbect, 0,0);

    }
}
