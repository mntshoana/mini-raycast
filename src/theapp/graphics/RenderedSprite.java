package theapp.graphics;

import theapp.core.App;
import theapp.input.Controller;
import theapp.level.RandomMaize;

import java.util.Arrays;
import java.util.Random;

public class RenderedSprite extends RenderedObject {
    private double x;
    private double y;
    private double z;
    private double zBuffer[];

    public RenderedSprite(double x, double y, double z) {
        super(App.width, App.height);
        this.x = x;
        this.y = y;
        this.z = z;
        this.loader = () -> {
            Arrays.fill(displayMemory, 0);
            init();
        };
    }

    public RenderedSprite(int[] existingArr) {
        super(existingArr); // must not clear this as it belongs to a different class
        x = y = z = 0;
        this.loader = () -> init();
    }

    private void init() {
        double walkingBob = Renderer.getWalkingBob();
        final double cos = Math.cos(Controller.rotation);
        final double sin = Math.sin(Controller.rotation);

        double correction = 0.25 ;
        double xc = ((x / 2.0) - Controller.x * correction) * 2.0;
        double zc = ((z / 2.0) - Controller.z * correction) * 2.0;

        double rotationX = xc * cos - zc * sin;
        double rotationY = ((y / 2.0) - (Controller.y - walkingBob) * -correction) * 2.0;
        double rotationZ = zc * cos + xc * sin;

        double xMid = App.width / 2.0;
        double yMid = App.height / 2.0;

        double xPixel = rotationX / rotationZ * height + xMid;
        double yPixel = rotationY / rotationZ * height + yMid;

        double xLeft = xPixel - 64 / rotationZ;
        double xRight = xPixel + 64 / rotationZ;
        double yTop = yPixel - 64 / rotationZ;
        double yBottom = yPixel + 64 / rotationZ;

        int xLeftInt = (int) xLeft;
        int xRightInt = (int) xRight;
        int yTopInt = (int) yTop;
        int yBottomInt = (int) yBottom;

        if (xLeftInt < 0) xLeftInt = 0;
        if (yTopInt < 0) yTopInt = 0;
        if (xRightInt > width) xRightInt = width;
        if (yBottomInt > height) yBottomInt = height;

        rotationZ *= 8;

        for (int x = xLeftInt; x < xRightInt; x++) {
            for (int y = yTopInt; y < yBottomInt; y++) {
                int pixel = 0xCCDD00;
                displayMemory[x + y * width] = RenderedObject.fade(pixel, rotationZ/8); // color wall
                zBuffer[x + y * width] = rotationZ;
            }
        }
    }

    public void reconf(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        loader.run();
    }

    public void reconfZBuffer(double zBuffer[]) {
        this.zBuffer = zBuffer;
    }
}
