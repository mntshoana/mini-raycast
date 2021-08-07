//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package theapp.level;

import java.util.Random;
import theapp.graphics.RenderedWall;
import theapp.graphics.Renderer;

public class RandomMaize {
    private boolean[] solidBlocks;
    private final int width;
    private final int height;
    private RenderedWall wall;
    private Renderer renderer;

    public RandomMaize(int width, int height, Renderer renderer) {
        this.width = width;
        this.height = height;
        this.renderer = renderer;
        this.solidBlocks = new boolean[width * height];
        this.wall = new RenderedWall(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        Random random = new Random();

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if (random.nextInt(4) == 0) {
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

    public void draw() {
        int size = 5;

        for(int x = -size; x < size; ++x) {
            for(int z = -size; z < size; ++z) {
                int w = 30;
                if (w < this.width) {
                    w = this.width;
                }

                int xLeft;
                int xRight;
                int zLeft;
                int zRight;
                if (this.checkBlock(x, z)) {
                    if (!this.checkBlock(x + 1, z)) {
                        xLeft = xRight = (x + 1) * w;
                        zLeft = z * w;
                        zRight = (z + 1) * w;
                        this.wall.reconf((double)xLeft, (double)xRight, (double)zLeft, (double)zRight, 0.0D, 15.0D);
                        this.renderer.draw(this.wall, 0, 0);
                    }

                    if (!this.checkBlock(x, z + 1)) {
                        xLeft = (x + 1) * w;
                        xRight = x * w;
                        zLeft = zRight = (z + 1) * w;
                        this.wall.reconf((double)xLeft, (double)xRight, (double)zLeft, (double)zRight, 0.0D, 15.0D);
                        this.renderer.draw(this.wall, 0, 0);
                    }
                } else {
                    if (this.checkBlock(x + 1, z)) {
                        xLeft = xRight = (x + 1) * w;
                        zLeft = (z + 1) * w;
                        zRight = z + 1 * w;
                        this.wall.reconf((double)xLeft, (double)xRight, (double)zLeft, (double)zRight, 0.0D, 15.0D);
                        this.renderer.draw(this.wall, 0, 0);
                    }

                    if (this.checkBlock(x, z + 1)) {
                        xLeft = x * w;
                        xRight = (x + 1) * w;
                        zLeft = zRight = (z + 1) * w;
                        this.wall.reconf((double)xLeft, (double)xRight, (double)zLeft, (double)zRight, 0.0D, 15.0D);
                        this.renderer.draw(this.wall, 0, 0);
                    }
                }
            }
        }

    }
}
