package Objects.Entities;

import Ability.*;
import Animation.Animation;
import Events.EventHandler;
import Events.FireEvent;
import GameManager.UserInputHandler.UserInputHandler;
import Global.Settings;
import Physics.Physics;
import GameManager.UserInputHandler.UserInputHandler.Binding;
import Objects.ICollidable;
import Visuals.Aimer;
import Visuals.PlayerUI;
import javafx.application.Platform;
import javafx.scene.Group;
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

    public final static int WIDTH = 40;
    public final static int HEIGHT = 50;

    private final static float MAXSPEED = 10;
    private final static float MAXBOOSTSPEED = 20;
    private final static float ACCEL = 0.8f;
    private final static float RACCEL = 4;
    private final static float BOOSTRACCEL = 6;
    private final static float AIMRACCEL = 2;


    public final static int MAXHEALTH = 10;

    private float velocity;

    private Group bodyGroup;
    private Polygon body; // The shape to use as the player's visual body
    private Aimer aimer;
    private Rotate r; // the rotation property for rotating the visuals
    private UserInputHandler input; // the object tracking user inputs

    private PrimaryFire primaryFire; // the primary fire ability of the player
    private Boost boost;
    private Ability ability1;
    private Ability ability2;

    private boolean moveDisabled = false;

    private List<Projectile> newShots = new ArrayList<>(); // all new shots that the server has not been made aware of

    private PlayerUI hud; // The UI for this player
    private int health = MAXHEALTH; // the health points of the player
    private Color color; // the color of the player

    private int spawnx, spawny;
    private float spawnAngle;

    // Constructor
    public Player(String ID, int x, int y, float angle){
        super(ID, x, y, WIDTH, HEIGHT);
        spawnx = x;
        spawny = y;
        spawnAngle = angle;
        this.angle = angle;
        xvel = 20;
        yvel = 20;
        velocity = 0;

        // Build a triangle from the player's dimensions
        bodyGroup = new Group();
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

        bodyGroup.getChildren().add(body);
        bodyGroup.getTransforms().add(r);
        visuals.getChildren().add(bodyGroup);
    }

    /**
     * Makes this player the controllable player for this game
     * @param scene the scene the player is controlled in
     * @param feHandler the fire event handler for handling ability firing
     */
    public void initializeAsPlayer1(Scene scene, EventHandler<FireEvent> feHandler){
        input = Settings.initializeUserInput(scene);
        primaryFire = new PrimaryFire(this, feHandler);
        boost = new Boost(this);
        ability1 = new Sniper(this);
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

        boolean boosting = boost.use();

        // Rotate the player towards the mouse

        /*
        // Get the angle the mouse pointer is from the player
        double mouseAngle =180/Math.PI * Physics.findAngle(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Get the amount to turn in
        double dAngle = 180/Math.PI *Math.sin((mouseAngle - angle)*Math.PI/180);
        */

        // Change in angle
        float dAngle = 0;
        if (input.isPressed(Binding.AIM)) {
            dAngle = AIMRACCEL;
        } else if(boosting){
            dAngle = BOOSTRACCEL;
        }
        else{
            dAngle = RACCEL;
        }
        if(input.isPressed(Binding.RIGHT)){
            angle += dAngle;
        }
        if(input.isPressed(Binding.LEFT)) {
            angle -= dAngle;
        }

        if(angle > 360){
            angle -= 360;
        }
        else if(angle < 0){
            angle += 360;
        }

        // Update the player's velocity

        /*
        float distFromMouse = 100; //Physics.getDistance(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Only move if far enough from the mouse
        if(distFromMouse > getYRadius() * 1.5) {
        */
        // Move forward if forward key is pressed and not at max speed, also accounting for boosting
        if ((input.isPressed(Binding.UP) || boosting) && !moveDisabled) {
            if (velocity < MAXSPEED) {
                velocity += ACCEL;
            } else if (boosting && velocity < MAXBOOSTSPEED) {
                velocity += ACCEL;
            }

        }
        // Move backward if the back key is pressed
        if (input.isPressed(Binding.DOWN) && velocity > -MAXSPEED/2 && !moveDisabled) {
            velocity -= ACCEL;
        }
        //}

        // convert new velocity back to its components
        float angleRads = Physics.toRadiians(angle);
        xvel = Physics.xComponent(velocity, angleRads);
        yvel = Physics.yComponent(velocity, angleRads);

        // attempt to use all abilities
        primaryFire.use();
        ability1.use();

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
        aimer = new Aimer(color);
        bodyGroup.getChildren().add(aimer);
        body.setFill(color);
    }

    /**
     * Updates the visuals of this player
     */
    @Override
    public void draw(){
        super.draw();

        // move to the correct position
        bodyGroup.setTranslateX(xpos - WIDTH/2);
        bodyGroup.setTranslateY(ypos - HEIGHT/2);
        // rotate
        r.angleProperty().set(angle);

        if(aimer == null || input == null){
            return;
        }
        if(input.isPressed(Binding.AIM)){
            aimer.show();
        } else{
            aimer.hide();
        }


    }

    public void damage(int amount){
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
            if(health < 0){
                health = 0;
            }
            if(health <= 0){
                body.setFill(Color.GRAY);
            }
        }
    }

    public void setSpawn(int x, int y, float angle){
        spawnx = x;
        spawny = y;
        spawnAngle = angle;
    }

    public void revive(){
        //xpos = spawnx;
        //ypos = spawny;
        health = MAXHEALTH;
        //velocity = xvel = yvel = 0;
        //angle = spawnAngle;
        body.setFill(hud.getColor());
        hud.notifyChanged(health);
    }

    public void addAnimation(Animation a, boolean attachedToPlayer){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(attachedToPlayer){
                    a.start(bodyGroup);
                } else{
                    a.start(visuals);
                }

            }
        });
    }

    public void addNewShot(Projectile p){
        this.newShots.add(p);
    }

    public List<Projectile> getNewShots(){ return newShots; }

    public void clearNewShots(){ newShots.clear(); }

    public void disableMovement(){ moveDisabled = true; }
    public void enableMovement(){ moveDisabled = false; }

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

    public float getVelocity(){ return velocity; }

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
