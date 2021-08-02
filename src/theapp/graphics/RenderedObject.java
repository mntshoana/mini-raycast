package theapp.graphics;

public class RenderedObject {
    private Runnable loader;// to hold the method that populats the object with a color maps
    protected final int width;
    protected final int height;
    public final int[] displayMemory;

    public RenderedObject (final int width, final int height){
        this.width = width;
        this.height = height;
        this.displayMemory = new int[width * height];
    }

    public void load(Runnable method){
        this.loader = method;
        loader.run();
    }
    public void reload(boolean[] keyPressed, int mouseX, int mouseY){
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