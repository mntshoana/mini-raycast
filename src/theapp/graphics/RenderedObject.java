package theapp.graphics;

import theapp.core.App;

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
    public void reload(){
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