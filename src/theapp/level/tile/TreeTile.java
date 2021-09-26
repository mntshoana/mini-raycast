package theapp.level.tile;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

public class TreeTile extends Tile {
    public TreeTile (Sprite sprite) {
        super(sprite);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void render (int x, int y, VisualBuffer visualBuffer) {
        visualBuffer.renderTileToBuffer(x, y, this);
    }
}
