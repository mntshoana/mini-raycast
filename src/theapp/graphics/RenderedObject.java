package theapp.graphics;

public class RenderedObject {
    private final int width;
    private final int height;
    public final int[] displayMemory;

    public RenderedObject (final int width, final int height){
        this.width = width;
        this.height = height;
        this.displayMemory = new int[width * height];
    }

    public void draw(RenderedObject object, int xOffset, int yOffset){
        for (int y = 0; y < object.height; y++){
            int yPixel = y + yOffset;
            for (int x = 0; x < object.width; x++){
                int xPixel = x + xOffset;
                displayMemory[xPixel + this.width*(yPixel)]
                        = object.displayMemory[x + object.width*(y)];
            }
        }
    }
}