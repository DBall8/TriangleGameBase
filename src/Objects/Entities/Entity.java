package Objects.Entities;

import Physics.Physics;
import Global.Settings;
import Objects.ICollidable;
import Physics.Collision;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.List;


public abstract class Entity {
    protected float xpos, ypos;
    protected float angle;
    protected float xvel, yvel;
    protected float width, height;

    protected Rectangle boundingBox;

    protected Collision tempCollision = new Collision();
    protected Collision earliestCollision = new Collision();

    public Entity(int x, int y){
        this.xpos = x;
        this.ypos = y;
        reset();
    }

    public abstract void draw();

    public void update(){}

    public void move(float time){
        // if collision in time step, update accordingly
        if(earliestCollision.t <= time){
            xpos = earliestCollision.getNewX(xpos, xvel);
            ypos = earliestCollision.getNewY(ypos, yvel);
            if(earliestCollision.xcollide){
                if(Settings.BOUNCE){
                    xvel *= -1;
                }else{
                    xvel = 0;
                }
            }
            if(earliestCollision.ycollide){
                if(Settings.BOUNCE){
                    yvel *= -1;
                }else{
                    yvel = 0;
                }
            }

        }
        // otherwise update location
        else{
            xpos += xvel*time*(60.0/ Settings.getFramerate());
            ypos += yvel*time*(60.0/ Settings.getFramerate());
        }
    }

    public float checkCollisions(float time, List<ICollidable> obstacles){
        if(boundingBox == null){
            return time;
        }
        // Check if the player will collide with the boundaries of the play field
        Physics.checkBoxCollision(this, 0, 0, Settings.getWindowWidth(), Settings.getWindowHeight(), time, tempCollision);
        if(tempCollision.t < earliestCollision.t){
            earliestCollision.copy(tempCollision);
        }
        for(ICollidable o: obstacles){
            if(this.equals(o)){
                continue;
            }
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                earliestCollision.copy(tempCollision);
            }
        }

        return earliestCollision.t < time? earliestCollision.t: time;
    }

    public void reset(){
        earliestCollision.reset();
        tempCollision.reset();
    }

    public abstract Node getVisuals();

    public float getX(){ return xpos; }
    public float getY(){ return ypos; }
    public float getAngle(){ return angle; }
    public float getXVel(){ return xvel; }
    public float getYVel(){ return yvel; }
    public float getXRadius(){ return width/2; }
    public float getYRadius(){ return height/2; }
    public Rectangle getBoundingBox(){ return boundingBox; }
}
