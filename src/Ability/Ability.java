package Ability;

import Objects.Entities.Player;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A class for handling player abilities
 */
public abstract class Ability {

    protected Player p; // the player who owns the ability

    protected boolean keyDown; // a boolean tracking whether or not the ability's key is currently pressed

    protected float cooldown = 5.0f; // Cooldown in seconds
    protected boolean onCooldown = false; // true when on Cooldown, false when available
    protected Timer timer = new Timer(); // the timer responsible for tracking the cooldown

    // Constructor
    public Ability(Player p){
        this.p = p;
        keyDown = false;
    }

    /**
     * Sets up key press listeners for the key corresponding to this ability
     * @param scene the scene to create the listener in
     * @param key the key to listen for
     */
    protected void setUpListeners(Scene scene, KeyCode key){

        // Create key down event
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == key && !keyDown) keyDown = true;
            }
        });

        // Create key up event
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == key)
                    keyDown = false;
            }
        });
    }

    /**
     * Sets up listeners for mouse clicks, for abilities triggered by the mouse
     * @param scene the scene to create the listener in
     */
    protected void setUpMouseListeners(Scene scene){

        // Set up mouse down event
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!keyDown) keyDown = true;
                }
        });

        // Set up mouse up event
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    keyDown = false;
            }
        });
    }

    // Function for using the ability
    public abstract boolean use();

}
