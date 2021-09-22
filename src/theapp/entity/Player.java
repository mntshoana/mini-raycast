package theapp.entity;

import theapp.input.Keyboard;

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
    public void render () {

    }
}
