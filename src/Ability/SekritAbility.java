package Ability;

import events.EventHandler;
import events.FireEvent;
import gameManager.userInputHandler.UserInputHandler;
import objects.entities.projectiles.BasicShot;
import objects.entities.Player;

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
