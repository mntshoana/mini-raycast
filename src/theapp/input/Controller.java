package theapp.input;

import theapp.level.RandomMaize;

import java.awt.event.KeyEvent;

public class Controller {
    public static double x=0.201, y,z=0.201;
    private static double xx, zz;
    public static double rotation;
    private static double rotation2;

    public static boolean moved;
    public static boolean sprint;
    public static boolean jumped;
    public static boolean crouched;
    private static int jumpTime;
    private static boolean jumpPeak;
    private static void update(boolean forward, boolean back,
                       boolean left, boolean right,
                       boolean turnLeft, boolean turnRight, boolean sprint){
        double rotationSpeed = 0.01;
        double walkSpeed = 1.2;
        double zMove = 0;
        double xMove = 0;
        Controller.sprint = sprint;
        if (forward)
            zMove+=0.75;
        if (back)
            zMove-=0.75;
        if (left)
            xMove-=0.75;
        if (right)
            xMove+=0.75;
        if (turnLeft)
            rotation2 -= rotationSpeed;
        if (turnRight)
            rotation2 += rotationSpeed;


        moved = ((forward || back || left || right) && (!jumped));

        if (sprint)
            walkSpeed = 1.75;
        if (crouched)
            walkSpeed = 0.75;
        xx += ((xMove * Math.cos(rotation) + zMove * Math.sin((rotation) ))) * walkSpeed;
        zz += ((zMove * Math.cos(rotation) - xMove * Math.sin((rotation) )) )* walkSpeed;

        if (xx < 0)
            if (RandomMaize.checkCollision(x+xx -4, z))
                x += xx;
        if (xx > 0)
            if (RandomMaize.checkCollision(x+xx +4, z))
                x += xx;
        if (zz < 0)
            if (RandomMaize.checkCollision(x, z + zz-4))
                z += zz;
        if (zz > 0)
            if (RandomMaize.checkCollision(x, z + zz+4))
                z += zz;

        xx *= 0.1;
        zz *= 0.1;
        rotation += rotation2;
        rotation2 *= 0.8;
    }
    private static void update( int turnLeftRight){
        double rotationSpeed = 0.005;
        rotation2 -= rotationSpeed*turnLeftRight;
        rotation += rotation2;
        rotation2 *= 0.8;
    }
    private static void update(boolean jump, boolean crouch){
        final double maxH = 15, minH = -10;
        double jumpIncrements = 0.75, crouchLevel = 2.5;

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

    public static void onKey(boolean[] keyPressed, int mouseX, int mouseY) {
        boolean forward = keyPressed[KeyEvent.VK_W];
        boolean back = keyPressed[KeyEvent.VK_S];
        boolean left = keyPressed[KeyEvent.VK_A];
        boolean right = keyPressed[KeyEvent.VK_D];
        boolean turnLeft = keyPressed[KeyEvent.VK_LEFT];
        boolean turnRight = keyPressed[KeyEvent.VK_RIGHT];

        boolean jump = keyPressed[KeyEvent.VK_SPACE];

        boolean crouch = keyPressed[KeyEvent.VK_CONTROL] || keyPressed[KeyEvent.VK_META];
        boolean sprint = keyPressed[KeyEvent.VK_ALT];

        update(forward, back, left, right, turnLeft, turnRight, sprint);
        update(jump, crouch);
        update(mouseX);
    }
}