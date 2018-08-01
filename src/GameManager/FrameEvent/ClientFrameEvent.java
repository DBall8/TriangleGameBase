package GameManager.FrameEvent;

import Objects.Entities.Player;
import org.json.JSONObject;

/**
 * Class for handling the state of a single client's players
 */
public class ClientFrameEvent implements IFrameEvent{

    private String ID; // the client's ID
    private float x, y, xvel, yvel, angle; // the client's player's status

    /**
     * Create a frame event from a player
     * @param p the player to track the status of
     */
    public ClientFrameEvent(Player p){
        this.ID = p.getID();
        this.x = p.getX();
        this.y = p.getY();
        this.xvel = p.getXVel();
        this.yvel = p.getYVel();
        this.angle = p.getAngle();
    }

    /**
     * Create a client frame from a message received
     * @param json the message received
     */
    public ClientFrameEvent(JSONObject json){
        this.ID = json.getString("ID");
        this.x = json.getFloat("X");
        this.y = json.getFloat("Y");
        this.xvel = json.getFloat("xvel");
        this.yvel = json.getFloat("yvel");
        this.angle = json.getFloat("angle");
    }

    // Conver the frame to a json object
    @Override
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

    // Getters

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
