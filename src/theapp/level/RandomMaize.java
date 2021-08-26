//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package theapp.level;

import java.util.Random;

import theapp.core.App;
import theapp.graphics.RenderedWall;
import theapp.graphics.RenderedSprite;
import theapp.graphics.Renderer;
import theapp.input.Controller;

public class RandomMaize {
    private double zBufferWall[];
    private double zBufferSprite[];
    public static Byte[] solidBlocks;
    private static int width;
    private static int height;
    private RenderedWall wall;
    private RenderedSprite sprite;

    public RandomMaize(int width, int height, Renderer renderer) {
        this.width = width;
        this.height = height;
        solidBlocks = new Byte[width * height];
        this.zBufferWall = new double[App.width];
        this.wall = new RenderedWall(renderer.getBuffer()); // use actual Renderer buffer (avoid copying of render.draw)
        wall.reconfZBuffer(zBufferWall);
        this.zBufferSprite = new double[App.width*App.height];
        this.sprite = new RenderedSprite(renderer.getBuffer()); // use Render buffer here too
        sprite.reconfZBuffer(zBufferSprite);

        Random random = new Random();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (random.nextInt(7) == 0) {
                    this.solidBlocks[x + y * width] = 1; // 1 is a solid wall
                } else {
                    //if (random.nextInt(7) == 0)
                        this.solidBlocks[x + y * width] = 2; // 2 is a sprite
                    //else
                      //  this.solidBlocks[x + y * width] = 0; // empty
                }
            }
        }
        double spawnX = 0.201;
        double spawnZ = 0.201;
        while (!isCollision(spawnX, spawnZ)) {
            spawnX += 0.201;
            spawnZ += 0.201;
        }
        Controller.x = spawnX;
        Controller.z = spawnZ;
    }

    private boolean checkBlock(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height ? this.solidBlocks[x + y * this.width] ==1 : true;
    }
    private boolean isSprite(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height ? this.solidBlocks[x + y * this.width] ==2 : false;
    }
    public static boolean isCollision(double x, double z){
        // purpose: Detects if the player's movements have collided with a wall
        //          (static) It is meant to be called from within the Controller class
        // param: x and z are the position of the player
        //          The param values are what the Controller class intends to update towards internally.
        //
        double xScale = 10;
        double zScale = 10;
        int xInt = (int) (x / xScale);
        int zInt = (int) (z / zScale);
        if (xInt < 0 || zInt < 0 || xInt >= width || zInt >= height) // define out of bounds as a collision
            return false;
        boolean isSolid = solidBlocks[xInt + zInt * width] == 1;
        return x > 0.0 && z > 0.0 && x < width * xScale && z < height * zScale && !isSolid;
    }

    private void extendWall(int xLeft, int xRight, int zLeft, int zRight){
        // walls are scaled up by 40
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 5 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 25 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 45 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 65 , 20);
        // no need to call draw method of renderer
    }
    public void draw() {
        for (int i = 0; i < App.width; i++) {
            zBufferWall[i] = -2.0;
            for (int j = 0; j < App.height; j++)
            zBufferSprite[i + j * width] = -400.0;
        }
        for(int x = -1; x < width + 1; ++x) {
            for(int z = height; z >= -1; --z) {
                if (this.checkBlock(x, z)) {
                    if (!this.checkBlock(x + 1, z)) // east
                        extendWall(x + 1, x + 1, z, z+1);
                    if (!this.checkBlock(x, z + 1)) // south
                        extendWall(x + 1, x, z+1, z+1);
                } else {
                    if (this.checkBlock(x, z + 1)) // south
                        extendWall(x, x+1, z+1, z+1);
                    if (this.checkBlock(x + 1, z)) // east
                        extendWall(x+1, x+1, z+1, z);
                }


            }
        }
        for(int x = -1; x < width + 1; ++x) {
            for (int z = height; z >= -1; --z) {
                if (isSprite(x, z)) {
                    sprite.reconf(x * 10, 0, z * 10);
                }
            }
        }

    }
}
