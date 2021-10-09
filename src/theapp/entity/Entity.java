package theapp.entity;

import theapp.graphics.VisualBuffer;
import theapp.level.Level;

import java.util.Random;

public class Entity {
    private boolean removed = false; // from level
    protected Level level;
    protected final Random random = new Random();

    public void update() {

    }

    public void render (VisualBuffer visualBuffer) {
        // Meant to be empty
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
