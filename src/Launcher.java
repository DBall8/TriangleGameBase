import Events.EventHandler;
import Events.FrameEvent.FrameEvent;
import GameManager.GameManager;
import Global.Settings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for launching a single player, local, game simulation
 */
public class Launcher extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        GameManager game = new GameManager(new EventHandler<FrameEvent>() {
            @Override
            public void handle(FrameEvent fe) {

            }
        });

        Scene scene = new Scene(game, Settings.getWindowWidth(), Settings.getWindowHeight());

        primaryStage.setScene(scene);
        primaryStage.show();

        game.start(scene, false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        System.exit(0);
    }
}
