package theapp.entity;

import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;
import theapp.input.Keyboard;

import java.security.SecureRandomParameters;

public class Player extends MobileEntity {
    private Keyboard controller;
    public Player(Keyboard keyboard) { // default location
        controller = keyboard;
    }
    public Player (Keyboard keyboard, int x, int y) {
        this.x = x;
        this.y = y;
        controller = keyboard;
    }

    @Override
    public void update (){
        int tempX = 0, tempY = 0;
        if (controller.up) tempY--;
        if (controller.down) tempY++;
        if (controller.left) tempX--;
        if (controller.right) tempX++;

        if (tempX != 0 || tempY != 0)
            move(tempX, tempY);
    }

    @Override
    public void render (VisualBuffer visualBuffer) {
        if (direction == 0) sprite = Sprite.character1Up;
        if (direction == 1) sprite = Sprite.character1Right;
        if (direction == 2) sprite = Sprite.character1Down;
        if (direction == 3) sprite = Sprite.character1Left;

        visualBuffer.renderPlayerToBuffer(x - sprite.SIZE/2, y - sprite.SIZE/2, sprite);
    }
}
