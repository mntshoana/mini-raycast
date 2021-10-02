package theapp.entity;

import theapp.level.Level;

import java.util.Random;

public class Entity {
    private boolean removed = false; // from level
    protected Level level;
    protected final Random random = new Random();

    public void update() {

    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved () {
        return removed;
    }

    public void initLevel( Level level){
        this.level = level;
    }
}
