package Ability;


import Events.EventHandler;
import Objects.Entities.Player;
import Objects.Entities.Projectile;
import Events.FireEvent;
import javafx.scene.Scene;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class for handling the primary projectile fire ability of the player
 */
public class PrimaryFire extends Ability {

    private final static float FIRERATE = 100; // the rate of fire (ms)

    private int remainingShots = 4; // for tracking how many shots are ready to be fired
    private Timer fireRateTimer = new Timer(); // an additional timer for tracking fire rate
    private EventHandler<FireEvent> feHandler; // an object for sending successful firing events to

    // Constructor
    public PrimaryFire(Player p, Scene scene, EventHandler<FireEvent> feHandler){
        super(p);
        this.feHandler = feHandler;
        cooldown = 1.5f; // set cooldown too 1.5(s)

        // set up as a mouse click ability
        super.setUpMouseListeners(scene);
    }


    // Attempts to use the ability
    @Override
    public boolean use(){
        // Fire only if the mouse is presseed, the ability is not on cooldown, and the number of shots left is greater
        // than none
        if(keyDown && !onCooldown && remainingShots > 0){
            remainingShots--; // consume a shot
            onCooldown = true; // go onto cooldown

            // regain a shot after a little time
            fireRateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    remainingShots++;
                }
            }, (int)(cooldown * 1000));

            // come off of cooldown after a little time
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onCooldown = false;
                }
            }, (int)FIRERATE);

            // fire the shot
            feHandler.handle(new FireEvent(new Projectile(p)));
            return true;
        }

        return false;
    }
}
