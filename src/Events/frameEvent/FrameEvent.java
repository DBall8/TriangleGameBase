package events.frameEvent;

import animation.InvisibleAnimation;
import animation.SniperAnimation;
import events.HitEvent;
import objects.entities.projectiles.*;
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

        switch (p.getType()){
            case Explosion:
                break;
            case Rocket:
            case BasicShot: default:
                json.put("xvel", p.getXVel());
                json.put("yvel", p.getYVel());
            case HitScan:
                json.put("angle", p.getAngle());
                break;

        }
        return json;
    }

    protected Projectile convertJSONtoProjectile(JSONObject json, String ownerID){
        String ID;
        float x, y, angle, xvel, yvel;
        Projectile.Type type;

        ID = json.getString("ID");
        type = json.getEnum(Projectile.Type.class, "type");
        x = json.getFloat("X");
        y = json.getFloat("Y");

        switch(type){
            case Explosion:
                return new Explosion(ID, ownerID, x, y);
            case HitScan:
                angle = json.getFloat("angle");
                return new HitScan(ID, ownerID, x, y, angle);
            case Rocket:
                angle = json.getFloat("angle");
                xvel = json.getFloat("xvel");
                yvel = json.getFloat("yvel");
                return new Rocket(ID, ownerID, x, y, xvel, yvel, angle);
            case BasicShot: default:
                angle = json.getFloat("angle");
                xvel = json.getFloat("xvel");
                yvel = json.getFloat("yvel");
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

    public JSONObject convertAnimationToJSON(Animation a){
        JSONObject json = new JSONObject();
        json.put("type", a.getType());
        switch(a.getType()){
            case Invisibilty:
                json.put("cloak", ((InvisibleAnimation)a).isCloaking());
            case SniperAnimation:
                json.put("ownerID", a.getOwnerID());
                break;
            default:
        }
        return json;
    }

    protected Animation convertJSONtoAnimation(JSONObject json){
        Animation.Type type = json.getEnum(Animation.Type.class, "type");

        switch (type){
            case SniperAnimation:
                return new SniperAnimation(json.getString("ownerID"));
            case Invisibilty:
                return new InvisibleAnimation(json.getString("ownerID"), json.getBoolean("cloak"));
            default:
                return new HitAnimation(100, 100, 1);
        }
    }

}
