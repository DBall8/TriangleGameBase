package Objects.Entities;

import Global.Settings;
import Objects.ICollidable;
import Physics.Physics;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Projectile extends Entity {

    private Player owner;
    private boolean alive;

    public Projectile(float xpos, float ypos, float velocity, float angle, Player p){
        super((int)xpos, (int)ypos);
        this.xvel = Physics.xComponent(velocity, Physics.toRadiians(angle));
        this.yvel = Physics.yComponent(velocity, Physics.toRadiians(angle));
        this.angle = angle;
        this.width = 10;
        this.height = 30;
        this.owner = p;
        boundingBox = new Rectangle(width, height);

        alive = true;
    }

    @Override
    public float checkCollisions(float time, List<ICollidable> obstacles){
        if(boundingBox == null){
            alive = false;
        }
        // Check if the player will collide with the boundaries of the play field
        Physics.checkBoxCollision(this, 0, 0, Settings.getWindowWidth(), Settings.getWindowHeight(), time, tempCollision);
        if(tempCollision.t < earliestCollision.t){
            alive = false;
            System.out.println(tempCollision.t);
            return time;
        }
        for(ICollidable o: obstacles){
            if(o.equals(owner)){
                continue;
            }
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                alive = false;
                System.out.println(tempCollision.t);
                return time;
            }
        }
        return time;
    }

    @Override
    public void draw() {
        boundingBox.setTranslateX(xpos);
        boundingBox.setTranslateY(ypos);
        boundingBox.setRotate(angle);
    }

    @Override
    public Node getVisuals() {
        return boundingBox;
    }

    public boolean isAlive() {
        return alive;
    }
}
