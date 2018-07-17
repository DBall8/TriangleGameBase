package Ability;


import java.util.Timer;
import java.util.TimerTask;

public class PrimaryFire extends Ability {

    private final static float FIRERATE = 150;

    private int remainingShots = 4;
    private Timer fireRateTimer = new Timer();


    public PrimaryFire(){
        cooldown = 1.5f;
    }


    @Override
    public boolean use(){
        if(!onCooldown && remainingShots > 0){
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
            return true;

        }
        else{
            return false;
        }
    }
}
