package theapp.entity;

import theapp.graphics.Particle;
import theapp.graphics.VisualBuffer;

import java.util.ArrayList;
import java.util.List;

public class ParticleEntity extends Entity {
    private int x, y;
    private double xDouble, xInc, yDouble, yInc;
    private int life;
    private Particle particle;

    public ParticleEntity (int x, int y, int life) {
        xDouble = this.x = x;
        yDouble = this.y = y;
        this.life = life;
        particle = Particle.simpleWhite;

        xInc = random.nextGaussian();
        yInc = random.nextGaussian();
    }

    public static List<ParticleEntity> generateParticles (int x, int y, int life, int amount) {
        List<ParticleEntity> particles = new ArrayList<ParticleEntity>();
        for (int i = 0; i < amount; i++){
            particles.add(new ParticleEntity(x, y, life));
        }
        return particles;
    }

    @Override
    public void update() {
        x = (int)(xDouble += xInc);
        y = (int)(yDouble += yInc);
    }

    @Override
    public void render(VisualBuffer visualBuffer){
        visualBuffer.renderParticleToBuffer(x, y, particle, true);
    }

}

