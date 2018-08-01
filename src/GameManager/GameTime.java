package GameManager;
import Global.Settings;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * A class for handling the timing of frame generation
 */
public class GameTime{

    private GameManager game; // the game whose frames are being handled
    private Timeline time; // UI Thread for creating frames
    private boolean playing = false; // true when the game is running, false when it is not

    // Constructor
    public GameTime(GameManager game){

        this.game = game;
        // Set up the animation timer
        time = new Timeline();
        time.setCycleCount(Animation.INDEFINITE); // have it run indefinitely
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000L / Settings.getFramerate()), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // update positions
                game.calculateFrame();
                // update visuals
                game.draw();
            }
        });
        time.getKeyFrames().add(keyFrame);
    }

    /**
     * Starts the game running
     */
    public void play(){
        if(!playing){
            playing = true;
            game.draw();
            time.play();
        }
    }

    /**
     * Pauses the game from running
     */
    public void pause(){
        if(playing){
            playing = false;
            time.pause();
        }
    }
}
