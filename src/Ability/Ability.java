package Ability;

import Objects.Entities.Player;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Ability {

    protected Player p;

    protected boolean keyDown;

    protected float cooldown = 5.0f; // Cooldown in seconds
    protected boolean onCooldown = false; // true when on Cooldown, false when available
    protected Timer timer = new Timer();

    public Ability(Player p){
        this.p = p;
        keyDown = false;
    }

    protected void setUpListeners(Scene scene, KeyCode key){
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == key && !keyDown) keyDown = true;
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == key)
                    keyDown = false;
            }
        });
    }

    protected void setUpMouseListeners(Scene scene){
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!keyDown) keyDown = true;
                }
        });

        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    keyDown = false;
            }
        });
    }

    public abstract void use();

}
