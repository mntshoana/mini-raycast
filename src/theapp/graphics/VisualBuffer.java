package theapp.graphics;

public class VisualBuffer {
    private int width, height;
    public int[] pixels;

    public VisualBuffer (int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public void renderToBuffer() {
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                pixels[x + y * width] = 0X797979; // Dark gray
            }
        }
    }
}
