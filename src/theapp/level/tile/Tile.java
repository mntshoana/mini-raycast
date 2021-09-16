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
}
