package theapp.level.location;

public class Coordinate {
    private int x;
    private int y;
    private final int PIXELS_PER_TILE = 16;
    private final int PIXELS_PER_PLAYER = 32;
    public Coordinate (int x, int y){
        this.x = x * PIXELS_PER_TILE + 8; // small correction as player was not drawn in the middle of 32 x32 block
        this.y = y * PIXELS_PER_TILE - PIXELS_PER_PLAYER/8;
    }

    public int x() { return x;}
    public int y() { return y;}
    public int[] xy() { return new int[]{x,y};}
}
