package theapp.graphics;

import theapp.core.App;
import theapp.input.Controller;

import java.util.Arrays;

public class RenderedWall extends RenderedObject{
    private double xLeft;
    private double xRight;
    private double zLeft;
    private double zRight;
    private double elevatedHeight;
    private double yHeight;

    public RenderedWall(double left, double right, double zLeft, double zRight, double elevHeight, double height) {
        super(App.width, App.height);
        xLeft = left;
        xRight = right;
        this.zLeft = zLeft;
        this.zRight = zRight;
        elevatedHeight = elevHeight;
        yHeight = height;
        this.loader = () -> {
                Arrays.fill(displayMemory, 0);
                init();
            };
    }

    public RenderedWall(int[] existingArr){
        super(existingArr); // must not clear this as it belongs to a different class
        xLeft = xRight = zLeft = zRight = elevatedHeight = yHeight = 0;
        this.loader = () -> init();
    }

    private void init(){
        double walkingBob = Renderer.getWalkingBob();
        final double cos = Math.cos(Controller.rotation);
        final double sin = Math.sin(Controller.rotation);

        double correction = 2.0;
        double newXLeft = (xLeft/2.0 - Controller.x * correction) * 2.0;
        double newZDistance4L = (zLeft/2.0 - Controller.z  * correction) *2.0;
        double rotationL = newXLeft * cos - newZDistance4L * sin;
        double rotationLZ = newZDistance4L * cos  + newXLeft * sin;

        double newXRight = (xRight/2.0  - Controller.x * correction) * 2.0;
        double newZDistance4R = (zRight/2.0 - Controller.z * correction) *2.0;
        double rotationR = newXRight * cos  - newZDistance4R * sin;
        double rotationRZ = newZDistance4R * cos  + newXRight * sin;

        // Cohen–Sutherland algorithm
        final double clipWindow = 0.5;
        double txt2Numerator = 0;
        double txt3Numerator = 8;

        if (rotationLZ < clipWindow && rotationRZ < clipWindow)
            return;
        if (rotationLZ < clipWindow){
            double commonFactor = (clipWindow - rotationLZ) / (rotationRZ - rotationLZ);
            rotationLZ = rotationLZ +  ( rotationRZ - rotationLZ) * commonFactor;
            rotationL = rotationL +  ( rotationR - rotationL) * commonFactor;
            txt2Numerator = txt2Numerator + (txt3Numerator - txt2Numerator) * commonFactor;
        }
        if (rotationRZ < clipWindow){
            double commonFactor = (clipWindow - rotationLZ) / (rotationRZ - rotationLZ);
            rotationRZ = rotationLZ +  ( rotationRZ - rotationLZ) * commonFactor;
            rotationR = rotationL +  ( rotationR - rotationL) * commonFactor;
            txt3Numerator = txt2Numerator + (txt3Numerator - txt2Numerator) * commonFactor;
        }

        double xPixelLeft = (rotationL / rotationLZ * height + width / 2.0);
        double xPixelRight = (rotationR / rotationRZ * height + width / 2.0);

        if (xPixelLeft >= xPixelRight) // do not render negatives
            return;

        int xPixelLeftInt = (int) xPixelLeft;
        int xPixelRightInt = (int) xPixelRight;
        if (xPixelLeftInt < 0 )
            xPixelLeftInt = 0;
        if (xPixelRightInt > width)
            xPixelRightInt = width;
        double yTL = ((-elevatedHeight) - (-Controller.y - walkingBob) * correction) * 2;
        double yTR = ((-elevatedHeight) - (-Controller.y - walkingBob) * correction) * 2;
        double yBL = ((yHeight - elevatedHeight) - (-Controller.y - walkingBob) * correction)* 2;
        double yBR = ((yHeight - elevatedHeight) - (-Controller.y - walkingBob) * correction)* 2 ;

        double yPixelTopL =    (yTL +15 * correction) / rotationLZ * height + height /2.0;
        double yPixelBottomL = (yBL +15* correction) / rotationLZ * height + height /2.0;
        double yPixelTopR =    (yTR +15* correction) / rotationRZ * height + height /2.0;
        double yPixelBottomR = (yBR +15* correction) / rotationRZ * height + height /2.0;

        // conform to 8 by 8 textures
        double txt0 = 1 / rotationLZ;
        double txt1 = 1 / rotationRZ;
        double txt2 = txt2Numerator / rotationLZ;
        double txt3 = txt3Numerator / rotationRZ - txt2;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++){
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
            double yPixelTop =    yPixelTopL + (yPixelTopR - yPixelTopL) * pixelRotation ;
            double yPixelBottom = yPixelBottomL + (yPixelBottomR - yPixelBottomL) * pixelRotation;

            int yPixelTopInt = (int) yPixelTop;
            int yPixelBottomInt = (int) yPixelBottom;

            int xTexture = (int) ((txt2 + txt3 * pixelRotation) / (txt0 + (txt1 - txt0) * pixelRotation));
            if (yPixelTopInt < 0)
                yPixelTopInt = 0;
            if (yPixelBottomInt > height)
                yPixelBottomInt = height;

            for (int y = yPixelTopInt ; y < yPixelBottomInt; y++){
                int yTexture = (int)(8 * (y - yPixelTop) / (yPixelBottom - yPixelTop));
                //int texture = (xTexture * 100 + yTexture * 100 ) * 256 ;
                int pixel = Texture.wall.displayMemory[(xTexture & 7) + (yTexture & 7) * 8] ;
                // int z = (int) (1 / (txt0 + (txt1 - txt0) * pixelRotation * 8));
                displayMemory[x+y*width] = RenderedObject.fade(pixel,  1 / (txt0 + ( txt1 - txt0) * pixelRotation)  / 4  ); // color wall
            }
        }
    }
    public void reconf(double left, double right, double zLeft, double zRight, double elevHeight, double height){
        xLeft = left;
        xRight = right;
        this.zLeft = zLeft;
        this.zRight = zRight;
        elevatedHeight = elevHeight;
        yHeight = height;
        loader.run();
    }
}
