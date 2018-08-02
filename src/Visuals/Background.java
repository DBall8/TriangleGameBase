package Visuals;

import Global.Settings;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Background extends Group {

    public Background(){
        super();

        Rectangle rec = new javafx.scene.shape.Rectangle(Settings.getWindowWidth(), Settings.getWindowHeight());
        rec.setFill(Color.BLACK);
        getChildren().add(rec);
    }
}
