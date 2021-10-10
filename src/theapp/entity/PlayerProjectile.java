package theapp.entity;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

public class PlayerProjectile extends Projectile {
    private final int xInc, yInc;
    public PlayerProjectile (int x, int y, double direction) {
        super (x, y, direction);
        range = 200;
        speed = 4;
        damage = 20;
        rateOfFire = 15;

        xInc = (int) (speed * Math.cos(angle) );
        yInc = (int) (speed * Math.sin(angle) );

        sprite = Sprite.fireOrange;
    }

    @Override
    public void update() {
        move(xInc, yInc);
    }

    @Override
    public void move (int horiz, int verti) {
        x += horiz;
        y += verti;
    }

    @Override
    public void render (VisualBuffer visualBuffer) {
        visualBuffer.renderTileToBuffer(x, y, sprite);
    }

}
