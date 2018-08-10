package events.frameEvent;

import events.HitEvent;
import objects.entities.projectiles.BasicShot;
import objects.entities.projectiles.HitScan;
import objects.entities.projectiles.Projectile;
import animation.Animation;
import animation.HitAnimation;
import org.json.JSONObject;

public abstract class FrameEvent {
    public abstract JSONObject toJSON();

    protected JSONObject convertProjectileToJSON(Projectile p){
        JSONObject json = new JSONObject();

        json.put("ID", p.getID());
        json.put("type", p.getType());
        json.put("X", p.getX());
        json.put("Y", p.getY());
        json.put("angle", p.getAngle());

        switch (p.getType()){
            case HitScan:
                break;
            case BasicShot: default:
                json.put("xvel", p.getXVel());
                json.put("yvel", p.getYVel());
                break;
        }
        return json;
    }

    protected Projectile convertJSONtoProjectile(JSONObject json, String ownerID){
        String ID;
        float x, y, angle;
        Projectile.Type type;

        ID = json.getString("ID");
        type = json.getEnum(Projectile.Type.class, "type");
        x = json.getFloat("X");
        y = json.getFloat("Y");
        angle = json.getFloat("angle");

        switch(type){
            case HitScan:
                return new HitScan(ID, ownerID, x, y, angle);
            case BasicShot: default:
                float xvel = json.getFloat("xvel");
                float yvel = json.getFloat("yvel");
                return new BasicShot(ID, ownerID, x, y, xvel, yvel, angle);
        }




    }

    protected JSONObject convertHitToJSON(HitEvent p){
        JSONObject json = new JSONObject();
        json.put("ID", p.getPlayerID());
        json.put("X", p.getX());
        json.put("Y", p.getY());
        json.put("damage", p.getDamage());
        return json;
    }

    protected HitEvent convertJSONtoHit(JSONObject json){
        String playerID;
        int x, y, damage;
        playerID = json.getString("ID");
        x = json.getInt("X");
        y = json.getInt("Y");
        damage = json.getInt("damage");

        return new HitEvent(playerID, x, y, damage);

    }

    protected JSONObject convertAnimationToJSON(Animation a){
        JSONObject json = new JSONObject();

        return json;
    }

    protected Animation convertJSONtoAnimation(JSONObject json){
        return new HitAnimation(100, 100);
    }

}
