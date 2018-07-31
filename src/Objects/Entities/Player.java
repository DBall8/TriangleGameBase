package Objects.Entities;

import Ability.Ability;
import Ability.PrimaryFire;
import Global.Settings;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Physics.Physics;
import GameManager.UserInputListener;
import Objects.ICollidable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Player extends Entity implements ICollidable {

    private Polygon body;
    private Rotate r;
    private UserInputListener input;

    private Rectangle debug;

    private Ability primaryFire;

    public Player(int x, int y){
        super(x, y);
        angle = 0;
        xvel = 20;
        yvel = 20;
        width = 40;
        height = 50;

        body = new Polygon();
        body.getPoints().addAll(new Double[]{
                (double)width/2, 0.0,
                0.0, (double)height,
                (double)width, (double)height
        });
        body.setFill(Color.GREEN);
        boundingBox = new Rectangle(width, height);

        r = new Rotate();
        r.setPivotX(width/2);
        r.setPivotY(height/2);

        body.getTransforms().add(r);

        if(Settings.isDebug()) {
            debug = new Rectangle(1, 1);
            debug.setFill(Color.TRANSPARENT);
            debug.setStroke(Color.BLUE);
        }
    }

    public void initializeAsPlayer1(Scene scene, FireEventHandler feHandler){
        input = new UserInputListener(scene);
        primaryFire = new PrimaryFire(this, scene, feHandler);
    }

    @Override
    public void update(){

        if(input == null){
            return;
        }

        float velocity = getVelocity();

        // Slow

        float mag = Math.abs(velocity);
        if( mag > 0.5){
            velocity -= 0.5 * mag / velocity;
        }
        else{
            velocity = 0;
        }

        // Rotate

        // Get the angle the mouse pointer is from the player
        double mouseAngle =180/Math.PI * Physics.findAngle(xpos, ypos, input.getMouseX(), input.getMouseY());
        // Get the amount to turn in
        double test = 180/Math.PI *Math.sin((mouseAngle - angle)*Math.PI/180);

        angle += test;

        // Move

        float distFromMouse = Physics.getDistance(xpos, ypos, input.getMouseX(), input.getMouseY());
        if(distFromMouse > getYRadius() * 1.5) {
            if (input.isUp()) {
                if (velocity < 10) {
                    velocity += 0.8;
                } else if (input.isBoost() && velocity < 20) {
                    velocity += 1;
                }

            }
            if (input.isDown() && velocity > -10) {
                velocity -= 0.8;
            }
        }


        float angleRads = Physics.toRadiians(angle);
        xvel = Physics.xComponent(velocity, angleRads);
        yvel = Physics.yComponent(velocity, angleRads);

        // Abilities
        primaryFire.use();

    }

    @Override
    public Node getVisuals(){
        Group g = new Group();
        g.getChildren().add(body);
        if(Settings.isDebug()){
            g.getChildren().add(debug);
        }
        return g;
    }

    @Override
    public void draw(){
        body.setTranslateX(xpos - getXRadius());
        body.setTranslateY(ypos - getYRadius());
        r.angleProperty().set(angle);

        if(Settings.isDebug()) {
            debug.setTranslateX(xpos - getXRadius());
            debug.setTranslateY(ypos - getYRadius());
            debug.setWidth(getXRadius() * 2);
            debug.setHeight(getYRadius() * 2);
        }
    }

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
