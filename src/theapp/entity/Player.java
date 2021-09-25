package theapp.entity;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;
import theapp.input.Keyboard;

import java.security.SecureRandomParameters;

public class Player extends MobileEntity {
    private Keyboard controller;
    public Player(Keyboard keyboard) { // default location
        controller = keyboard;
        sprite = Sprite.character1Down;
    }
    public Player (Keyboard keyboard, int x, int y) {
        this.x = x;
        this.y = y;
        controller = keyboard;
        sprite = Sprite.character1Down;
    }

    @Override
    public void update (){
        if (animationTick < 0xFFFF)
            animationTick++;
        else
            animationTick = 0;
        int tempX = 0, tempY = 0;
        if (controller.up) tempY--;
        if (controller.down) tempY++;
        if (controller.left) tempX--;
        if (controller.right) tempX++;

        if (tempX != 0 || tempY != 0) {
            move(tempX, tempY);
            moving = true;
        }
        else
            moving = false;
    }

    @Override
    public void render (VisualBuffer visualBuffer) {
        boolean leftFoot = animationTick % 20 > 10;
        if (direction == 0) {
            sprite = Sprite.character1Up;
            if (moving && leftFoot)
                sprite = Sprite.character1UpLFF;
            if (moving && !leftFoot)
                sprite = Sprite.character1UpRFF;
        }
        if (direction == 1){
            sprite = Sprite.character1Right;
            if (moving && leftFoot)
                sprite = Sprite.character1RightLFF;
            if (moving && !leftFoot)
                sprite = Sprite.character1RightRFF;
        }
        if (direction == 2) {
            sprite = Sprite.character1Down;
            if (moving && leftFoot)
                sprite = Sprite.character1DownLFF;
            if (moving && !leftFoot)
                sprite = Sprite.character1DownRFF;
        }
        if (direction == 3) {
            sprite = Sprite.character1Left;
            if (moving && leftFoot)
                sprite = Sprite.character1LeftLFF;
            if (moving && !leftFoot)
                sprite = Sprite.character1LeftRFF;
        }

        visualBuffer.renderPlayerToBuffer(x - sprite.SIZE/2, y - sprite.SIZE/2, sprite);
    }
}
