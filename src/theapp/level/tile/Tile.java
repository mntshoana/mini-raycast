package theapp.level.tile;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

public class Tile {
    public int x, y;
    public Sprite sprite;

    public Tile (Sprite sprite){
        this.sprite = sprite;
    }

    public boolean isSolid() {
        return false;
    }

    public void render (int x, int y, VisualBuffer visualBuffer) {

    }

    public static Tile grass = new GrassTile(Sprite.grass);
    public static Tile rock = new RockTile(Sprite.rock);
    public static Tile flower = new FlowerTile(Sprite.flower);
    public static Tile tree = new TreeTile(Sprite.tree);
    public static Tile colourBlue = new EmptyTile(Sprite.simpleBlue);

    public static final int COLOUR_GRASS = 0xff00ff00;
    public static final int COLOUR_FLOWER = 0xffffff00;
    public static final int COLOUR_TREE = 0xff7f7f00;
    public static final int COLOUR_ROCK = 0xff7f7f7f;
}
