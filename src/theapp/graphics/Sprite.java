package theapp.graphics;

public class Sprite {
    public final int SIZE;
    private int x, y;
    public int[] pixels;
    private SpriteSheet spriteSheet;

    public Sprite (int size, int x, int y, SpriteSheet sheet){
        SIZE = size;
        this.x = x * SIZE;
        this.y = y * SIZE;
        this.spriteSheet = sheet;

        pixels = new int[SIZE * SIZE];
        load();
    }

    public Sprite (int size, int colour) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        for (int i = 0; i < SIZE*SIZE; i++) {
            pixels[i] = colour;
        }
    }
    private void load() {
        for ( int y = 0; y < SIZE; y++){
            for (int x = 0; x < SIZE; x++){
                pixels[x+y*SIZE] = spriteSheet.sheet[ x + this.x + (y + this.y) * spriteSheet.getSize()];
            }

        }
    }

    public static Sprite testColor = new Sprite(16, 0, 0, SpriteSheet.testSheet);
    public static Sprite simpleBlue = new Sprite(16, 0x0034acd4);
    public static Sprite grass = new Sprite(16, 1, 0, SpriteSheet.testSheet);
}
