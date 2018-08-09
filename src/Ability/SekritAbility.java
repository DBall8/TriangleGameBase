package Ability;

import Events.EventHandler;
import Events.FireEvent;
import GameManager.UserInputHandler.UserInputHandler;
import Objects.Entities.Projectiles.BasicShot;
import Objects.Entities.Player;

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
            feHandler.handle(new FireEvent(new BasicShot(p), "sekrit"));
            goOnCooldown();
            return true;
        }
        return false;
    }
}
