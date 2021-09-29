package theapp.level;

import theapp.level.tile.Tile;

import java.util.Random;

public class RandomLevel extends Level {
    private Random random;

    public RandomLevel(int width, int height) {
        super(width, height);
        random = new Random();
    }

    @Override
    protected void generateLevel() {
        tiles = new int[width * height];
        if (random == null)
            random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x + y * width] = random.nextInt(200);
            }
        }
    }

    @Override
    public Tile getTile (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.colourBlue;

        int tile = tiles[x + y * width];
        if (tile >= 0 && tile <= 196 )
            return Tile.grass;
        if (tiles[x + y * width] == 197)
            return Tile.rock;
        if (tiles[x + y * width] == 198)
            return Tile.flower;
        if (tiles[x + y * width] == 199)
            return Tile.tree;

        return Tile.colourBlue;
    }
}
