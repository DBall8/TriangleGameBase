package Visuals;

import Objects.Entities.Player;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PlayerUI extends Group{

    final static int HEALTHBARWIDTH = 200;
    final static int HEALTHBARHEIGHT = 10;
    final static int HEALTHBARBORDER = 5;
    final static int BOOSTBARHEIGHT = 5;
    final static int SPACING = 10;
    final static int FONTSIZE = 30;

    private Text playerLabel;
    private Rectangle healthBar;
    private Rectangle boostMeter;

    private Color color;
    private short pnum;

    public PlayerUI(short pnum){
        super();

        this.pnum = pnum;
        switch (pnum){
            case 0:
                color = Color.LIGHTBLUE;
                break;
            case 1:
                color = Color.ORANGE;
                break;
            case 2:
                color= Color.GREENYELLOW;
                break;
            case 3:
                color = Color.LIGHTCORAL;
                break;
            default:

        }

        playerLabel = new Text("Player " + (pnum + 1));
        playerLabel.setFont(new Font(FONTSIZE));
        playerLabel.setFill(color);

        Rectangle healthBarBG = new Rectangle(2*HEALTHBARBORDER + HEALTHBARWIDTH, 2*HEALTHBARBORDER + HEALTHBARHEIGHT);
        healthBarBG.setFill(Color.GRAY);
        healthBarBG.setTranslateY(SPACING);

        healthBar = new Rectangle(HEALTHBARWIDTH, HEALTHBARHEIGHT);
        healthBar.setFill(Color.GREEN);
        healthBar.setTranslateY(SPACING + HEALTHBARBORDER);
        healthBar.setTranslateX(HEALTHBARBORDER);

        boostMeter = new Rectangle(2*HEALTHBARBORDER + HEALTHBARWIDTH, BOOSTBARHEIGHT);
        boostMeter.setFill(Color.CYAN);
        boostMeter.setTranslateY(2*SPACING + (2*HEALTHBARBORDER) + HEALTHBARHEIGHT);

        getChildren().addAll(playerLabel, healthBarBG, healthBar);
    }

    public void notifyChanged(int health) {
        double healthPercentage = (double)health / (double)Player.MAXHEALTH;

        if(healthPercentage > 0.5){
            healthBar.setFill(Color.GREEN);
        }
        else if(healthPercentage > 0.25){
            healthBar.setFill(Color.YELLOW);
        }
        else{
            healthBar.setFill(Color.RED);
        }
        healthBar.setWidth(HEALTHBARWIDTH * healthPercentage);
    }

    public void notifyBoostChanged(double percent){
        boostMeter.setWidth(totalWidth() * percent);
    }

    public void setControlled(){
        getChildren().add(boostMeter);
    }

    int totalWidth(){
        return 2*HEALTHBARBORDER + HEALTHBARWIDTH;
    }

    int totalHeight(){
        return (FONTSIZE/2) + SPACING + (2*HEALTHBARBORDER) + HEALTHBARHEIGHT + SPACING + BOOSTBARHEIGHT + SPACING;
    }

    public Color getColor(){ return color; }
    public short getPnum(){ return pnum; }
}
