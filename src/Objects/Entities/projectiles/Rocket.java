package objects.entities.projectiles;

import animation.HitAnimation;
import events.EventHandler;
import events.FireEvent;
import events.HitEvent;
import global.Settings;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import objects.ICollidable;
import objects.entities.Player;
import physics.Physics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rocket extends Projectile {

    private final static int WIDTH = 20;
    private final static int HEIGHT = 40;
    private final static float VELOCITY = 10;
    private final static int DAMAGE = 1;

    private Group body;
    private EventHandler<FireEvent> feHandler;
    private Color c;

    public Rocket(Player p, EventHandler<FireEvent> feHandler) {
        super("Rck-" + System.currentTimeMillis(), p.getID(), p.getX(), p.getY(), p.getAngle(), WIDTH, HEIGHT);

        this.feHandler = feHandler;

        p1Controlled = true;
        damage = DAMAGE;
        type = Type.Rocket;

        this.xvel = Physics.xComponent(VELOCITY, Physics.toRadiians(angle));
        this.yvel = Physics.yComponent(VELOCITY, Physics.toRadiians(angle));

        initializeVisuals(p.getColor());
    }

    public Rocket(String ID, String ownerID, float x, float y, float xvel, float yvel, float angle){
        super(ID, ownerID, x, y, angle, WIDTH, HEIGHT);

        damage = DAMAGE;
        type = Type.Rocket;
        p1Controlled = false;

        this.xvel = xvel;
        this.yvel = yvel;

        initializeVisuals(Color.RED);
    }

    private void initializeVisuals(Color c){
        this.c = c;
        body = new Group();
        Rectangle rec = new Rectangle(WIDTH, HEIGHT);
        rec.setFill(c);
        rec.setTranslateY(HEIGHT/2);
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                0.0, HEIGHT/2.0,
                WIDTH/2.0, 0.0,
                (double)WIDTH, HEIGHT/2.0
        });
        triangle.setFill(c);
        body.getChildren().addAll(triangle, rec);

        visuals.getChildren().add(body);
    }

    /**
     * Updates the visuals of the projectile
     */
    @Override
    public void draw() {
        super.draw();
        if(alive) {
            body.setTranslateX(xpos - WIDTH/2);
            body.setTranslateY(ypos - HEIGHT/2);
            body.setRotate(angle);
        }
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
            explode();
            return time;
        }

        for(ICollidable o: obstacles){
            Physics.checkObstacleCollision(this, o, time, tempCollision);
            if(tempCollision.t < earliestCollision.t){
                alive = false;
                explode();
            }
        }
        return time;
    }

    @Override
    public void checkHits(HashMap<String, Player> players){
        for(Map.Entry<String, Player> entry: players.entrySet()){
            Player p = entry.getValue();
            if(p.getID().equals(ownerID)){
                continue;
            }

            if(p.getBoundingBox().intersects(boundingBox)){
                if(p1Controlled) {
                    p.damage(damage);
                    if(hitEventHandler != null){
                        hitEventHandler.handle(new HitEvent(p.getID(), (int)xpos, (int)ypos, damage ));
                    }
                }
                p.addAnimation(new HitAnimation((int)xpos, (int)ypos, damage), false);
                alive = false;
                explode();
            }
        }
    }

    private void explode(){
        if(feHandler != null && p1Controlled){
            feHandler.handle(new FireEvent(new Explosion(ownerID, xpos, ypos, c)));
        }
    }
}
