package theapp.graphics;

import theapp.level.tile.Tile;

import java.util.Random;

public class VisualBuffer {
    private int xOffset, yOffset;
    private int width, height;
    public int[] pixels;
    private final int SIDE = 64;
    private int[] tiles = new int[SIDE*SIDE];
    public VisualBuffer (int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        Random random = new Random();
        for (int x = 0; x < SIDE; x++)
            for (int y = 0; y < SIDE; y++)
                tiles[x + y * SIDE] = random.nextInt(0xffffff);
    }

    public void renderToBuffer(int xOffset, int yOffset) {
        for (int y = 0; y < height; y++){
            //int yInd = ((y+yOffset) >> 4) & (SIDE-1);  // equal but faster than: floor(y+yoffset) / 16
            for (int x = 0; x < width; x++){
                //int xInd = ((x+xOffset) >> 4) & (SIDE-1); // equal but faster than: floor(x+xoffset) / 16
                //int indexScaledUp = xInd + (yInd << 6);
                //pixels[x + y * width] = tiles[indexScaledUp]; // Dark gray
                int index = ((x+xOffset )&15) + ((y+yOffset)&15)  * Sprite.grass.SIZE;
                pixels[x + y * width] = Sprite.grass.pixels[index]; // Dark gray
            }
        }
    }

    public void renderTileToBuffer (int xp, int yp, Tile tile){
        renderTileToBuffer(xp, yp, tile.sprite);
    }

    public void renderTileToBuffer (int xp, int yp, Sprite sprite){
        xp -= xOffset;
        yp -= yOffset;
        for ( int y = 0; y < sprite.SIZE; y++){
            int yAbs = y + yp;
            for ( int x = 0; x < sprite.SIZE; x++){
                int xAbs = x + xp;
                if (xAbs >= -16 && xAbs < 0)
                    xAbs = 0;
                if (xAbs < 0 || xAbs >= width || yAbs < 0 || yAbs >= height)
                    break;

                int colour = sprite.pixels[x + y * sprite.SIZE];
                if (colour != 0xffff00ff)
                    pixels[xAbs + yAbs * width] = sprite.pixels[x + y * sprite.SIZE];
            }
        }
    }

    public void renderParticleToBuffer (int xp, int yp, Particle particle, boolean fixed){
        if (fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for ( int y = 0; y < particle.getHeight(); y++){
            int yAbs = y + yp;
            for ( int x = 0; x < particle.getWidth(); x++){
                int xAbs = x + xp;
                if (xAbs >= -16 && xAbs < 0)
                    xAbs = 0;
                if (xAbs < 0 || xAbs >= width || yAbs < 0 || yAbs >= height)
                    break;

                int colour = particle.pixels[x + y * particle.getWidth()];
                if (colour != 0xffff00ff)
                    pixels[xAbs + yAbs * width] = particle.pixels[x + y * particle.getWidth()];
            }
        }
    }

    public void renderPlayerToBuffer (int xp, int yp, Sprite sprite){
        xp -= xOffset;
        yp -= yOffset;
        for ( int y = 0; y < sprite.SIZE; y++){
            int yAbs = y + yp;
            for ( int x = 0; x < sprite.SIZE; x++){
                int xAbs = x + xp;
                if (xAbs >= -16 && xAbs < 0)
                    xAbs = 0;
                if (xAbs < -sprite.SIZE || xAbs >= width || yAbs < 0 || yAbs >= height)
                    break;
                int colour = sprite.pixels[x + y * sprite.SIZE];
                if (colour != 0xffff00ff)
                    pixels[xAbs + yAbs * width] = colour;
            }
        }
    }

    public final int getWidth(){
        return width;
    }

    public final int getHeight(){
        return height;
    }

    public void setOffsets(int x, int y) {
        xOffset = x;
        yOffset = y;
    }
}