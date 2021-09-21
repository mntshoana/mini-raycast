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
        if (controller.up) y--;
        if (controller.down) y++;
        if (controller.left) x--;
        if (controller.right) x++;
    }

    @Override
    public void render () {

    }
}
