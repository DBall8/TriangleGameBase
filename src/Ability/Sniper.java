package Ability;

import Animation.SniperAnimation;
import GameManager.UserInputHandler.UserInputHandler;
import Objects.Entities.Player;

public class Sniper extends Ability {

    Player p;

    public Sniper(Player p) {
        super(p, UserInputHandler.Binding.ABILITY1);

        this.p = p;
    }

    @Override
    public boolean use() {
        if(!onCooldown && isPressed()){
            //p.disableMovement();
            goOnCooldown();
            p.addAnimation(new SniperAnimation(p.getColor()), true);
            return true;
        }
        return false;
    }

}
