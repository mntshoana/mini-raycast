package theapp.input;

public class Controller {
    public double x, y,z;
    public double xx, zz;
    public double rotation;
    public double rotation2;

    public boolean jumped;
    public boolean crouched;
    private int jumpTime;
    public void update(boolean forward, boolean back,
                       boolean left, boolean right,
                       boolean turnLeft, boolean turnRight, boolean sprint){
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

        if (sprint)
            walkSpeed = 1.6;
        if (crouched)
            walkSpeed = 0.5;
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
    public void update(boolean jump, boolean crouch){
        double jumpHeight = 1.5, crouchLevel = 2.5;
        if (crouch)
            crouched = true;
        else
            crouched = false;
        if (y == 0)
            jumped = false;
        if (y == 15)
            jumped = true;


        if (jump && y >= 0 && y < 15 && jumped == false) {
            if (jumpTime >= 0) {
                y += jumpHeight;
                jumpTime = 2;
            }
            else
                jumpTime++;
        }
        else if (!jump && y > 0 || jumped == true && y > 0) {
            jumped = true;
            if (jumpTime > 0)
                jumpTime--;
            else {
                y -= jumpHeight;
                jumpTime = -5;
            }
        }

        if (crouch && y <= 0 && y > -10)
            y-= crouchLevel;
        else if (!crouch && y < 0){
            y += crouchLevel;
        }
    }
}