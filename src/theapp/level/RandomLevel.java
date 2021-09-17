package theapp.level;

import java.util.Random;

public class RandomLevel extends Level {
    private Random random;
    public RandomLevel(int width, int height) {
        super(width, height);
        random = new Random();
    }

    @Override
    protected void generateLevel() {
        if (random == null)
            random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x + y * width] = random.nextInt(4);
            }
        }
    }
}
