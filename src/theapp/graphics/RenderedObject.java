package theapp.graphics;

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
        controller.update( mouseX);
        loader.run();
    }

    public static int fade(int pixel, double z){
        // Fade using gradient
        final double fadeDist = 8000.0;
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

        return (r <<16) | (g << 8) | b;
    }
}