package Ability;

import Objects.Entities.Player;
import Visuals.PlayerUI;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.TimerTask;

public class Boost extends Ability {

    final static float COOLDOWN = 1.0f;
    final static int FUELMAX = 100;
    final static int CONSUMPTIONRATE = 2;
    final static int REFUELRATE = 1;

    private PlayerUI hud;

    private int fuel = FUELMAX;

    public Boost(Player p, Scene scene){
        super(p);
        cooldown = COOLDOWN;

        setUpListeners(scene, KeyCode.SPACE);
    }

    public void attachUI(PlayerUI ui){
        this.hud = ui;
    }

    @Override
    public boolean use() {
        if(keyDown){
            if(fuel > 0){
                onCooldown = true;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onCooldown = false;
                    }
                }, (int)cooldown * 1000);

                fuel -= CONSUMPTIONRATE;
                if(fuel < 0) fuel = 0;
                updateUI();
                return true;
            }
        }
        else{
            if(!onCooldown && fuel < FUELMAX){
                fuel += REFUELRATE;
                if(fuel > FUELMAX) fuel = FUELMAX;
                updateUI();
            }
        }

        return false;
    }

    private void updateUI(){
        if(hud != null){
            hud.notifyBoostChanged((double)fuel / (double) FUELMAX);
        }
    }
}
