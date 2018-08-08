package Animation;

import Objects.Entities.Player;
import Physics.Physics;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SniperAnimation extends Animation {

    private final static int STARTWIDTH = 40;
    private final static int ENDWIDTH = 1;
    private final static int LENGTH = 10000;

    private final static int DURATION = 2 * 1000; // ms

    private Rectangle shotLine;

    public SniperAnimation(Player p) {
        super(DURATION);

        shotLine = new Rectangle(STARTWIDTH, LENGTH);
        shotLine.setFill(p.getColor());
        shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
        shotLine.setTranslateY((Player.HEIGHT/2) - (LENGTH));
        getChildren().add(shotLine);

        float changeAmount = ((float)STARTWIDTH - (float)ENDWIDTH) / (float)cycleCount;

        super.setFrameEvent(new EventHandler() {
            @Override
            public void handle(Event event) {
                shotLine.setWidth(shotLine.getWidth() - changeAmount);
                shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
            }
        });
    }
}
