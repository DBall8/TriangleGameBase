package ability;

import events.EventHandler;
import events.FireEvent;
import gameManager.userInputHandler.UserInputHandler;
import objects.entities.Player;
import objects.entities.projectiles.Rocket;


public class RocketAbility extends Ability {

    private static float COOLDOWN = 3.0f; // cooldown in seconds

    private EventHandler<FireEvent> feHandler; // an object for sending successful firing events to

    public RocketAbility(Player p, EventHandler<FireEvent> feHandler) {
        super(p, UserInputHandler.Binding.ABILITY2);
        this.feHandler = feHandler;

        this.cooldown = COOLDOWN;
    }

    @Override
    public boolean use(){
        if(isPressed() && !onCooldown){
            goOnCooldown();
            feHandler.handle(new FireEvent(new Rocket(p, feHandler)));
            return true;
        }

        return false;
    }
}
