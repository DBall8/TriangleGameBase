package Physics;

/**
 * Class for tracking collisions
 */
public class Collision {

    private static final float T_EPSILON = 0.005f; // A tiny amount of time to undershoot by to ensure nothing passes
                                                    // Through a wall

    public float t; // the time until the collision
    public boolean xcollide; // true if a horizontal collision was detected
    public boolean ycollide; // true if a vertical collision was detected

    // Constructor
    public Collision(){
        reset();
    }

    /**
     * Resets all values to the starting values (no collision)
     */
    public void reset(){
        t = Float.MAX_VALUE;
        xcollide = false;
        ycollide = false;
    }

    /**
     *  copy over another collision's fields to this collision
     * @param c2
     */
    public void copy(Collision c2){
        this.t = c2.t;
        this.xcollide = c2.xcollide;
        this.ycollide = c2.ycollide;
    }

    /**
     * Get the new x position at this collision
     * @param xpos the x position before the collision
     * @param xvol the x velocity before the collision
     * @return the resulting x position at the time of the collision
     */
    public float getNewX(float xpos, float xvol){
        if(t > T_EPSILON){
            float tround = (t - T_EPSILON);
            return (float) (xpos + xvol*tround);
        }
        else{
            return xpos;
        }
    }

    /**
     * Get the new y position at this collision
     * @param ypos the y position before the collision
     * @param yvol the y velocity before the collision
     * @return the resulting y position at the time of the collision
     */
    public float getNewY(float ypos, float yvol){
        if(t > T_EPSILON){
            float tround = (t - T_EPSILON);
            return (float) (ypos + yvol*tround);
        }
        else{
            return ypos;
        }
    }
}
