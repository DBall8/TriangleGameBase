package Objects.Entities;

import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Physics.Physics;
import GameManager.UserInputListener;
import Objects.ICollidable;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Player extends Entity implements ICollidable {

    private Polygon body;
    private UserInputListener input;

    private FireEventHandler feHandler;

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
    }

    public void setInputListener(UserInputListener input){
        this.input = input;
    }

    @Override
    public void update(){

        float velocity = (float)Math.sqrt((xvel*xvel) + (yvel * yvel));

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

//        if(test < 90 && test > -90) {
//            if(input.isBoost()){
//                velocity = (float) (velocity * ((180 - (Math.abs(test)))/ 180));
//            }
//            else{
//                velocity = (float) (velocity * ((90 - (Math.abs(test)))/ 90));
//            }
//
//        }
//        else
//            velocity = 0;



        if(input.isUp()){
            if(velocity < 10 ){
                velocity += 0.8;
            }
            else if(input.isBoost() && velocity < 20){
                velocity += 1;
            }

        }
        if(input.isDown()  && velocity > -10){
            velocity -= 0.8;
        }
        //System.out.println(velocity);
        float angleRads = Physics.toRadiians(angle);
        xvel = Physics.xComponent(velocity, angleRads);
        yvel = Physics.yComponent(velocity, angleRads);

        // Fire
        if(input.isMouseDown()){
            feHandler.handle(new FireEvent(new Projectile(xpos, ypos, velocity + 2, angle, this)));
        }

    }

    public void addFireEventHandler(FireEventHandler feHandler){
        this.feHandler = feHandler;
    }

    @Override
    public Node getVisuals(){ return body; }

    @Override
    public void draw(){
        body.setTranslateX(xpos - getXRadius());
        body.setTranslateY(ypos - getYRadius());
        body.setRotate(angle);
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
}
