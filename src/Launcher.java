import GameManager.FrameEvent.IFrameEvent;
import GameManager.FrameEvent.FrameEventHandler;
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
        Settings.setWindowSize(800, 800);
        GameManager game = new GameManager(new FrameEventHandler() {
            @Override
            public void handle(IFrameEvent fe) {

            }
        });

        Scene scene = new Scene(game, 800, 800);

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
