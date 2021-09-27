package theapp.level;

import theapp.level.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
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
            int width = image.getWidth();
            int height = image.getHeight();
            image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        catch ( IOException e){
            System.out.println("Error: Unable to load level from file");
            e.printStackTrace();
        }
    }

    @Override
    protected void generateLevel () {
        
    }
}
