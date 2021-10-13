package theapp.entity;

import theapp.core.Game;
import theapp.graphics.Sprite;
import theapp.graphics.VisualBuffer;
import theapp.input.Keyboard;
import theapp.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class Player extends MobileEntity {
    private int pauseRateOfFire;
    private Keyboard controller;
    private List<Projectile> projectiles = new ArrayList<>();
    public Player(Keyboard keyboard) { // default location
        controller = keyboard;
    }
    public Player (Keyboard keyboard, int x, int y) {
        this.x = x;
        this.y = y;
        pauseRateOfFire = Projectile.pauseRateOfFire;
        controller = keyboard;
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

        // Shoot
        if (Mouse.getButton() == 1) {
            if (pauseRateOfFire > 0)
                pauseRateOfFire--;
            else {
                double opposite = Mouse.getY() - (Game.height * Game.scale) / 2;
                double adjacent = Mouse.getX() - (Game.width * Game.scale) / 2;
                double projectileDirection = Math.atan2(opposite, adjacent);
                shootToMouse(x, y, projectileDirection);
                pauseRateOfFire = Projectile.pauseRateOfFire;
            }
        }
        // Clear projectiles
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).isRemoved()) {
                projectiles.remove(i);
            }
        }

        // move player
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

    protected void shootToMouse(int x, int y, double direction) {
        Projectile p = new PlayerProjectile(x, y, direction);
        projectiles.add(p);
        level.addEntity(p);
    }

}
