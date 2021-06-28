package theapp.graphics;

import theapp.core.App;

public class RenderedObject {
    protected final int width;
    protected final int height;
    public final int[] displayMemory;

    public RenderedObject (final int width, final int height){
        this.width = width;
        this.height = height;
        this.displayMemory = new int[width * height];
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
                displayMemory[xPixel + this.width*(yPixel)]
                        = object.displayMemory[x + object.width*(y)];
            }
        }
    }
}