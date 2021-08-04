package theapp.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Texture {
    public static RenderedObject floor = loadBitmap("/textures/greenBlock.png");
    public static RenderedObject ceiling = loadBitmap("/textures/blueBlock.png");
    public static RenderedObject wall = loadBitmap("/textures/colorBlock.png");
    public static RenderedObject loadBitmap(String filename){
        try{
            BufferedImage image = ImageIO.read(Texture.class.getResource(filename));
            int width = image.getWidth();
            int height = image.getHeight();
            RenderedObject result = new RenderedObject(width, height);
            image.getRGB(0,0,width, height, result.displayMemory, 0, width);
            return result;
        } catch (IOException e) {
            System.out.println(" [ERROR] Unable to load texture");
            throw new RuntimeException(e);
        }

    }
}
