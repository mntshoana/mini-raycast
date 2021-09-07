package theapp.graphics;

import java.util.Random;

public class VisualBuffer {
    private int width, height;
    public int[] pixels;
    private int[] tiles = new int[64*64];
    public VisualBuffer (int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        Random random = new Random();
        for (int x = 0; x < 64; x++)
            for (int y = 0; y < 64; y++)
                tiles[x + y * 64] = random.nextInt(0xfffff);
    }

    public void renderToBuffer() {
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int indexScaledUp = (x>>4) + ((y>>4) << 6); // equal but faster than: x / 16 + (floor(y / 16) * 64 )
                pixels[x + y * width] = tiles[indexScaledUp]; // Dark gray
            }
        }
    }
}
