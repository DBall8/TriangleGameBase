package Events.FrameEvent;

import Events.HitEvent;
import Objects.Entities.Projectile;
import org.json.JSONObject;

public abstract class FrameEvent {
    public abstract JSONObject toJSON();

    protected JSONObject convertProjectileToJSON(Projectile p){
        JSONObject json = new JSONObject();
        json.put("ID", p.getID());
        json.put("X", p.getX());
        json.put("Y", p.getY());
        json.put("xvel", p.getXVel());
        json.put("yvel", p.getYVel());
        json.put("angle", p.getAngle());
        return json;
    }

    protected Projectile convertJSONtoProjectile(JSONObject json, String ownerID){
        String ID;
        float x, y, xvel, yvel, angle;
        ID = json.getString("ID");
        x = json.getFloat("X");
        y = json.getFloat("Y");
        xvel = json.getFloat("xvel");
        yvel = json.getFloat("yvel");
        angle = json.getFloat("angle");

        return new Projectile(ID, ownerID, x, y, xvel, yvel, angle);

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

}
