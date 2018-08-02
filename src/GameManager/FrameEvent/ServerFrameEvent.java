package GameManager.FrameEvent;

import Objects.Entities.Player;
import Objects.Entities.Projectile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class for containing the status of every player in a server's game simulation
 */
public class ServerFrameEvent extends FrameEvent {

    private JSONObject json; // server's status as a json
    private ClientFrameEvent[] clientFrames; // an array of each player's status
    private Projectile[] newProjectiles;
    private List<String> disconnectedIDs = new ArrayList<>();

    /**
     * Creates a server frame event from a players map iterator
     * @param it the iterator from the players hashmap
     */
    public ServerFrameEvent(Iterator it){
        // Create a json object with an array of all player statuses
        json = new JSONObject();
        JSONArray playerArray = new JSONArray();
        while(it.hasNext()){
            Player p = (Player)((Map.Entry)it.next()).getValue();
            ClientFrameEvent clientFrame = new ClientFrameEvent(p);
            playerArray.put(clientFrame.toJSON());
        }

        json.put("players", playerArray);
    }

    /**
     * Create a list of player statuses from a message received
     * @param json the message received
     */
    public ServerFrameEvent(JSONObject json){
        JSONArray playerArray = json.getJSONArray("players");
        clientFrames = new ClientFrameEvent[playerArray.length()];
        for(int i=0; i<playerArray.length(); i++){
            JSONObject playerJson = (JSONObject)playerArray.get(i);
            clientFrames[i] = new ClientFrameEvent(playerJson);
        }

        if(json.has("disconnects")){
            JSONArray disconnIDs = json.getJSONArray("disconnects");
            for(Object id: disconnIDs){
                disconnectedIDs.add((String)id);
            }
        }
    }

    public ClientFrameEvent[] getClientFrames(){ return clientFrames; }
    public List<String> getDisconnectedIDs(){ return disconnectedIDs; }

    @Override
    public JSONObject toJSON() {
        return json;
    }
}
