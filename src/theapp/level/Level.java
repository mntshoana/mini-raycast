package theapp.level;

import theapp.graphics.VisualBuffer;

public class Level {
    protected int width, height;
    protected int[] tiles;

    public Level (int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new int[width * height];
        generateLevel();
    }

    public Level(String path){
        loadLevel(path);
    }
    protected void generateLevel(){

    }

    private void loadLevel (String path){

    }

    public void update() {

    }

    public void render (int x, int y, VisualBuffer visualBuffer) {

    }
}
