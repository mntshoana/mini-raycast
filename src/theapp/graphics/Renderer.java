package theapp.graphics;

import theapp.core.App;
import theapp.input.Controller;
import theapp.input.InputHandler;
import theapp.level.RandomMaize;

public class Renderer {

    // just make 2 throw away test object
    private RenderedScene scene;
    private RandomMaize level;

    private InputHandler input;
    private App parent;
    // a counter to help with color changes over time in a controlled way
    private long ticks;

    private static double walkingBob;
    // Constructor
    public Renderer(App parent){
        input = new InputHandler();

        parent.addKeyListener(input);
        parent.addFocusListener(input);
        parent.addMouseListener(input);
        parent.addMouseMotionListener(input);
        this.parent = parent;

        updateWalkingBob();
        scene = new RenderedScene();
        level = new RandomMaize(5, 5, this);
    }

    private void updateWalkingBob(){
        walkingBob = Math.sin(ticks / Math.PI);
        if (!Controller.moved)
            walkingBob = 0.0;
        else if (Controller.crouched && Controller.moved)
            walkingBob = Math.sin(ticks / Math.PI /2) *  0.3;
        else if (Controller.moved && Controller.sprint)
            walkingBob *= Math.sin(ticks / Math.PI * 1.5) *  0.7;
        else if (Controller.moved)
            walkingBob *= 0.5;
    }
    public static double getWalkingBob(){
        return walkingBob;
    }
    public void draw(RenderedObject object, int xOffset, int yOffset){
        for (int y = 0; y < object.height; y++){
            int yPixel = y + yOffset;
            if (yPixel < 0 || yPixel >= App.height)
                continue;;
            for (int x = 0; x < object.width; x++){
                int xPixel = x + xOffset;
                if (xPixel < 0 || xPixel >= App.width)
                    continue;

                int alpha = object.displayMemory[x + object.width*(y)]; // allow empty pixels to not be written which allows transparency
                if (alpha > 0) {
                    parent.getBuffer()[xPixel + App.width * (yPixel)]
                            = object.displayMemory[x + object.width * (y)];
                }
            }
        }
    }

    // Renders everything to the screen
    public void update(){
        tick();
        // Clear buffer
        for (int i = 0, len = App.width * App.height; i < len; i++)
            parent.getBuffer()[i] = 0;

        // Playing with background like image drawn from code
        draw(scene, 0, 0);
        System.out.println("X: " + input.MouseX + ", Y: " + input.MouseY );
        level.draw();
    }
    private void tick(){
        walkingBob = getWalkingBob();
        input.captureCurrentMousePos();
        Controller.onKey(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        scene.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        //wall.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        ticks++;
    }
}
