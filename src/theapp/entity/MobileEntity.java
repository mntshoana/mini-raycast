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
        if (horiz != 0 && verti != 0){
            // diagonal
            // so collision does not prevent both movements if the other is possible
            move (horiz, 0);
            move (0, verti);
            return;
        }
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
        boolean collides = false;
        for (int corner = 0; corner < 4; corner++){
            int xWise = corner % 2 * 12 - 6;
            int yWise = corner / 2 * 8 + 6;
            if (level.getTile((x+horiz + xWise) / 16, (y+verti + yWise) / 16).isSolid() )
                collides = true;
        }
       return collides;

    }

    @Override
    public void render (VisualBuffer visualBuffer) {

    }

    protected void shootToMouse(int x, int y, double direction) {
        System.out.println("Angle of projection: " + direction);
    }
}
