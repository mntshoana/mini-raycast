package theapp.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    private String path;
    private final int SIZE;
    public int[] sheet;

    public SpriteSheet (String path, int size){
        this.path = path;
        this.SIZE = size;
        sheet = new int[SIZE * SIZE];
    }

    private void loadSheet(){
        try {
            BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
            int w = image.getHeight();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, sheet, 0, w)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
