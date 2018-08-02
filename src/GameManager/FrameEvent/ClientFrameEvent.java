package GameManager.FrameEvent;

import Objects.Entities.Player;
import Objects.Entities.Projectile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Class for handling the state of a single client's players
 */
public class ClientFrameEvent extends FrameEvent {

    private String ID; // the client's ID
    private float x, y, xvel, yvel, angle; // the client's player's status
    private Projectile[] newProjectiles;

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

        if(p.getNewShots().size() > 0){
            this.newProjectiles = new Projectile[p.getNewShots().size()];
            for(int i = 0; i<this.newProjectiles.length; i++){
                this.newProjectiles[i] = p.getNewShots().get(i);
            }
            p.clearNewShots();
        }
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

        if(json.has("newProjectiles")){
            JSONArray newP = json.getJSONArray("newProjectiles");
            newProjectiles = new Projectile[newP.length()];
            for(int i=0; i<newP.length(); i++){
                newProjectiles[i] = convertJSONtoProjectile((JSONObject)newP.get(i), ID);
            }
        }
    }

    // Convert the frame to a json object
    @Override
    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("ID", ID);
        json.put("X", x);
        json.put("Y", y);
        json.put("xvel", xvel);
        json.put("yvel", yvel);
        json.put("angle", angle);

        if(newProjectiles != null && newProjectiles.length > 0){
            JSONArray newP = new JSONArray();
            for(Projectile p: newProjectiles){
                JSONObject pJSON = convertProjectileToJSON(p);
                newP.put(pJSON);
            }
            json.put("newProjectiles", newP);
        }

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

    public Projectile[] getNewProjectiles() { return newProjectiles; }
}
