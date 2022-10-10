package theapp.entity;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

public class PlayerProjectile extends Projectile {
    private final double xInc, yInc;
    public PlayerProjectile (int x, int y, double direction) {
        super (x, y, direction);
        range = 200;
        speed = 4;
        damage = 20;
        rateOfFire = 15;

        xInc = (speed * Math.cos(angle) );
        yInc = (speed * Math.sin(angle) );

        sprite = Sprite.fireOrange.rotate(angle);
    }

    @Override
    public void update() {
        move(xInc, yInc);
    }

    // Hides move(int, int)
    public void move (double horiz, double verti) {
        x += horiz;
        y += verti;
    }

    @Override
    public void render (VisualBuffer visualBuffer) {
        visualBuffer.renderTileToBuffer((int)x, (int)y, sprite);
    }

}
