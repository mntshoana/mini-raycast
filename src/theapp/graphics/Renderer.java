package theapp.graphics;

import java.util.Arrays;
import theapp.core.App;
import theapp.input.Controller;
import theapp.input.InputHandler;

public class Renderer {

    // just make 2 throw away test object
    private RenderedObject roofFloor;
    private RenderedObject walls;

    private InputHandler input;
    private App parent;
    // a counter to help with color changes over time in a controlled way
    private long ticks;

    private double walkingBob;
    // Constructor
    public Renderer(App parent){
        input = new InputHandler();
        this.parent = parent;
        parent.addKeyListener(input);
        parent.addFocusListener(input);
        parent.addMouseListener(input);
        parent.addMouseMotionListener(input);

        walkingBob = getWalkingBob();
        roofFloor = new RenderedObject(App.width, App.height);
        roofFloor.load(()->{
            RenderedObject object = roofFloor; // like the 'this' pointer
            double floorDistance = 15;
            double ceilingDistance = 20;
            double forward = Controller.z ;
            double rightward = Controller.x;
            double upward = Controller.y;
            double rotation = Controller.rotation;
            double cos = Math.cos(rotation);
            double sine = Math.sin(rotation);

            for (int y = 0; y < object.height; y++ ){
                double ceiling = ( y - object.height / 2.0)  / object.height;
                // ceiling ranges from [-1/2 to 1/2) not including 1/2

                double z; // 1 / ceiling, for ceiling >= 0 ranges from [2 to inf) positive 2, growing to height
                                                            //  and when ceiling is 0, z is infinity
                if (ceiling > 0)
                    z = (floorDistance + upward +walkingBob) /  ceiling; // scaled
                else
                    z = (ceilingDistance - upward -walkingBob) / -ceiling; // scaled

                for (int x = 0; x < object.width; x++){
                    double depth = (x - object.width / 2.0) / object.height;
                    // depth ranges from [-0.888888 to 0.8875]
                    depth = depth*z; //  helps to think of this as a percentage of z

                    int xx = (int) ((depth * cos +    z  * sine) + rightward) ;
                    int yy = (int) ((z * cos     - depth * sine) + forward) ;

                    int pixel;
                    if (y <= object.height/2)
                    //pixel = ((xx * 16) & 0xB << 8 | (xx * 16) << 16 ) | (yy *16 << 16);
                    // Apply texture instead
                        pixel = Texture.ceiling.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];
                    else
                        pixel = Texture.floor.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];

                    object.displayMemory[x + object.width * y] = RenderedObject.fade(pixel, z);
                }
            }
        });

        walls = new RenderedObject(App.width, App.height);
        walls.load(()->{
            Arrays.fill(walls.displayMemory, 0);
            final double cos = Math.cos(Controller.rotation);
            final double sin = Math.sin(Controller.rotation);

            double xLeft = 0;
            double xRight = 30;
            double zDistance = 100;
            double yHeight = -2;

            double newXLeft = (xLeft/2.0 - Controller.x * 2.0625) * 2.0;
            double newZDistance4L = (zDistance/2.0 - Controller.z  * 2.0625 ) *2.0;
            double rotationL = newXLeft * cos - newZDistance4L * sin;
            double rotationLZ = newZDistance4L * cos  + newXLeft * sin;

            double newXRight = (xRight/2.0  - Controller.x * 2.0625) * 2.0;
            double newZDistance4R = (zDistance/2.0 - Controller.z * 2.0625 ) *2.0;
            double rotationR = newXRight * cos  - newZDistance4R * sin;
            double rotationRZ = newZDistance4R * cos  + newXRight * sin;

            double xPixelLeft = (rotationL / rotationLZ * walls.height + walls.width / 2.0);
            double xPixelRight = (rotationR / rotationRZ * walls.height + walls.width / 2.0);

            if (xPixelLeft >= xPixelRight) // do not render negatives
                return;

            int xPixelLeftInt = (int) xPixelLeft;
            int xPixelRightInt = (int) xPixelRight;
            if (xPixelLeftInt < 0 )
                xPixelLeftInt = 0;
            if (xPixelRightInt > App.width)
                xPixelRightInt = App.width;
            double yTL = ((-yHeight) - (-Controller.y - walkingBob)*2.0625 ) * 2 ;
            double yTR = ((-yHeight) - (-Controller.y - walkingBob)*2.0625 ) * 2 ;
            double yBL = ((30 - yHeight) - (-Controller.y - walkingBob)*2.0625 )* 2 ;
            double yBR = ((30 - yHeight) - (-Controller.y - walkingBob)*2.0625 )* 2 ;

            double yPixelTopL =    yTL / rotationLZ * App.height + App.height /2.0;
            double yPixelBottomL = yBL / rotationLZ * App.height + App.height /2.0;
            double yPixelTopR =    yTR / rotationRZ * App.height + App.height /2.0;
            double yPixelBottomR = yBR / rotationRZ * App.height + App.height /2.0;

            // conform to 8 by 8 textures
            double txt0 = 1 / rotationLZ;
            double txt1 = 1 / rotationRZ;
            double txt2 = 1 / rotationLZ;
            double txt3 = 8 / rotationRZ - txt2;

            for (int x = xPixelLeftInt; x < xPixelRightInt; x++){
                double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
                double yPixelTop =    yPixelTopL + (yPixelTopR - yPixelTopL) * pixelRotation ;
                double yPixelBottom = yPixelBottomL + (yPixelBottomR - yPixelBottomL) * pixelRotation;

                int yPixelTopInt = (int) yPixelTop;
                int yPixelBottomInt = (int) yPixelBottom;

                int xTexture = (int) ((txt2 + txt3 * pixelRotation) / (txt0 + (txt1 - txt0) * pixelRotation));
                if (yPixelTopInt < 0)
                    yPixelTopInt = 0;
                if (yPixelBottomInt > App.height)
                    yPixelBottomInt = App.height;

                for (int y = yPixelTopInt ; y < yPixelBottomInt; y++){
                    int yTexture = (int)(8 * (y - yPixelTop) / (yPixelBottom - yPixelTop));
                    int texture = (xTexture * 100 + yTexture * 100 ) * 256 ;
                   // int z = (int) (1 / (txt0 + (txt1 - txt0) * pixelRotation * 8));
                    walls.displayMemory[x+y*walls.width] = RenderedObject.fade(texture,  1 / (txt0 + (txt1 - txt0) * pixelRotation)  / 4  ); // color wall
                }
            }
        });
    }

    public double getWalkingBob(){
        double walkingBob = Math.sin(ticks / Math.PI);
        if (!Controller.moved)
            walkingBob = 0.0;
        else if (Controller.crouched && Controller.moved)
            walkingBob = Math.sin(ticks / Math.PI /2) *  0.3;
        else if (Controller.moved && Controller.sprint)
            walkingBob *= Math.sin(ticks / Math.PI * 1.5) *  0.7;
        else if (Controller.moved)
            walkingBob *= 0.5;
        return walkingBob;
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
                    parent.getBuffer()[xPixel + App.width * (yPixel)]
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
            parent.getBuffer()[i] = 0;

        // Playing with background like image drawn from code
        draw(roofFloor, 0, 0);
        System.out.println("X: " + input.MouseX + ", Y: " + input.MouseY );
        draw(walls, 0, 0);
    }
    private void tick(){
        walkingBob = getWalkingBob();
        input.captureCurrentMousePos();
        Controller.onKey(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        roofFloor.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        walls.reload(input.keyPresses, input.MouseXDiff, input.MouseYDiff);
        ticks++;
    }
}
