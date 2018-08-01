package Objects.Entities;

import Physics.Physics;
import Global.Settings;
import Objects.ICollidable;
import Physics.Collision;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Abstract class for an entity, or anything that moves around and interacts with objects in a game
 */
public abstract class Entity {

    private String ID; // the entity's ID

    protected float xpos, ypos; // screen positions
    protected float angle; // rotation angle
    protected float xvel, yvel; // velocities
    protected float width, height; // dimenions

    protected Rectangle boundingBox; // a box for detecting collisions

    // Objects for tracking collisions
    protected Collision tempCollision = new Collision();
    protected Collision earliestCollision = new Collision();

    public Entity(String id, int x, int y){
        this.ID = id;
        this.xpos = x;
        this.ypos = y;
        reset();
    }

    // For drawing the entity in the game
    public abstract void draw();
    // For the initial update before each frame is calculated
    public void update(){}

    /**
     * Default method for moving the entity
     * @param time the amount of time to move the entity by
     */
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

    /**
     * Checks for the earliest collision in the given time step
     * @param time the time step amount
     * @param obstacles a list of potential objects to collide with
     * @return the time of the earlist collision
     */
    public float checkCollisions(float time, List<ICollidable> obstacles){
        // quit now if the entity does not even have a bounding box
        if(boundingBox == null){
            return time;
        }
        // Check if the enitity will collide with the boundaries of the play field
        Physics.checkBoxCollision(this, 0, 0, Settings.getWindowWidth(), Settings.getWindowHeight(), time, tempCollision);
        if(tempCollision.t < earliestCollision.t){
            earliestCollision.copy(tempCollision);
        }

        // Check if the entity will collide with other collidables
        for(ICollidable o: obstacles){
            // dont check colliding with itself
            if(this.equals(o)){
                continue;
            }
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                earliestCollision.copy(tempCollision);
            }
        }

        // return the earliest collision time, or the time step, whichever is smaller
        return earliestCollision.t < time? earliestCollision.t: time;
    }

    /**
     * Resets the collision trackers
     */
    public void reset(){
        earliestCollision.reset();
        tempCollision.reset();
    }

    /**
     * Updates the state of this entity
     * @param x new x position
     * @param y new y position
     * @param xvel new x velocity
     * @param yvel new y velocity
     * @param angle new angle
     */
    public void updateState(float x, float y, float xvel, float yvel, float angle){
        this.xpos = x;
        this.ypos = y;
        this.xvel = xvel;
        this.yvel = yvel;
        this.angle = angle;
    }

    // Getters and setters

    public abstract Node getVisuals();

    public String getID(){ return ID; }
    public float getX(){ return xpos; }
    public float getY(){ return ypos; }
    public float getAngle(){ return angle; }
    public float getXVel(){ return xvel; }
    public float getYVel(){ return yvel; }
    public float getVelocity(){ return (float)Math.sqrt((xvel*xvel) + (yvel * yvel));}
    public float getXRadius(){ return width/2; }
    public float getYRadius(){ return height/2; }
    public Rectangle getBoundingBox(){ return boundingBox; }
}
