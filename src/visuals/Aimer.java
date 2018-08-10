package visuals;

import objects.entities.Player;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Aimer extends Group {

    private Circle[] circles = {new Circle(2), new Circle(2), new Circle(2)};

    private boolean isShown = false;

    public Aimer(Color c){
        super();
        circles[0].setTranslateX(Player.WIDTH/2);
        circles[1].setTranslateX(Player.WIDTH/2);
        circles[2].setTranslateX(Player.WIDTH/2);

        circles[0].setTranslateY(-1*(Player.HEIGHT));
        circles[1].setTranslateY(-1*(Player.HEIGHT*3));
        circles[2].setTranslateY(-1*(Player.HEIGHT*6));

        circles[0].setFill(c);
        circles[1].setFill(c);
        circles[2].setFill(c);

    }

    public void show(){
        if(isShown) return;
        getChildren().addAll(circles);
        isShown = true;
    }

    public void hide(){
        if(!isShown) return;
        getChildren().clear();
        isShown = false;
    }
}
