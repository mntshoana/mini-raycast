package theapp.input;

public class Controller {
    public double x, y,z;
    public double xx, zz;
    public double rotation;
    public double rotation2;

    public boolean moved;
    public boolean jumped;
    public boolean crouched;
    private int jumpTime;
    private boolean jumpPeak;
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


        moved = ((forward || back || left || right) && (!jumped));

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
        final double maxH = 15, minH = -10;
        double jumpIncrements = 0.5, crouchLevel = 2.5;

        crouched = (y < 0) ? true: false;
        jumped = (y > 0 ) ? true: false;

        if (y == 0)
            jumpPeak = false;
        if (jumped && !jump)
            jumpPeak = true;

        if (!jumpPeak && jump /*button*/ && y >= 0 && y < maxH) { // elevate with button
                if (y <= 2)
                    y +=  jumpIncrements;
                else if (y < 5)
                    y += (4 * jumpIncrements);
                else if (y < 10)
                    y += (3 * jumpIncrements);
                else if (y < 13)
                    y += (2 * jumpIncrements);
                else
                    y += jumpIncrements;

                if (y == maxH)
                    jumpPeak = true;

                jumpTime += 1; // moment of hover in air
        }
        else if (!jumpPeak && !jump /*button*/ && jumped && y < maxH) { // incomplete/short jump
                y += jumpIncrements;
                if (jumpTime > 2) {
                    jumpPeak = true;
                    jumpTime--;
                }
        }
        else if (!jump /*button*/ && jumped || jumpPeak == true) { // de-elevate
            jumpPeak = true;

            if (y > 13)
                y -= jumpIncrements;
            else if (y > 10 ) {
                if (jumpTime <= 2) {// hover a little
                    jumpTime--;
                    y -= jumpIncrements;
                } else
                    y -= (2 * jumpIncrements);
            }
            else if (y > 5)
                y -= (3 * jumpIncrements);
            else if (y > 2)
                y -= (2 * jumpIncrements);
            else
                y -= jumpIncrements;
            if (y < 0)
                y = 0;
            jumpTime = -1; // wait time before next jump
        }


        if (crouch && y <= 0.0 && y > minH)
            y -= crouchLevel;
        else if (!crouch && crouched){
            y += crouchLevel;
        }
    }
}