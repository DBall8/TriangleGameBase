package ability;

import animation.SniperAnimation;
import events.EventHandler;
import events.FireEvent;
import gameManager.userInputHandler.UserInputHandler;
import objects.entities.projectiles.HitScan;
import objects.entities.Player;

public class Sniper extends Ability {

    Player p;
    private EventHandler<FireEvent> feHandler;
    private boolean isHeld = false;
    SniperAnimation animation;

    public Sniper(Player p, EventHandler<FireEvent> feHandler) {
        super(p, UserInputHandler.Binding.ABILITY1);

        cooldown = 0.1f;
        this.feHandler = feHandler;
        this.p = p;
    }

    @Override
    public boolean use() {
        if(!onCooldown){
            if(isPressed() && !isHeld){
                isHeld = true;
                animation = new SniperAnimation(p);
                p.addAnimation(animation, true);
            } else if(!isPressed() && isHeld){
                feHandler.handle(new FireEvent(new HitScan(p), "hitscan"));
                goOnCooldown();
                isHeld = false;
                animation.stop();
                return true;
            }

        }
        return false;
    }

}
