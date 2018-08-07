package GameManager.UserInputHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class UserInputHandler {

    private boolean ready = false;

    public enum Binding{
        UP,
        DOWN,
        RIGHT,
        LEFT,
        BOOST,
        SHOOT,
        ABILITY1,
        ABILITY2
    }

    private HashMap<Binding, KeyHandler> keyMap = new HashMap<>();

    public UserInputHandler(){
        setDefaultBindings();
    }

    public void attachBindings(Scene scene){
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

        ready = true;
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

    public void setBinding(Binding binding, KeyCode key){
        if(keyMap.containsKey(binding)){
            keyMap.get(binding).setKey(key);
        }
        else{
            keyMap.put(binding, new KeyHandler(key));
        }
    }

    public String getBoundKey(Binding binding){
        if(keyMap.containsKey(binding)){
            return keyMap.get(binding).getKey().toString();
        }
        else{
            return "Unbound";
        }
    }

    private void setDefaultBindings(){
        // Set default bindings
        keyMap.put(Binding.UP, new KeyHandler(KeyCode.W));
        keyMap.put(Binding.DOWN, new KeyHandler(KeyCode.S));
        keyMap.put(Binding.RIGHT, new KeyHandler(KeyCode.D));
        keyMap.put(Binding.LEFT, new KeyHandler(KeyCode.A));
        keyMap.put(Binding.BOOST, new KeyHandler(KeyCode.SPACE));
        keyMap.put(Binding.SHOOT, new KeyHandler(KeyCode.UP));
    }
}
