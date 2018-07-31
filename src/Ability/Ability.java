package Ability;

import java.util.Timer;
import java.util.TimerTask;

public class Ability {
    protected float cooldown = 5.0f; // Cooldown in seconds
    protected boolean onCooldown = false; // true when on Cooldown, false when available
    protected Timer timer = new Timer();

    public Ability(){
    }

    public boolean use(){
        if(onCooldown) {
            return false;
        }
        else{
            onCooldown = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    consume();
                }
            }, (int)(cooldown*1000));
            return true;
        }
    }

    protected void consume(){
        onCooldown = false;
    }
}
