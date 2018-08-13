package animation;

import ability.Cloak;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import objects.entities.Player;

public class InvisibleAnimation extends Animation {

    private boolean runOnce = true;
    private boolean isCloaking = true;

    public InvisibleAnimation(Player p, boolean cloaking) {
        super(0);

        isCloaking = cloaking;
        ownerID = p.getID();

        initialize(true, cloaking);
    }

    public InvisibleAnimation(String ownerID, boolean cloaking){
        super(0);
        this.ownerID = ownerID;
        isCloaking = cloaking;

        initialize(false, cloaking);
    }

    private void initialize(boolean isP1, boolean cloaking){
        type = Type.Invisibilty;

        eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(runOnce) {
                    if (cloaking) {
                        if (isP1) {
                            g.setOpacity(0.5);
                        } else {
                            g.setOpacity(0);
                        }
                    }
                    else{
                        g.setOpacity(1);
                    }

                }
            }
        };
    }

    public boolean isCloaking() {
        return isCloaking;
    }
}
