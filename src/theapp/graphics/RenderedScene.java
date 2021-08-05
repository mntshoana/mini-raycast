package theapp.graphics;

import theapp.core.App;
import theapp.input.Controller;

// Roof Floor Object
public class RenderedScene extends RenderedObject {

    public RenderedScene() {
        super(App.width, App.height);
        load(() -> init());
    }

    private void init(){
        double floorDistance = 15;
        double ceilingDistance = 20;
        double forward = Controller.z ;
        double rightward = Controller.x;
        double upward = Controller.y;
        double rotation = Controller.rotation;
        double cos = Math.cos(rotation);
        double sine = Math.sin(rotation);

        double walkingBob = Renderer.getWalkingBob();
        for (int y = 0; y < height; y++ ){
            double ceiling = ( y - height / 2.0)  / height;
            // ceiling ranges from [-1/2 to 1/2) not including 1/2

            double z; // 1 / ceiling, for ceiling >= 0 ranges from [2 to inf) positive 2, growing to height
            //  and when ceiling is 0, z is infinity
            if (ceiling > 0)
                z = (floorDistance + upward +walkingBob) /  ceiling; // scaled
            else
                z = (ceilingDistance - upward -walkingBob) / -ceiling; // scaled

            for (int x = 0; x < width; x++){
                double depth = (x - width / 2.0) / height;
                // depth ranges from [-0.888888 to 0.8875]
                depth = depth*z; //  helps to think of this as a percentage of z

                int xx = (int) ((depth * cos +    z  * sine) + rightward) ;
                int yy = (int) ((z * cos     - depth * sine) + forward) ;

                int pixel;
                if (y <= height/2)
                    //pixel = ((xx * 16) & 0xB << 8 | (xx * 16) << 16 ) | (yy *16 << 16);
                    // Apply texture instead
                    pixel = Texture.ceiling.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];
                else
                    pixel = Texture.floor.displayMemory[(xx & 7) + (yy & 7) * 8 /*image has width of 8*/];

                displayMemory[x + width * y] = RenderedObject.fade(pixel, z);
            }
        }
    }
}
