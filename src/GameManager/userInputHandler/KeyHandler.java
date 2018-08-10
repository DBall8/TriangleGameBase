package gameManager.userInputHandler;

import javafx.scene.input.KeyCode;

public class KeyHandler {

    private KeyCode key;
    private boolean isPressed;

    public KeyHandler(KeyCode key){
        this.key = key;
        isPressed = false;
    }

    KeyCode getKey(){ return key; }

    void setKey(KeyCode key){
        this.key = key;
        isPressed = false;
    }

    boolean isPressed(){ return isPressed; }
    void setPressed(boolean isPressed){ this.isPressed = isPressed; }
}
