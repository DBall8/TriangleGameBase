package GameManager.FrameEvent;

import Objects.Entities.Player;
import org.json.JSONObject;

public class FrameEvent {

    private String ID;
    private float x, y, xvel, yvel, angle;

    public FrameEvent(Player p){
        this.ID = p.getID();
        this.x = p.getX();
        this.y = p.getY();
        this.xvel = p.getXVel();
        this.yvel = p.getYVel();
        this.angle = p.getAngle();
    }

    public FrameEvent(JSONObject json){
        this.ID = json.getString("ID");
        this.x = json.getFloat("X");
        this.y = json.getFloat("Y");
        this.xvel = json.getFloat("xvel");
        this.yvel = json.getFloat("yvel");
        this.angle = json.getFloat("angle");
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("ID", ID);
        json.put("X", x);
        json.put("Y", y);
        json.put("xvel", xvel);
        json.put("yvel", yvel);
        json.put("angle", angle);

        return json;
    }

    public float getX() { return x; }

    public float getY() {
        return y;
    }

    public String getID(){ return ID; }

    public float getXvel() {
        return xvel;
    }

    public float getYvel() {
        return yvel;
    }

    public float getAngle() {
        return angle;
    }
}
