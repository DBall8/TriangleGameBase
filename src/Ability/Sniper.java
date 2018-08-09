package Ability;

import Animation.SniperAnimation;
import Events.EventHandler;
import Events.FireEvent;
import GameManager.UserInputHandler.UserInputHandler;
import Objects.Entities.Player;
import Objects.Entities.Projectile;

public class Sniper extends Ability {

    Player p;
    private EventHandler<FireEvent> feHandler;
    private boolean isHeld = false;
    SniperAnimation animation;

    public Sniper(Player p, EventHandler<FireEvent> feHandler) {
        super(p, UserInputHandler.Binding.ABILITY1);

        cooldown = 5.0f;
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
                feHandler.handle(new FireEvent(new Projectile(p), "hitscan"));
                goOnCooldown();
                isHeld = false;
                animation.stop();
                return true;
            }

        }
        return false;
    }

}
