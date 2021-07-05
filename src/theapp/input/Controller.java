package theapp.input;

public class Controller {
    public double x, z;
    public double xx, zz;
    public double rotation;
    public double rotation2;
    public void update(boolean forward, boolean back,
                       boolean left, boolean right,
                       boolean turnLeft, boolean turnRight){
        double rotationSpeed = 0.005;
        double walkSpeed = 1;
        double zMove = 0;
        double xMove = 0;

        if (forward)
            zMove++;
        if (back)
            zMove--;
        if (left)
            xMove--;
        if (right)
            xMove++;
        if (turnLeft)
            rotation2 -= rotationSpeed;
        if (turnRight)
            rotation2 += rotationSpeed;

        xx += ((xMove * Math.cos(rotation) + zMove * Math.sin((rotation) ))) * walkSpeed;
        zz += ((zMove * Math.cos(rotation) - xMove * Math.sin((rotation) )) )* walkSpeed;
        x += xx;
        z += zz;

        xx *= 0.1;
        zz *= 0.1;
        rotation += rotation2;
        rotation2 *= 0.8;
    }
    public void update(int forward,
                       int leftRight,
                       int turnLeftRight){
        double rotationSpeed = 0.005;
        double walkSpeed = 1;
        double zMove = 0;
        double xMove = 0;

        zMove+= forward;
        xMove+=leftRight;
        rotation2 -= rotationSpeed*turnLeftRight;


        xx += ((xMove * Math.cos(rotation) + zMove * Math.sin((rotation) ))) * walkSpeed;
        zz += ((zMove * Math.cos(rotation) - xMove * Math.sin((rotation) )) )* walkSpeed;
        x += xx;
        z += zz;

        xx *= 0.1;
        zz *= 0.1;
        rotation += rotation2;
        rotation2 *= 0.8;
    }
}