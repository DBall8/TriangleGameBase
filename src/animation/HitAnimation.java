package animation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HitAnimation extends Animation {

    private final static int DURATION = 200; // ms
    Circle circle;

    public HitAnimation(int x, int y) {
        super(DURATION);

        type = Type.HitAnimation;

        circle = new Circle(5);
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        circle.setFill(Color.RED);

        getChildren().add(circle);

        super.setFrameEvent(new EventHandler() {
            @Override
            public void handle(Event event) {
                circle.setRadius(circle.getRadius() + 1);
            }
        });

    }
}
