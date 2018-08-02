package Objects.Entities;

import Ability.Ability;
import Ability.PrimaryFire;
import Ability.Boost;
import Animation.HitAnimation;
import Global.Settings;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Physics.Physics;
import GameManager.UserInputListener;
import Objects.ICollidable;
import Visuals.HUD;
import Visuals.PlayerUI;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for a player entity
 */
public class Player extends Entity implements ICollidable {

    private final static int WIDTH = 40;
    private final static int HEIGHT = 50;

    public final static int MAXHEALTH = 10;

    private Polygon body; // The shape to use as the player's visual body
    private Rotate r; // the rotation property for rotating the visuals
    private UserInputListener input; // the object tracking user inputs

    private Rectangle debug; // a debug shape for showing the collision boundaries

    private PrimaryFire primaryFire; // the primary fire ability of the player
    private Boost boost;

    private List<Projectile> newShots = new ArrayList<>();

    private PlayerUI hud;
    private int health = MAXHEALTH;
    private Color color;

    Group visuals;

    // Constructor
    public Player(String ID, int x, int y){
        super(ID, x, y);
        angle = 0;
        xvel = 20;
        yvel = 20;
        width = WIDTH;
        height = HEIGHT;

        // Build a triangle from the player's dimensions
        body = new Polygon();
        body.getPoints().addAll(new Double[]{
                (double)width/2, 0.0,
                0.0, (double)height,
                (double)width, (double)height
        });
        body.setFill(Color.GREEN);
        boundingBox = new Rectangle(width, height);

        // Set the pivot point (likely not needed but might want to change later)
        r = new Rotate();
        r.setPivotX(width/2);
        r.setPivotY(height/2);

        body.getTransforms().add(r);

        visuals = new Group();
        visuals.getChildren().add(body);

        if(Settings.isDebug()) {
            debug = new Rectangle(1, 1);
            debug.setFill(Color.TRANSPARENT);
            debug.setStroke(Color.BLUE);
            visuals.getChildren().add(debug);
        }
    }

    /**
     * Makes this player the controllable player for this game
     * @param scene the scene the player is controlled in
     * @param feHandler the fire event handler for handling ability firing
     */
    public void initializeAsPlayer1(Scene scene, FireEventHandler feHandler){
        input = new UserInputListener(scene);
        primaryFire = new PrimaryFire(this, scene, feHandler);
        boost = new Boost(this, scene);
    }

    /**
     * Updates the player according to the current state of the user input
     */
    @Override
    public void update(){

        // If not user input object, there is nothing to update
        if(input == null){
            return;
        }

        // convert velocity to scalar
        float velocity = getVelocity();

        // Slow the player from "friction"
        float mag = Math.abs(velocity);
        if( mag > 0.5){
            velocity -= 0.5 * mag / velocity;
        }
        else{
            velocity = 0;
        }

        // Rotate the player towards the mouse

        // Get the angle the mouse pointer is from the player
        double mouseAngle =180/Math.PI * Physics.findAngle(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Get the amount to turn in
        double dAngle = 180/Math.PI *Math.sin((mouseAngle - angle)*Math.PI/180);

        angle += dAngle;

        // Update the player's velocity

        boolean boosting = boost.use();

        float distFromMouse = Physics.getDistance(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Only move if far enough from the mouse
        if(distFromMouse > getYRadius() * 1.5) {

            // Move forward if forward key is pressed and not at max speed, also accounting for boosting
            if (input.isUp()) {
                if (velocity < 10) {
                    velocity += 0.8;
                } else if (boosting && velocity < 20) {
                    velocity += 1;
                }

            }
            // Move backward if the back key is presed
            if (input.isDown() && velocity > -10) {
                velocity -= 0.8;
            }
        }

        // convert new velocity back to its components
        float angleRads = Physics.toRadiians(angle);
        xvel = Physics.xComponent(velocity, angleRads);
        yvel = Physics.yComponent(velocity, angleRads);

        // attempt to use all abilities
        primaryFire.use();

    }

    /**
     * Attaches a player HUD to this player
     */
    public void attachHUD(PlayerUI hud){
        this.hud = hud;
        if(boost != null){
            boost.attachUI(hud);
        }
        color = hud.getColor();
        body.setFill(color);
    }

    /**
     * Returns the visual object to display this player
     * @return the triangle fir displaying this player
     */
    @Override
    public Node getVisuals(){
        return visuals;
    }

    /**
     * Updates the visuals of this player
     */
    @Override
    public void draw(){
        // move to the correct position
        body.setTranslateX(xpos - getXRadius());
        body.setTranslateY(ypos - getYRadius());
        // rotate
        r.angleProperty().set(angle);

        if(Settings.isDebug()) {
            debug.setTranslateX(xpos - getXRadius());
            debug.setTranslateY(ypos - getYRadius());
            debug.setWidth(getXRadius() * 2);
            debug.setHeight(getYRadius() * 2);
        }
    }

    void damage(int amount){
        if(health > 0){
            health -= amount;
            new HitAnimation(visuals, (int)xpos, (int)ypos);
        }
        if(health < 0){
            health = 0;
        }

        if(hud != null){
            hud.notifyChanged(this);
        }
    }

    public void addNewShot(Projectile p){
        this.newShots.add(p);
    }

    public List<Projectile> getNewShots(){ return newShots; }

    public void clearNewShots(){ newShots.clear(); }

    // Getters

    @Override
    public float rightX() {
        return xpos + getXRadius();
    }

    @Override
    public float leftX() {
        return xpos - getXRadius();
    }

    @Override
    public float bottomY() {
        return ypos + getYRadius();
    }

    @Override
    public float topY() {
        return ypos - getYRadius();
    }

    public int getHealth(){ return this.health; }

    public PlayerUI getUI(){ return hud; }

    public Color getColor(){ return color; }

//    @Override
//    public float getXRadius(){
//        float radiians = Physics.toRadiians(angle);
//        float cos = (float)Math.cos(radiians);
//        float sin = (float)Math.sin(radiians);
//        return ((width * cos * cos) + (height * sin * sin))/2.0f;
//
//    }
//
//    @Override
//    public float getYRadius(){
//        float radiians = Physics.toRadiians(angle);
//        float cos = (float)Math.cos(radiians);
//        float sin = (float)Math.sin(radiians);
//        return ((width * sin * sin) + (height * cos * cos))/2.0f;
//    }
}
