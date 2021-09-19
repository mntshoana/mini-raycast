package theapp.level;

import theapp.graphics.VisualBuffer;
import theapp.level.tile.Tile;

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

    public void render (int xOffset, int yOffset, VisualBuffer visualBuffer) {
        visualBuffer.setOffsets(xOffset, yOffset);
        int addedRenderLength = 16;
        int x0 = xOffset >> 4;
        int x1 = (xOffset + visualBuffer.getWidth() + addedRenderLength) >> 4;
        int y0 = yOffset >> 4;
        int y1 = (yOffset + visualBuffer.getHeight() + addedRenderLength) >> 4;

        for (int y = y0; y < y1; y++){
            for (int x = x0; x < x1; x++){
                getTile(x, y).render(x << 4,y << 4, visualBuffer);
            }
        }
    }

    public Tile getTile (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.colourBlue;

        if (tiles[x + y * width] == 0)
            return Tile.grass;

        return Tile.colourBlue;
    }
}
