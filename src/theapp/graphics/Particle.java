package theapp.graphics;

public class Particle {
    private int width;
    private int height;
    public int[] pixels;

    public Particle (int width, int height, int colour) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        setColour(colour);
    }
    private void setColour (int colour) {
        for (int i = 0; i < width * height; i++)
            pixels[i] = colour;
    }

    public int getWidth() { return width;}
    public int getHeight() { return height;}

    public static Particle simpleWhite = new Particle(3,3, 0xffaaaaaa);

}
