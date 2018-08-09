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

    protected int cycleCount;

    protected EventHandler<ActionEvent> eventHandler;

    public Animation(int time){
        super();
        timeline = new Timeline();
        if(time != javafx.animation.Animation.INDEFINITE) {
            cycleCount = (int) (time / RESOLUTION);
        }
        else{
            cycleCount = time;
        }

    }

    protected void setFrameEvent(EventHandler<ActionEvent> eventHandler){
        this.eventHandler = eventHandler;
    }

    public void start(Group g){

        if(eventHandler == null) return;

        this.g = g;
        g.getChildren().add(this);

        timeline.setCycleCount(cycleCount);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(RESOLUTION), eventHandler);
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

    public void stop(){
        timeline.stop();
        g.getChildren().remove(this);
    }
}
