package GameManager.FrameEvent;

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

}
