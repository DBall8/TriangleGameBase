package GameManager.UserInputHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {

    public enum Binding{
        UP,
        DOWN,
        RIGHT,
        LEFT,
        BOOST,
        SHOOT
    }

    private HashMap<Binding, KeyHandler> keyMap = new HashMap<>();

    public InputHandler(Scene scene){

        // Set default bindings
        keyMap.put(Binding.UP, new KeyHandler(KeyCode.W));
        keyMap.put(Binding.DOWN, new KeyHandler(KeyCode.S));
        keyMap.put(Binding.RIGHT, new KeyHandler(KeyCode.D));
        keyMap.put(Binding.LEFT, new KeyHandler(KeyCode.A));
        keyMap.put(Binding.BOOST, new KeyHandler(KeyCode.SPACE));
        keyMap.put(Binding.SHOOT, new KeyHandler(KeyCode.UP));


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyDown(event);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyUp(event);
            }
        });
    }

    /**
     * Detects a key press and marks any tracked keys accordingly
     * @param ke the key event
     */
    private void keyDown(KeyEvent ke){
        for (Map.Entry<Binding, KeyHandler> entry : keyMap.entrySet())
        {
            KeyHandler key = entry.getValue();
           if(ke.getCode() == key.getKey()){
               if(!key.isPressed()){
                   key.setPressed(true);
               }
           }
        }
    }

    /**
     * Detects a key release and marks any tracked keys accordingly
     * @param ke the key event
     */
    private void keyUp(KeyEvent ke){
        for (Map.Entry<Binding, KeyHandler> entry : keyMap.entrySet())
        {
            KeyHandler key = entry.getValue();
            if(ke.getCode() == key.getKey()){
                key.setPressed(false);
            }
        }
    }

    public boolean isPressed(Binding binding){
        return keyMap.get(binding).isPressed();
    }
}
