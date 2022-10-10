package theapp.entity;

public class Projectile extends MobileEntity {
    // Overwrite Entity int x, y (more precision is required)
    protected double x, y;
    protected final int xSpawn;
    protected final int ySpawn;
    protected double angle;
    protected double speed;
    protected double rateOfFire;
    protected double range;
    protected double damage;

    public Projectile (int x, int y, double direction) {
        this.x = xSpawn = x;
        this.y = ySpawn = y;
        angle = direction;
    }
}
