package Ability;


import Objects.Entities.Player;
import Objects.Entities.Projectile;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

public class PrimaryFire extends Ability {

    private final static float FIRERATE = 100;

    private int remainingShots = 4;
    private Timer fireRateTimer = new Timer();
    private FireEventHandler feHandler;


    public PrimaryFire(Player p, Scene scene, FireEventHandler feHandler){
        super(p);
        this.feHandler = feHandler;
        cooldown = 1.5f;

        super.setUpMouseListeners(scene);
    }


    @Override
    public void use(){
        if(keyDown && !onCooldown && remainingShots > 0){
            remainingShots--;
            onCooldown = true;
            fireRateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    remainingShots++;
                }
            }, (int)(cooldown * 1000));
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onCooldown = false;
                }
            }, (int)FIRERATE);

            feHandler.handle(new FireEvent(new Projectile(p)));

        }
    }
}
