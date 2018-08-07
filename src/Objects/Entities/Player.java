package Objects.Entities;

import Ability.PrimaryFire;
import Ability.Boost;
import Animation.HitAnimation;
import Events.EventHandler;
import Events.FireEvent;
import GameManager.UserInputHandler.UserInputHandler;
import Global.Settings;
import Physics.Physics;
import GameManager.UserInputHandler.UserInputHandler.Binding;
import Objects.ICollidable;
import Visuals.PlayerUI;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;


/**
 * A class for a player entity
 */
public class Player extends Entity implements ICollidable {

    private final static int WIDTH = 40;
    private final static int HEIGHT = 50;

    private final static float MAXSPEED = 10;
    private final static float MAXBOOSTSPEED = 20;
    private final static float ACCEL = 0.8f;
    private final static float RACCEL = 5;

    public final static int MAXHEALTH = 10;

    private float velocity;

    private Polygon body; // The shape to use as the player's visual body
    private Rotate r; // the rotation property for rotating the visuals
    private UserInputHandler input; // the object tracking user inputs

    private PrimaryFire primaryFire; // the primary fire ability of the player
    private Boost boost;

    private List<Projectile> newShots = new ArrayList<>(); // all new shots that the server has not been made aware of

    private PlayerUI hud; // The UI for this player
    private int health = MAXHEALTH; // the health points of the player
    private Color color; // the color of the player

    // Constructor
    public Player(String ID, int x, int y){
        super(ID, x, y, WIDTH, HEIGHT);
        angle = 0;
        xvel = 20;
        yvel = 20;
        velocity = 0;

        // Build a triangle from the player's dimensions
        body = new Polygon();
        body.getPoints().addAll(new Double[]{
                (double)width/2, 0.0,
                0.0, (double)height,
                (double)width, (double)height
        });
        body.setFill(Color.GREEN);

        // Set the pivot point (likely not needed but might want to change later)
        r = new Rotate();
        r.setPivotX(width/2);
        r.setPivotY(height/2);

        body.getTransforms().add(r);
        visuals.getChildren().add(body);
    }

    /**
     * Makes this player the controllable player for this game
     * @param scene the scene the player is controlled in
     * @param feHandler the fire event handler for handling ability firing
     */
    public void initializeAsPlayer1(Scene scene, EventHandler<FireEvent> feHandler){
        input = Settings.setUserInput(scene);
        primaryFire = new PrimaryFire(this, Binding.SHOOT, feHandler);
        boost = new Boost(this, Binding.BOOST);
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

        // Slow the player from "friction"
        float mag = Math.abs(velocity);
        if( mag > 0.5){
            velocity -= 0.5 * mag / velocity;
        }
        else{
            velocity = 0;
        }

        // Dont show any signs of life if dead, so stop here
        if(health <=0){
            return;
        }

        // Rotate the player towards the mouse

        /*
        // Get the angle the mouse pointer is from the player
        double mouseAngle =180/Math.PI * Physics.findAngle(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Get the amount to turn in
        double dAngle = 180/Math.PI *Math.sin((mouseAngle - angle)*Math.PI/180);
        */

        // Change in angle
        float dAngle = 0;
        if(input.isPressed(Binding.RIGHT)){
            dAngle += RACCEL;
        }
        if(input.isPressed(Binding.LEFT))
            dAngle -= RACCEL;

        angle += dAngle;
        if(angle > 360){
            angle -= 360;
        }
        else if(angle < 0){
            angle += 360;
        }

        // Update the player's velocity

        boolean boosting = boost.use();

        /*
        float distFromMouse = 100; //Physics.getDistance(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Only move if far enough from the mouse
        if(distFromMouse > getYRadius() * 1.5) {
        */
        // Move forward if forward key is pressed and not at max speed, also accounting for boosting
        if (input.isPressed(Binding.UP)) {
            if (velocity < MAXSPEED) {
                velocity += ACCEL;
            } else if (boosting && velocity < MAXBOOSTSPEED) {
                velocity += ACCEL;
            }

        }
        // Move backward if the back key is pressed
        if (input.isPressed(Binding.DOWN) && velocity > -MAXSPEED/2) {
            velocity -= ACCEL;
        }
        //}

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
            hud.setControlled();
        }
        color = hud.getColor();
        body.setFill(color);
    }

    /**
     * Updates the visuals of this player
     */
    @Override
    public void draw(){
        super.draw();

        // move to the correct position
        body.setTranslateX(xpos - WIDTH/2);
        body.setTranslateY(ypos - HEIGHT/2);
        // rotate
        r.angleProperty().set(angle);


    }

    public void damage(int amount, int x, int y){
        if(health > 0){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    new HitAnimation(visuals, x, y);
                }
            });

        }
        if(health < 0){
            health = 0;
        }

        updateHealth(health - amount);

    }

    public void updateState(float x, float y, float xvel, float yvel, float angle, int health){
        super.updateState(x, y, xvel, yvel, angle);
        updateHealth(health);
    }

    public void updateHealth(int health){
        if(this.health != health) {
            this.health = health;
            if (hud != null) {
                hud.notifyChanged(health);
            }

            if(health <= 0){
                body.setFill(Color.GRAY);
            }
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

    public UserInputHandler getInputHandler(){ return this.input; }

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
