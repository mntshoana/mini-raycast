package theapp.entity;

import theapp.graphics.VisualBuffer;

public abstract class MobileEntity extends VisibleEntity {
    protected int direction = 0; // 0 = north, 1 = east, 2 = south, 3 = west
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

        if (!isCollision()) {
            x += horiz;
            y += verti;
        }
    }

    private boolean isCollision() {
        return false;
    }

    @Override
    public void render (VisualBuffer visualBuffer) {

    }
}
