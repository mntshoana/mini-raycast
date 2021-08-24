//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package theapp.level;

import java.util.Random;

import theapp.core.App;
import theapp.graphics.RenderedWall;
import theapp.graphics.Renderer;

public class RandomMaize {
    private double zBuffer[];
    private boolean[] solidBlocks;
    private final int width;
    private final int height;
    private RenderedWall wall;

    public RandomMaize(int width, int height, Renderer renderer) {
        this.width = width;
        this.height = height;
        this.solidBlocks = new boolean[width * height];
        this.zBuffer = new double[App.width];
        this.wall = new RenderedWall(renderer.getBuffer()); // use actual Renderer buffer (avoid copying of render.draw)
        wall.reconfZBuffer(zBuffer);
        Random random = new Random();

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if (random.nextInt(7) == 0) {
                    this.solidBlocks[x + y * width] = true;
                } else {
                    this.solidBlocks[x + y * width] = false;
                }
            }
        }
    }

    private boolean checkBlock(int x, int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height ? this.solidBlocks[x + y * this.width] : true;
    }

    private void extendWall(int xLeft, int xRight, int zLeft, int zRight){

        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 5 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 25 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 45 , 20);
        wall.reconf( 40* xLeft, 40 * xRight, 40 * zLeft, 40 * zRight, 65 , 20);
        // no need to call draw method of renderer
    }
    public void draw() {
        for (int i = 0; i < App.width; i++)
            zBuffer[i] = -2.0;
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

    }
}
