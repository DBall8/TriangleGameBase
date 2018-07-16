package Physics;

public class Collision {

    private static final float T_EPSILON = 0.005f;

    public float t;
    public boolean xcollide;
    public boolean ycollide;

    public Collision(){
        reset();
    }

    public void reset(){
        t = Float.MAX_VALUE;
        xcollide = false;
        ycollide = false;
    }

    // copy over another collision to this collision
    public void copy(Collision c2){
        this.t = c2.t;
        this.xcollide = c2.xcollide;
        this.ycollide = c2.ycollide;
    }

    // get the new x position
    public float getNewX(float xpos, float xvol){
        if(t > T_EPSILON){
            float tround = (t - T_EPSILON);
            return (float) (xpos + xvol*tround);
        }
        else{
            return xpos;
        }
    }

    // get the new y position
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
