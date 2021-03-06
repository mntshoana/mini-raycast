package theapp.level;

import theapp.entity.Entity;
import theapp.graphics.VisualBuffer;
import theapp.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Level {
    protected int width, height;
    protected int[] tiles;
    private List<Entity> entityList = new ArrayList<>();

    public Level (int width, int height) {
        this.width = width;
        this.height = height;
        generateLevel();
    }

    public Level(String path){
        loadLevel(path);
        generateLevel();
    }
    protected void generateLevel(){

    }

    protected void loadLevel (String path){

    }

    public void addEntity (Entity e) {
        e.initLevel(this);
        entityList.add(e);
    }
    public void addEntityList (List<? extends Entity> list) {
        for (Entity e : list) {
            e.initLevel(this);
            entityList.add(e);
        }
    }

    public boolean isCollision(int xPos, int yPos, int horizDirection, int vertiDirection, int objectSize) {
        boolean collides = false;
        for (int corner = 0; corner < 4; corner++){
            int xWise = corner % 2 * objectSize;
            int yWise = corner / 2 * objectSize;
            if (getTile((xPos+horizDirection + xWise) / 16, (yPos+vertiDirection + yWise) / 16).isSolid() )
                collides = true;
        }
        return collides;

    }

    public void update() {
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).isRemoved()) {
                entityList.remove(i);
                continue;
            }
            entityList.get(i).update();
        }
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
        for (int i = 0; i < entityList.size(); i++)
            entityList.get(i).render(visualBuffer);
    }

    public Tile getTile (int x, int y) {
        return Tile.colourBlue;
    }

    public static Level level1 = new LoadedLevel("/textures/simpleMap.png");
}
