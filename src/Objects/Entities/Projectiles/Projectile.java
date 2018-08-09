package Objects.Entities.Projectiles;

import Animation.HitAnimation;
import Events.HitEvent;
import Global.Settings;
import Objects.Entities.Entity;
import Objects.Entities.Player;
import Objects.ICollidable;
import Physics.Physics;
import Events.EventHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for handling projectile entities
 */
public abstract class Projectile extends Entity {

    protected String ownerID; // the player that shot this projectile
    protected boolean alive; // true when projectile is still traveling through the air

    protected EventHandler hitEventHandler;

    protected Type type;
    protected int damage = 1;
    protected boolean p1Controlled;

    public enum Type{
        BasicShot,
        HitScan
    }

    /**
     * Constructor for a projectile that already has an ID
     * @param ID The ID of the projectile
     * @param ownerID the ID of the player who shot the projectile
     */
    public Projectile(String ID, String ownerID, float x, float y, int width, int height){
        super(ID, (int)x, (int)y, width, height);

        this.ownerID = ownerID;
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

        for(ICollidable o: obstacles){
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                alive = false;
            }
        }
        return time;
    }

    public void checkHits(HashMap<String, Player> players){
        for(Map.Entry<String, Player> entry: players.entrySet()){
            Player p = entry.getValue();
            if(p.getID().equals(ownerID)){
                continue;
            }

            if(p.getBoundingBox().intersects(boundingBox)){
                if(p1Controlled) {
                    p.damage(damage);
                    if(hitEventHandler != null){
                        hitEventHandler.handle(new HitEvent(p.getID(), (int)xpos, (int)ypos, damage ));
                    }
                }
                alive = false;
            }
        }
    }


    // Getters
    public boolean isAlive() {
        return alive;
    }

    public String getOwnerID(){ return ownerID; }

    // Setters

    public void setHitEventHandler(EventHandler hitEventHandler){
        this.hitEventHandler = hitEventHandler;
    }
}
