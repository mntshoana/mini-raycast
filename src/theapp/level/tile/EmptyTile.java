package theapp.level.tile;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

public class EmptyTile extends Tile {
    public EmptyTile (Sprite sprite) {
        super (sprite);
    }

    @Override
    public void render (int x, int y, VisualBuffer visualBuffer) {
        visualBuffer.renderTileToBuffer(x, y, this);
    }
}
