package Events;

import Objects.Entities.Projectile;

/**
 * Simple event for handling the firing of a projectile
 * Will likely add much more as more types of projectiles are added
 */
public class FireEvent {

    public Projectile projectile;
    public String type;

    public FireEvent(Projectile p, String type){
        this.type = type;
        this.projectile = p;
    }
}
