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

    public Sniper(Player p, EventHandler<FireEvent> feHandler) {
        super(p, UserInputHandler.Binding.ABILITY1);

        cooldown = 5.0f;
        this.feHandler = feHandler;
        this.p = p;
    }

    @Override
    public boolean use() {
        if(!onCooldown && isPressed()){

            feHandler.handle(new FireEvent(new Projectile(p), "hitscan"));
            goOnCooldown();
            p.addAnimation(new SniperAnimation(p), true);
            return true;
        }
        return false;
    }

}
