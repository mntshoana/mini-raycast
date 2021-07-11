package theapp.graphics;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import theapp.core.App;
import theapp.input.InputHandler;

public class DisplayBuffer extends RenderedObject{

    // just make 2 throw away test object
    private RenderedObject testObject;
    private RenderedObject roofFloor;
    private RenderedObject walls;

    private InputHandler input;
    private App parent;
    // a counter to help with color changes over time in a controlled way
    private long ticks;

    // Constructor
    public DisplayBuffer(int width, int height, App parent){
        super(width, height);

        input = new InputHandler();
        this.parent = parent;
        parent.addKeyListener(input);
        parent.addFocusListener(input);
        parent.addMouseListener(input);
        parent.addMouseMotionListener(input);

        roofFloor = new RenderedObject(App.width, App.height);
        roofFloor.load(()->{
            RenderedObject object = roofFloor;
            double floorDistance = 15;
            double ceilingDistance = 20;
            double forward = object.controller.z ;
            double rightward = object.controller.x;
            double upward = object.controller.y;
            double rotation = object.controller.rotation;
            double cos = Math.cos(rotation);
            double sine = Math.sin(rotation);

            for (int y = 0; y < object.height; y++ ){
                                           /*adjacent*/       /*opposite*/
                double ceiling = ( y - object.height / 2.0)  / object.height;
                // ceiling ranges from [-1/2 to 1/2) not including 1/2

                double z; // positive, growing to infinity
                double walkingBob = Math.sin(ticks / Math.PI);
                if (!object.controller.moved)
                    walkingBob = 0.0;
                else if (object.controller.crouched && object.controller.moved)
                    walkingBob = Math.sin(ticks / Math.PI /2) *  0.3;
                else if (object.controller.moved && object.controller.sprint)
                    walkingBob *= Math.sin(ticks / Math.PI * 1.5) *  0.7;
                else if (object.controller.moved)
                    walkingBob *= 0.5;
                if (ceiling > 0)
                    z = (floorDistance + upward +walkingBob) /  ceiling; // invert cotan = tan
                else
                    z = (ceilingDistance - upward -walkingBob) / -ceiling;

                for (int x = 0; x < object.width; x++){
                                            /*adjacent*/       /*hypotenuse*/
                    double depth = (x - object.width / 2.0) / object.height;
                    // depth ranges from [-0.888888 to 0.8875]
                    depth = depth*z;

                    int xx = (int) ((depth * cos + z * sine)  + rightward) ;
                    int yy = (int) ((z * cos - depth * sine) + forward) ;

                    int pixel;
                    if (y <= object.height/2)
                    //pixel = ((xx * 16) & 0xB << 8 | (xx * 16) << 16 ) | (yy *16 << 16);
                    // Apply texture instead
                        pixel = Texture.ceiling.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];
                    else
                    pixel = Texture.floor.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];

                    object.displayMemory[x + object.width * y] = fade(pixel, z);
                    //testOImageFromCode.displayMemory[x+ object.width*(y)] = pixel;
                }
            }
        });

        walls = new RenderedObject(App.width, App.height);
        walls.load(()->{
            Arrays.fill(walls.displayMemory, 0);
            final double cos = Math.cos(walls.controller.rotation);
            final double sin = Math.sin(walls.controller.rotation);

            double xLeft = 0;
            double xRight = 10;
            double zDistance = 2;
            double yHeight = 0;

            double newXLeft = (xLeft - walls.controller.x) * 2;
            double newZDistance4L = (zDistance - walls.controller.z) *2;
            double rotationL = newXLeft * cos
                             - newZDistance4L * sin;

            double yTL = ((-yHeight) + walls.controller.y) * 2;
            double yBL = ((0.5 - yHeight) + walls.controller.y)* 2;
            double rotationLZ = newZDistance4L * cos
                            + newXLeft * sin;

            double newXRight = (xRight  - walls.controller.x) * 2;
            double newZDistance4R = (zDistance - walls.controller.z) *2;
            double rotationR = newXRight * cos
                    - newZDistance4R * sin;

            double yTR = ((-yHeight) + walls.controller.y) * 2;
            double yBR = ((0.5 - yHeight) + walls.controller.y)* 2;
            double rotationRZ = newZDistance4R * cos
                                + newXRight * sin;

            double xPixelLeft = (rotationL / rotationLZ * walls.height + walls.width / 2);
            double xPixelRight = (rotationR / rotationRZ * walls.height + walls.width / 2);

            if (xPixelLeft >= xPixelRight) // do not render negatives
                return;

            int xPixelLeftInt = (int) xPixelLeft;
            int xPixelRightInt = (int) xPixelRight;
            if (xPixelLeftInt < 0 )
                xPixelLeftInt = 0;
            if (xPixelRightInt > App.width)
                xPixelRightInt = App.width;

            int yPixelTopLeftInt =  (int) (yTL / rotationLZ * App.height + App.height /2);
            int yPixelBottomLtInt = (int) (yBL / rotationLZ * App.height + App.height /2);
            int yPixelTopRightInt = (int) (yTR / rotationRZ * App.height + App.height /2);
            int yPixelBottomRInt =  (int) (yBR / rotationRZ * App.height + App.height /2);

            for (int x = xPixelLeftInt; x < xPixelRightInt; x++){
                double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
                double yPixelTop =    yPixelTopLeftInt + (yPixelTopRightInt - yPixelTopLeftInt) * pixelRotation ;
                double yPixelBottom = yPixelBottomLtInt + (yPixelBottomRInt - yPixelBottomLtInt) * pixelRotation;

                int yPixelTopInt = (int) yPixelTop;
                int yPixelBottomInt = (int) yPixelBottom;

                if (yPixelTopInt < 0)
                    yPixelTopInt = 0;
                if (yPixelBottomInt > App.height)
                    yPixelBottomInt = App.height;

                for (int y = yPixelTopInt; y < yPixelBottomInt; y++){
                    walls.displayMemory[x+y*walls.width] = fade(0x232499, zDistance - walls.controller.z); // color wall
                }
            }
        });
    }

    public void draw(RenderedObject object, int xOffset, int yOffset){
        for (int y = 0; y < object.height; y++){
            int yPixel = y + yOffset;
            if (yPixel < 0 || yPixel >= App.height)
                continue;;
            for (int x = 0; x < object.width; x++){
                int xPixel = x + xOffset;
                if (xPixel < 0 || xPixel >= App.width)
                    continue;

                int alpha = object.displayMemory[x + object.width*(y)]; // allow empty pixels to not be written which allows transparency
                if (alpha > 0) {
                    displayMemory[xPixel + this.width * (yPixel)]
                            = object.displayMemory[x + object.width * (y)];
                }
            }
        }
    }

    // Renders everything to the screen
    public void update(){
        tick();
        // Clear buffer
        for (int i = 0, len = App.width * App.height; i < len; i++)
            displayMemory[i] = 0;

        // Playing with background like image drawn from code
        draw(roofFloor, 0, 0);
        System.out.println("X: " + input.MouseX + ", Y: " + input.MouseY );
        draw(walls, 0, 0);
    }
    private void tick(){
        input.captureCurrentMousePos();
        roofFloor.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        walls.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        ticks++;
    }
}
