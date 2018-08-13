package animation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HitAnimation extends Animation {

    private final static int STARTRADIUS = 5;
    private static int ENDRADIUSBASE = 20;
    private final static int DURATION = 200; // ms

    private static float DELTA;

    private float endRadius = 10;
    Circle circle;

    public HitAnimation(int x, int y, int sizeFactor) {
        super(DURATION);

        type = Type.HitAnimation;

        circle = new Circle(5);
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        circle.setFill(Color.RED);

        getChildren().add(circle);

        endRadius = ENDRADIUSBASE * sizeFactor;
        DELTA  = (endRadius - STARTRADIUS) / cycleCount;

        super.setFrameEvent(new EventHandler() {
            @Override
            public void handle(Event event) {
                double radius = circle.getRadius();
                if(radius < endRadius) {
                    circle.setRadius(radius + DELTA);
                }
            }
        });

    }
}
