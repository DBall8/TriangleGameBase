package Ability;

import Events.EventHandler;
import Events.FireEvent;
import GameManager.UserInputHandler.UserInputHandler;
import Objects.Entities.Player;
import Objects.Entities.Projectile;

public class SekritAbility extends Ability {

    Player p;
    EventHandler<FireEvent> feHandler;

    public SekritAbility(Player p, EventHandler<FireEvent> feHandler) {
        super(p, UserInputHandler.Binding.SEKRIT);
        cooldown = 0.1f;
        this.p = p;
        this.feHandler = feHandler;
    }

    @Override
    public boolean use(){
        if(!onCooldown && isPressed()){
            feHandler.handle(new FireEvent(new Projectile(p), "sekrit"));
            goOnCooldown();
            return true;
        }
        return false;
    }
}
