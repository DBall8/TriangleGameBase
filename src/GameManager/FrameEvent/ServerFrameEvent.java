package GameManager.FrameEvent;

import Objects.Entities.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class ServerFrameEvent implements IFrameEvent {

    private JSONObject json;
    private ClientFrameEvent[] clientFrames;

    public ServerFrameEvent(Iterator it){

        json = new JSONObject();
        JSONArray playerArray = new JSONArray();
        while(it.hasNext()){
            Player p = (Player)((Map.Entry)it.next()).getValue();
            ClientFrameEvent clientFrame = new ClientFrameEvent(p);
            playerArray.put(clientFrame.toJSON());
        }

        json.put("players", playerArray);
    }

    public ServerFrameEvent(JSONObject json){
        JSONArray playerArray = json.getJSONArray("players");
        clientFrames = new ClientFrameEvent[playerArray.length()];
        for(int i=0; i<playerArray.length(); i++){
            JSONObject playerJson = (JSONObject)playerArray.get(i);
            clientFrames[i] = new ClientFrameEvent(playerJson);
        }
    }

    public ClientFrameEvent[] getClientFrames(){ return clientFrames; }

    @Override
    public JSONObject toJSON() {
        return json;
    }
}
