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
    protected double distance;
    protected double damage;

    public Projectile (int x, int y, double direction) {
        this.x = xSpawn = x;
        this.y = ySpawn = y;
        angle = direction;
    }

    public double calcDistance (){
        return distance = Math.sqrt( Math.abs( (xSpawn-x)*(xSpawn-x) + (ySpawn-y)*(ySpawn-y) ) );
    }
}
