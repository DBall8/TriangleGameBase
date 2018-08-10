package Objects.Entities.Projectiles;

import Objects.Entities.Player;
import Objects.Entities.Projectiles.Projectile;
import Physics.Physics;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BasicShot extends Projectile {

    private final static int DAMAGE = 1;

    private final static int WIDTH = 10;
    private final static int HEIGHT = 30;
    private final static float PVELOCITY = 10; // the base velocity of a projectile
    private final static float MOVEFACTOR = 0.9f; // the percentage of the player's speed to add to the projectile speed

    private Rectangle body;

    /**
     * Constructor for brand new projectile, only differs in that it creates its own ID
     * @param p the player who shot the projectile
     */
    public BasicShot(Player p){
        super("Proj-" + System.currentTimeMillis(), p.getID(), p.getX(), p.getY(), p.getAngle(), WIDTH, HEIGHT);

        damage = DAMAGE;
        type = Type.BasicShot;

        float pvel;
        if(p.getVelocity() > 0){
            pvel = p.getVelocity() * MOVEFACTOR + PVELOCITY;
        }
        else{
            pvel = PVELOCITY;
        }
        this.angle = p.getAngle();
        this.xvel = Physics.xComponent(pvel, Physics.toRadiians(angle));
        this.yvel = Physics.yComponent(pvel, Physics.toRadiians(angle));

        body = new Rectangle(WIDTH, HEIGHT);
        body.setFill(p.getColor());
        visuals.getChildren().add(body);

        p1Controlled = true;
    }

    /**
     * Constructor for a projectile that already has an ID
     * @param ID The ID of the projectile
     * @param ownerID the ID of the player who shot the projectile
     */
    public BasicShot(String ID, String ownerID, float x, float y, float xvel, float yvel, float angle){
        super(ID, ownerID, x, y, angle, WIDTH, HEIGHT);
        this.angle = angle;
        this.xvel = xvel;
        this.yvel = yvel;

        body = new Rectangle(WIDTH, HEIGHT);
        body.setFill(Color.RED);
        visuals.getChildren().add(body);

        p1Controlled = false;
    }

    /**
     * Updates the visuals of the projectile
     */
    @Override
    public void draw() {
        super.draw();
        if(alive) {
            body.setTranslateX(xpos - WIDTH/2);
            body.setTranslateY(ypos - HEIGHT/2);
            body.setRotate(angle);
        }
    }
}
