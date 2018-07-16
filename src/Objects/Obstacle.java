package Objects;

import Objects.ICollidable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Rectangle implements ICollidable {


    public Obstacle(int xpos, int ypos, int width, int height){
        super(width, height, Color.BLACK);
        setX(xpos);
        setY(ypos);
    }

    public float rightX(){
        return (int)(getX() + getWidth());
    }

    public float leftX(){
        return (int)(getX());
    }

    public float bottomY(){
        return (int)(getY() + getHeight());
    }

    public float topY(){
        return (int)(getY());
    }



}
