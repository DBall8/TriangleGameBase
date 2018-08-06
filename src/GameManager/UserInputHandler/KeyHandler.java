package GameManager.UserInputHandler;

import javafx.scene.input.KeyCode;

public class KeyHandler {

    private KeyCode key;
    private boolean isPressed;

    public KeyHandler(KeyCode key){
        this.key = key;
        isPressed = false;
    }

    KeyCode getKey(){ return key; }

    boolean isPressed(){ return isPressed; }
    void setPressed(boolean isPressed){ this.isPressed = isPressed; }
}
