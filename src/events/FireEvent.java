package events;

import objects.entities.projectiles.Projectile;

/**
 * Simple event for handling the firing of a projectile
 * Will likely add much more as more types of projectiles are added
 */
public class FireEvent {

    public Projectile projectile;

    public FireEvent(Projectile p){
        this.projectile = p;
    }
}
