package theapp.graphics;

import java.util.Random;

public class VisualBuffer {
    private int width, height;
    public int[] pixels;
    private final int SIDE = 64;
    private int[] tiles = new int[SIDE*SIDE];
    public VisualBuffer (int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        Random random = new Random();
        for (int x = 0; x < SIDE; x++)
            for (int y = 0; y < SIDE; y++)
                tiles[x + y * SIDE] = random.nextInt(0xffffff);
    }

    public void renderToBuffer(int xOffset, int yOffset) {
        for (int y = 0; y < height; y++){
            int yInd = ((y+yOffset) >> 4) & (SIDE-1);  // equal but faster than: floor(y+yoffset) / 16
            for (int x = 0; x < width; x++){
                int xInd = ((x+xOffset) >> 4) & (SIDE-1); // equal but faster than: floor(x+xoffset) / 16
                //int indexScaledUp = xInd + (yInd << 6);
                //pixels[x + y * width] = tiles[indexScaledUp]; // Dark gray
                int index = ((x+xOffset )&15) + ((y+yOffset)&15)  * Sprite.testColor.SIZE;
                pixels[x + y * width] = Sprite.testColor.pixels[index]; // Dark gray
            }
        }
    }
}
