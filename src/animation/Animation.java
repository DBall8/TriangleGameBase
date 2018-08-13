package animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;

public abstract class Animation extends Group {

    final static float RESOLUTION = 1000 / 30; // 30 Frames per second

    private Timeline timeline;
    protected Group g;

    protected int cycleCount;

    protected EventHandler<ActionEvent> eventHandler;
    protected EventHandler<ActionEvent> finishedEventHandler;

    protected Type type;
    protected String ownerID;

    public enum Type{
        HitAnimation,
        SniperAnimation,
        Invisibilty
    }

    public Animation(int time){ // time in ms
        super();
        timeline = new Timeline();
        if(time == 0){
            cycleCount = 1;
        }
        else if(time != javafx.animation.Animation.INDEFINITE) {
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
                if(finishedEventHandler != null){
                    finishedEventHandler.handle(event);
                }
                g.getChildren().remove(a);
            }
        });


        timeline.play();
    }

    public void stop(){
        timeline.stop();
        if(finishedEventHandler != null){
            finishedEventHandler.handle(new ActionEvent());
        }
        g.getChildren().remove(this);
    }

    public Type getType(){ return type; }

    public String getOwnerID(){
        if(ownerID != null){
            return ownerID;
        }

        return "";
    }
}
