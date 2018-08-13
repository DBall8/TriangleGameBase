package ability;

import gameManager.userInputHandler.UserInputHandler;
import objects.entities.Player;
import visuals.AbilityCooldownUI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A class for handling player abilities
 */
public abstract class Ability {

    protected Player p; // the player who owns the ability

    private UserInputHandler userInputHandler;
    private UserInputHandler.Binding binding;

    protected float cooldown = 5.0f; // Cooldown in seconds
    protected boolean onCooldown = false; // true when on Cooldown, false when available
    protected Timer timer = new Timer(); // the timer responsible for tracking the cooldown

    protected AbilityCooldownUI ui;

    // Constructor
    public Ability(Player p, UserInputHandler.Binding binding){
        this.p = p;
        userInputHandler = p.getInputHandler();
        this.binding = binding;
    }

    protected boolean isPressed(){
        if(userInputHandler == null){
            return false;
        }
        return userInputHandler.isPressed(binding);
    }

    protected void goOnCooldown(){
        onCooldown = true;
        // come off of cooldown after a little time
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onCooldown = false;
            }
        }, (int)(cooldown * 1000));

        if(ui != null){
            ui.goOnCooldown((int)cooldown);
        }
    }

    public boolean use() {
        if(!onCooldown && isPressed()){
            goOnCooldown();
            return true;
        }
        return false;
    }

    public void attachUI(AbilityCooldownUI ui){
        this.ui = ui;
    }

}
