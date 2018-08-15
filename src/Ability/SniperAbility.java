package ability;

import animation.SniperAnimation;
import events.EventHandler;
import events.FireEvent;
import gameManager.userInputHandler.UserInputHandler;
import objects.entities.projectiles.HitScan;
import objects.entities.Player;

public class SniperAbility extends Ability {

    Player p;
    private EventHandler<FireEvent> feHandler;
    private boolean isHeld = false;
    SniperAnimation animation;

    public SniperAbility(Player p, EventHandler<FireEvent> feHandler, UserInputHandler.Binding binding) {
        super(p, binding);

        cooldown = 5.0f;
        this.feHandler = feHandler;
        this.p = p;
    }

    @Override
    public boolean use() {
        if(!onCooldown){
            if(isPressed() && !isHeld){
                p.disableMovement();
                p.slowTurning();
                isHeld = true;
                animation = new SniperAnimation(p);
                p.addAnimation(animation, true);
                return true;
            } else if(!isPressed() && isHeld){
                feHandler.handle(new FireEvent(new HitScan(p)));
                goOnCooldown();
                isHeld = false;
                animation.stop();
                p.enableMovement();
                p.unslowTurning();
                return true;
            }


        }
        return false;
    }

}
