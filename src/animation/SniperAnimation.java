package animation;

import javafx.scene.paint.Color;
import objects.entities.Player;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;

public class SniperAnimation extends Animation {

    private final static int STARTWIDTH = 40;
    private final static int ENDWIDTH = 1;
    private final static int LENGTH = 10000;

    private Rectangle shotLine;

    public SniperAnimation(Player p) {
        super(javafx.animation.Animation.INDEFINITE);

        type = Type.SniperAnimation;
        ownerID = p.getID();

        shotLine = new Rectangle(STARTWIDTH, LENGTH);
        shotLine.setFill(p.getColor());
        shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
        shotLine.setTranslateY((Player.HEIGHT/2) - (LENGTH));
        getChildren().add(shotLine);

        float changePercent = 0.9f;

        super.setFrameEvent(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(shotLine.getWidth() > ENDWIDTH){
                    shotLine.setWidth(shotLine.getWidth() * changePercent);
                    shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
                }
            }
        });
    }

    public SniperAnimation(String ownerID) {
        super(javafx.animation.Animation.INDEFINITE);

        type = Type.SniperAnimation;
        this.ownerID = ownerID;

        shotLine = new Rectangle(STARTWIDTH, LENGTH);
        shotLine.setFill(Color.RED);
        shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
        shotLine.setTranslateY((Player.HEIGHT/2) - (LENGTH));
        getChildren().add(shotLine);

        float changePercent = 0.9f;

        super.setFrameEvent(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(shotLine.getWidth() > ENDWIDTH){
                    shotLine.setWidth(shotLine.getWidth() * changePercent);
                    shotLine.setTranslateX((Player.WIDTH/2) - (shotLine.getWidth()/2));
                }
            }
        });
    }
}
