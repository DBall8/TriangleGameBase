package Objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Simple collidable object class for creating walls
 */
public class Obstacle extends Rectangle implements ICollidable {

    final static Color COLOR = Color.GREY;

    public Obstacle(int xpos, int ypos, int width, int height){
        super(width, height, Color.BLACK);
        setX(xpos);
        setY(ypos);
        setFill(COLOR);
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
