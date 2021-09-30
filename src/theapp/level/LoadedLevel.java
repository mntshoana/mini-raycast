package theapp.level;

import theapp.level.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadedLevel extends Level {
    public LoadedLevel (String path) {
        super(path);
    }

    @Override
    protected void loadLevel (String path) {
        try {
            BufferedImage image = ImageIO.read(LoadedLevel.class.getResource(path));
            width = image.getWidth();
            height = image.getHeight();
            tiles = new int[width * height];
            image.getRGB(0, 0, width, height, tiles, 0, width);
        }
        catch ( IOException e){
            System.out.println("Error: Unable to load level from file");
            e.printStackTrace();
        }
    }

    @Override
    protected void generateLevel () {

    }

    public Tile getTile (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.colourBlue;

            if (tiles[x + y * width] == 0x000000) return Tile.colourBlue;// black = wall (temporarily blue)
            else if (tiles[x + y * width] == Tile.COLOUR_GRASS) return Tile.grass;
            else if (tiles[x + y * width] == Tile.COLOUR_FLOWER) return Tile.flower;// yellow = flower
            else if (tiles[x + y * width] == Tile.COLOUR_TREE) return Tile.tree;// brown = tree
            else if (tiles[x + y * width] == Tile.COLOUR_ROCK) return Tile.rock;// gray = rock
            else return Tile.colourBlue;
    }
}
