package Objects.Entities;

import Global.Settings;
import Objects.ICollidable;
import Physics.Physics;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * A class for handling projectile entities
 */
public class Projectile extends Entity {

    private final static int DAMAGE = 1;

    private final static int WIDTH = 10;
    private final static int HEIGHT = 30;
    private final static float PVELOCITY = 5; // the base velocity of a projectile
    private final static float MOVEFACTOR = 0.9f; // the percentage of the player's speed to add to the projectile speed

    private String ownerID; // the player that shot this projectile
    private boolean alive; // true when projectile is still traveling through the air

    /**
     * Constructor for brand new projectile, only differs in that it creates its own ID
     * @param p the player who shot the projectile
     */
    public Projectile(Player p){
        super("Proj-" + System.currentTimeMillis(), (int)p.getX(), (int)p.getY());
        float pvel = p.getVelocity() * MOVEFACTOR + PVELOCITY;
        this.angle = p.getAngle();
        this.xvel = Physics.xComponent(pvel, Physics.toRadiians(angle));
        this.yvel = Physics.yComponent(pvel, Physics.toRadiians(angle));

        this.width = WIDTH;
        this.height = HEIGHT;
        this.ownerID = p.getID();
        boundingBox = new Rectangle(width, height);
        boundingBox.setFill(p.getColor());

        alive = true;
    }

    /**
     * Constructor for a projectile that already has an ID
     * @param ID The ID of the projectile
     * @param p the player who fired the projectile
     */
    public Projectile(String ID, String ownerID, float x, float y, float xvel, float yvel, float angle){
        super(ID, (int)x, (int)y);
        this.angle = angle;
        this.xvel = xvel;
        this.yvel = yvel;

        this.width = WIDTH;
        this.height = HEIGHT;
        this.ownerID = ownerID;
        boundingBox = new Rectangle(width, height);
        boundingBox.setFill(Color.RED);

        alive = true;
    }

    /**
     * Overriding collision check to never stop the timer but only see if the projectile hit anything during the step
     * @param time the time step amount
     * @param obstacles a list of potential objects to collide with
     * @return
     */
    @Override
    public float checkCollisions(float time, List<ICollidable> obstacles){
        if(boundingBox == null){
            alive = false;
        }
        // Check if the player will collide with the boundaries of the play field
        Physics.checkBoxCollision(this, 0, 0, Settings.getWindowWidth(), Settings.getWindowHeight(), time, tempCollision);
        if(tempCollision.t < earliestCollision.t){
            alive = false;
            return time;
        }

        ICollidable earliestCollidedObject = null;
        for(ICollidable o: obstacles){
            // TODO dont use instanceof
            if(o instanceof Player && ((Player)o).getID().equals(ownerID)){
                continue;
            }
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                earliestCollidedObject = o;
                alive = false;
            }
        }

        if(earliestCollidedObject != null && earliestCollidedObject instanceof Player){
            ((Player)earliestCollidedObject).damage(DAMAGE);
        }
        return time;
    }

    /**
     * Updates the visuals of the projectile
     */
    @Override
    public void draw() {
        if(alive) {
            boundingBox.setTranslateX(xpos - getXRadius());
            boundingBox.setTranslateY(ypos - getYRadius());
            boundingBox.setRotate(angle);
        }
    }

    // Getters

    @Override
    public Node getVisuals() {
        return boundingBox;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getOwnerID(){ return ownerID; }
}
