package visuals;

import ability.Ability;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class AbilityCooldownUI extends Group {

    final static int WIDTH = 40;
    final static int HEIGHT = 40;
    final static int FONTSIZE = 20;

    private Text cooldownText;

    private Timer timer;

    public AbilityCooldownUI(Ability a){
        super();

        if(a != null)
            a.attachUI(this);

        Rectangle back = new Rectangle(WIDTH, HEIGHT);
        back.setFill(Color.GRAY);

        cooldownText = new Text("");
        cooldownText.setFont(new Font(FONTSIZE));
        cooldownText.setFill(Color.WHITE);
        cooldownText.setTranslateX(15);
        cooldownText.setTranslateY(FONTSIZE + 5);

        getChildren().addAll(back, cooldownText);
    }

    public void goOnCooldown(int cooldown){
        int secondsLeft = cooldown;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cooldownText.setText(Integer.toString(cooldown));
            }
        });

        scheduleTimerTick(cooldown-1);

    }

    private void scheduleTimerTick(int secondsLeft){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(secondsLeft == 0){
                    cooldownText.setText("");
                    return;
                }
                cooldownText.setText(Integer.toString(secondsLeft));
                if(secondsLeft > 0){
                    scheduleTimerTick(secondsLeft-1);
                }
            }
        }, 1000 /*1 seconds */);
    }
}
