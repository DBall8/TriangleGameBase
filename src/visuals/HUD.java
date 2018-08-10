package visuals;

import global.Settings;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import objects.entities.Player;

public class HUD extends Group {

    final static int COLUMNSPACE = 40;
    final static int ROWSPACE = 40;

    final static int WINNERFONTSIZE = 40;

    private final static int MAXPLAYERS = 4;

    private PlayerUI[] playerUIs = new PlayerUI[]{null, null, null, null};
    private Text winnerText;

    public HUD(){
        super();
        winnerText = new Text();
        winnerText.setFill(Color.TRANSPARENT);
        winnerText.setFont(new Font(WINNERFONTSIZE));
        winnerText.setTranslateX(Settings.getWindowWidth() / 2 - 100);
        winnerText.setTranslateY(Settings.getWindowWidth() / 2);
        getChildren().add(winnerText);
    }

    public PlayerUI addNewPlayerUI(Player p){

        short index = availableIndex();
        if(index < 0 || index >= MAXPLAYERS){
            return null;
        }

        PlayerUI newUI = new PlayerUI(index);
        p.attachHUD(newUI);

        playerUIs[index] = newUI;

        switch (index){
            case 0:
                newUI.setTranslateX(COLUMNSPACE);
                newUI.setTranslateY(ROWSPACE);
                break;
            case 1:
                newUI.setTranslateX(Settings.getWindowWidth() - newUI.totalWidth() - COLUMNSPACE);
                newUI.setTranslateY(ROWSPACE);
                break;
            case 2:
                newUI.setTranslateX(COLUMNSPACE);
                newUI.setTranslateY(Settings.getWindowHeight() - newUI.totalHeight() - ROWSPACE);
                break;
            case 3:
                newUI.setTranslateX(Settings.getWindowWidth() - newUI.totalWidth() - COLUMNSPACE);
                newUI.setTranslateY(Settings.getWindowHeight() - newUI.totalHeight() - ROWSPACE);
                break;
            default:
        }

        getChildren().add(newUI);

        return newUI;
    }

    public void removePlayerUI(PlayerUI ui){
        for(int i=0; i<MAXPLAYERS; i++){
            if(playerUIs[i] != null && playerUIs[i].equals(ui)){
                playerUIs[i] = null;
                getChildren().remove(ui);
                return;
            }
        }
    }

    private short availableIndex(){
        for(short i=0; i<MAXPLAYERS; i++){
            if(playerUIs[i] == null){
                return i;
            }
        }

        return -1;
    }

    public void displayWinner(short pnum, Color color){
        winnerText.setText("Player " + (pnum+1) + " wins!");
        winnerText.setFill(color);
    }

    public void removeWinner(){
        winnerText.setFill(Color.TRANSPARENT);
    }
}
