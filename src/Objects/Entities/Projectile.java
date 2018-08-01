package Objects.Entities;

import Global.Settings;
import Objects.ICollidable;
import Physics.Physics;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * A class for handling projectile entities
 */
public class Projectile extends Entity {

    private final static int WIDTH = 10;
    private final static int HEIGHT = 30;
    private final static float PVELOCITY = 5; // the base velocity of a projectile
    private final static float MOVEFACTOR = 0.9f; // the percentage of the player's speed to add to the projectile speed

    private Player owner; // the player that shot this projectile
    private boolean alive; // true when projectile is still traveling through the air

    /**
     * Constructor for brand new projectile
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
        this.owner = p;
        boundingBox = new Rectangle(width, height);

        alive = true;
    }

    public Projectile(String ID, Player p){
        super(ID, (int)p.getX(), (int)p.getY());
        float pvel = p.getVelocity() * MOVEFACTOR + PVELOCITY;
        this.angle = p.getAngle();
        this.xvel = Physics.xComponent(pvel, Physics.toRadiians(angle));
        this.yvel = Physics.yComponent(pvel, Physics.toRadiians(angle));

        this.width = 10;
        this.height = 30;
        this.owner = p;
        boundingBox = new Rectangle(width, height);

        alive = true;
    }

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
        for(ICollidable o: obstacles){
            if(o.equals(owner)){
                continue;
            }
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                alive = false;
                return time;
            }
        }
        return time;
    }

    @Override
    public void draw() {
        if(alive) {
            boundingBox.setTranslateX(xpos - getXRadius());
            boundingBox.setTranslateY(ypos - getYRadius());
            boundingBox.setRotate(angle);
        }
    }

    @Override
    public Node getVisuals() {
        return boundingBox;
    }

    public boolean isAlive() {
        return alive;
    }
}
