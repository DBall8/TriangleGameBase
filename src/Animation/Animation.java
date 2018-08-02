package Animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;

public abstract class Animation extends Group {

    final static float RESOLUTION = 1000 / 30; // 30 Frames per second

    private Timeline timeline;
    private Group g;

    public Animation(Group g){
        super();
        this.g = g;
        g.getChildren().add(this);
        timeline = new Timeline();

    }

    public void start(int time, EventHandler event){
        int cycleCount = (int)(time / RESOLUTION);
        timeline.setCycleCount(cycleCount);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(RESOLUTION), event);
        timeline.getKeyFrames().add(keyFrame);

        Animation a = this;
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                g.getChildren().remove(a);
            }
        });
        timeline.play();
    }
}
