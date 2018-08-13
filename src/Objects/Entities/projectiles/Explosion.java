package objects.entities.projectiles;

import animation.HitAnimation;
import events.HitEvent;
import global.Settings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import objects.ICollidable;
import objects.entities.Player;
import physics.Bounds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Explosion extends Projectile {

    private final static int STARTRADIUS = 5;
    private final static int ENDRADIUS = 80;
    private final static float GROWTHRATE = 0.2f;

    private final static float CLOSEENOUGH = 1f;

    private final static int DAMAGE = 2;

    private Circle body;
    private float currRadius = STARTRADIUS;

    private List<String> damagedPlayers = new LinkedList<>();

    public Explosion(String ownerID, float x, float y, Color c) {
        super("Exp-" + System.currentTimeMillis(), ownerID, x, y, 0, 2*STARTRADIUS, 2*STARTRADIUS);

        p1Controlled = true;
        damage = DAMAGE;
        type = Type.Explosion;

        this.xvel = 0;
        this.yvel = 0;

        body = new Circle(STARTRADIUS);
        body.setFill(c);
        body.setTranslateX(xpos);
        body.setTranslateY(ypos);
        visuals.getChildren().add(body);
    }

    public Explosion(String ID, String ownerID, float x, float y){
        super(ID, ownerID, x, y, 0, 2*STARTRADIUS, 2*STARTRADIUS);

        p1Controlled = false;
        damage = DAMAGE;
        type = Type.Explosion;

        this.xvel = this.yvel = 0;

        body = new Circle(STARTRADIUS);
        body.setFill(Color.RED);
        body.setTranslateX(xpos);
        body.setTranslateY(ypos);
        visuals.getChildren().add(body);
    }

    @Override
    public void update(){
        if(currRadius >= ENDRADIUS - CLOSEENOUGH){
            alive = false;
        }

        currRadius = currRadius + (ENDRADIUS-currRadius)*GROWTHRATE;
        boundingBox = new Bounds(xpos, ypos, 2*currRadius, 2*currRadius);
        if(Settings.isDebug()){
            boundingBoxVisuals.setWidth(boundingBox.getWidth());
            boundingBoxVisuals.setHeight(boundingBox.getHeight());
        }
        body.setRadius(currRadius);
    }

    @Override
    public float checkCollisions(float time, List<ICollidable> obstacles){
        if(boundingBox == null){
            alive = false;
        }

        return time;
    }

    @Override
    public void checkHits(HashMap<String, Player> players){
        for(Map.Entry<String, Player> entry: players.entrySet()){
            Player p = entry.getValue();
            if(p.getID().equals(ownerID) || damagedPlayers.contains(p.getID())){
                continue;
            }

            if(p.getBoundingBox().intersects(boundingBox)){
                if(p1Controlled) {
                    p.damage(damage);
                    damagedPlayers.add(p.getID());
                    if(hitEventHandler != null){
                        hitEventHandler.handle(new HitEvent(p.getID(), (int)xpos, (int)ypos, damage ));
                    }
                }
                p.addAnimation(new HitAnimation((int)xpos, (int)ypos, damage), false);
            }
        }
    }

}
