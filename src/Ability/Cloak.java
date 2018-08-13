package ability;

import animation.InvisibleAnimation;
import gameManager.userInputHandler.UserInputHandler;
import objects.entities.Player;

import java.util.Timer;
import java.util.TimerTask;

public class Cloak extends Ability {

    private final static int DURATION = 3;
    private final static int COOLDOWN = 7;

    private boolean inUse = false;

    private Timer durationTimer;


    public Cloak(Player p) {
        super(p, UserInputHandler.Binding.ABILITY2);

        cooldown = COOLDOWN;
    }

    @Override
    public boolean use(){
        if(isPressed() && !inUse && !onCooldown){
            p.addAnimation(new InvisibleAnimation(p, true), true);
            p.setInvisile(true);
            inUse = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    inUse = false;
                    goOnCooldown();
                    p.addAnimation(new InvisibleAnimation(p, false), true);
                }
            }, DURATION*1000);

            return true;
        }

        return false;
    }

    public void endEarly(){
        timer.cancel();
        inUse = false;
        goOnCooldown();
    }

    public static int getDuration(){ return DURATION; }
}
