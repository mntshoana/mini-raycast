package theapp.entity;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;

import java.util.List;

public class PlayerProjectile extends Projectile {
    private final double xInc, yInc;
    public PlayerProjectile (int x, int y, double direction) {
        super (x, y, direction);
        range = 200;
        speed = 4;
        damage = 20;

        xInc = (speed * Math.cos(angle) );
        yInc = (speed * Math.sin(angle) );

        sprite = Sprite.fireOrange.rotate(angle);
    }

    @Override
    public void update() {
        if (level.isCollision((int)x, (int)y, (int)xInc, (int)yInc, sprite.SIZE)) {
            remove();
            List<ParticleEntity> particles = ParticleEntity.generateParticles((int)x, (int)y, 5, 25);
            level.addEntityList(particles);
        }
        else
            move();

    }

    // Hides move(int, int)
    public void move () {
        x += xInc;
        y += yInc;
        if (calcDistance() > range)
            remove();
    }

    @Override
    public void render (VisualBuffer visualBuffer) {
        visualBuffer.renderTileToBuffer((int)x, (int)y, sprite);
    }

}
