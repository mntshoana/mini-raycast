package theapp.level;

import theapp.level.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadedLevel extends Level {

    private Tile[] tiles;
    private int[] pixels;

    public LoadedLevel (String path) {
        super(path);
    }

    @Override
    protected void loadLevel (String path) {
        try {
            BufferedImage image = ImageIO.read(LoadedLevel.class.getResource(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        catch ( IOException e){
            System.out.println("Error: Unable to load level from file");
            e.printStackTrace();
        }
    }

    @Override
    protected void generateLevel () {
        tiles = new Tile[width * height];
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i] == 0x000000) tiles[i] = Tile.colourBlue;// black = wall (temporarily blue)
            else if (pixels[i] == 0xff00ff00) tiles[i] = Tile.grass; // green = grass
            else if (pixels[i] == 0xffffff00) tiles[i] = Tile.flower;// yellow = flower
            else if (pixels[i] == 0xff7f7f00) tiles[i] = Tile.tree;// brown = tree
            else if (pixels[i] == 0xff7f7f7f) tiles[i] = Tile.rock;// gray = rock
            else tiles[i] = Tile.colourBlue;
        }
    }

    public Tile getTile (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.colourBlue;

        return tiles[x + y * width];
    }
}
