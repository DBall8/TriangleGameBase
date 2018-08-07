package Global;

import GameManager.UserInputHandler.UserInputHandler;
import javafx.scene.Scene;

/**
 * A nested singleton class for tracking game settings
 */
public class Settings {

    private static final int FRAMERATE = 60; // how many frames are genereated per second
    public final static boolean BOUNCE = false; // whether or not collisions cause bouncing

    // Screen dimenions
    private static int WIDTH = 800;
    private static int HEIGHT = 800;

    // If true, shows debug output
    private final static boolean DEBUG = false;

    private static boolean isClient = true;

    private static UserInputHandler userInput = new UserInputHandler();

    // Nested singleton
    private static class Settings_{
        private static final Settings instance = new Settings();
    }

    private static Settings getInstance(){ return Settings_.instance; }

    // Getters and Setters

    public static void setWindowSize(int width, int height){
        getInstance().WIDTH = width;
        getInstance().HEIGHT = height;
    }
    public static int getFramerate(){ return FRAMERATE; }
    public static int getWindowWidth(){ return getInstance().WIDTH; }
    public static int getWindowHeight(){ return getInstance().HEIGHT; }

    public static boolean isDebug(){ return getInstance().DEBUG; }

    public static boolean isClient(){ return getInstance().isClient; }
    public static void setServer(){ getInstance().isClient = false; }

    public static UserInputHandler initializeUserInput(Scene scene){
        getInstance().userInput.attachBindings(scene);
        return getInstance().userInput;
    }

    public static UserInputHandler getUserInputHandler(){
        return getInstance().userInput;
    }

    public UserInputHandler getUserInputListener(){ return getInstance().userInput; }
}
