package theapp.graphics;

import theapp.core.App;
import theapp.input.Controller;

import java.awt.event.KeyEvent;

public class RenderedObject {
    private Runnable loader;// to hold the method that populats the object with a color maps
    public Controller controller;
    protected final int width;
    protected final int height;
    public final int[] displayMemory;

    public RenderedObject (final int width, final int height){
        this.width = width;
        this.height = height;
        this.displayMemory = new int[width * height];
        controller = new Controller();
    }

    public void load(Runnable method){
        this.loader = method;
        loader.run();
    }
    public void reload(boolean[] keyPressed, int mouseX, int mouseY){
        boolean forward = keyPressed[KeyEvent.VK_W];
        boolean back = keyPressed[KeyEvent.VK_S];
        boolean left = keyPressed[KeyEvent.VK_A];
        boolean right = keyPressed[KeyEvent.VK_D];
        boolean turnLeft = keyPressed[KeyEvent.VK_LEFT];
        boolean turnRight = keyPressed[KeyEvent.VK_RIGHT];

        boolean jump = keyPressed[KeyEvent.VK_SPACE];

        boolean crouch = keyPressed[KeyEvent.VK_CONTROL] || keyPressed[KeyEvent.VK_META];
        boolean sprint = keyPressed[KeyEvent.VK_ALT];

        controller.update(forward, back, left, right, turnLeft, turnRight, sprint);
        controller.update(jump, crouch);
        controller.update(0, 0, mouseX);
        loader.run();
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
                    displayMemory[xPixel + this.width * (yPixel)]
                            = object.displayMemory[x + object.width * (y)];
                }
            }
        }
    }
}