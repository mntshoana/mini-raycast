package theapp.entity;

import theapp.graphics.VisualBuffer;

public abstract class MobileEntity extends VisibleEntity {
    protected int direction = 2; // 0 = north, 1 = east, 2 = south, 3 = west
    protected boolean moving = false;
    protected int animationTick = 0;

    @Override
    public void update () {

    }

    public void move (int horiz, int verti) {
        if (horiz > 0) direction = 1; // east
        if (horiz < 0) direction = 3;  // west
        if (verti > 0) direction = 2;  // south
        if (verti < 0) direction = 0; // north

        if (!isCollision(horiz, verti)) {
            x += horiz;
            y += verti;
        }
    }

    private boolean isCollision(int horiz, int verti) {
        final int centerToFeet = 12;
        final int centerToFace = (horiz > 0) ? 8 : (horiz < 0) ? -8 : 0;
        final int xTileCoord = (x+horiz +centerToFace) / 16;
        final int yTileCoord = (y+verti +centerToFeet) / 16 ; //  bcoz player uses 32 x 32 sprite. moving reference from center to feet
        System.out.println("X: " + xTileCoord + " Y: " + yTileCoord);
        // detect collision
        return level.getTile(xTileCoord, yTileCoord).isSolid();

    }

    @Override
    public void render (VisualBuffer visualBuffer) {

    }
}
