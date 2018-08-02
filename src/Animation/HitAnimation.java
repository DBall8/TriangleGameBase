package Animation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HitAnimation extends Animation {

    Circle circle;

    public HitAnimation(Group g, int x, int y) {
        super(g);

        circle = new Circle(5);
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        circle.setFill(Color.RED);

        getChildren().add(circle);

        super.start(200, new EventHandler() {
            @Override
            public void handle(Event event) {
                circle.setRadius(circle.getRadius() + 1);
            }
        });

    }
}
