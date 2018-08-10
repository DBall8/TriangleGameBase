package Objects.Entities.Projectiles;

import Events.HitEvent;
import Global.Settings;
import Objects.Entities.Player;
import Objects.Entities.Projectiles.Projectile;
import Objects.ICollidable;
import Physics.Line;
import Physics.Physics;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HitScan extends Projectile {

    private final static int DAMAGE = 2;
    private final static int WIDTH = 4;
    private final static int HANGAROUNDTIME = 200; //ms

    private Rectangle body;
    private boolean active = true;

    private Line los;

    private int framesSpentAlive = 0;

    public HitScan(Player p) {
        super("Hit-" + System.currentTimeMillis(), p.getID(), p.getX(), p.getY(), p.getAngle(), WIDTH, 1);

        p1Controlled = true;
        los = Physics.getLOS(p.getX(), p.getY(), p.getAngle());

        damage = DAMAGE;

        height = los.getLength();

        body = new Rectangle(WIDTH, los.getLength());
        body.setFill(p.getColor());

        Rotate r = new Rotate();
        r.setPivotX(WIDTH/2);
        r.setPivotY(0);
        r.setAngle(p.getAngle() - 180);
        body.setTranslateX(p.getX() - WIDTH/2);
        body.setTranslateY(p.getY());
        body.getTransforms().add(r);


        visuals.getChildren().add(body);

        type = Type.HitScan;
    }

    public HitScan(String ID, String ownerID, float x, float y, float angle) {
        super(ID, ownerID, x, y, angle, WIDTH, 1);

        p1Controlled = false;
        los = Physics.getLOS(x, y, angle);

        damage = DAMAGE;

        height = los.getLength();

        body = new Rectangle(WIDTH, los.getLength());
        body.setFill(Color.RED);

        Rotate r = new Rotate();
        r.setPivotX(WIDTH/2);
        r.setPivotY(0);
        r.setAngle(angle - 180);
        body.setTranslateX(x - WIDTH/2);
        body.setTranslateY(y);
        body.getTransforms().add(r);


        visuals.getChildren().add(body);

        type = Type.HitScan;
    }

    @Override
    public void update(){
        if(!alive) return;
        framesSpentAlive++;
        if(framesSpentAlive > HANGAROUNDTIME * Settings.getFramerate() / 1000){
            alive = false;
        }
    }

    @Override
    public void move(float time){
    }

    @Override
    public float checkCollisions(float time, List<ICollidable> obstacles){
        return time;
    }

    @Override
    public void checkHits(HashMap<String, Player> players){
        if(!p1Controlled || !active) return;
        for(Map.Entry<String, Player> entry: players.entrySet()){
            Player p = entry.getValue();
            if(p.getID().equals(ownerID)){
                continue;
            }

            if(Physics.checkLOS(los, p)){
                p.damage(damage);
                if(hitEventHandler != null){
                    hitEventHandler.handle(new HitEvent(p.getID(), (int)xpos, (int)ypos, damage ));
                }
                active = false;
            }
        }
    }
}
