package GameManager;

import Global.Settings;
import Objects.Entities.Entity;
import Objects.Entities.Player;
import Objects.Entities.Projectile;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Objects.ICollidable;
import Objects.Obstacle;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager extends Pane {

    private Scene scene;
    private List<Entity> entities = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<Projectile> fireQueue = new ArrayList<>();
    private List<ICollidable> obstacles = new ArrayList<>();
    private int width, height;
    private GameTime time;

    public GameManager(){
        super();
        this.width = Settings.getWindowWidth();
        this.height = Settings.getWindowHeight();

        time = new GameTime(this);
    }

    public void start(Scene scene){
        this.scene = scene;
        Player p = new Player(50, 50);
        p.setInputListener(new UserInputListener(scene));
        p.addFireEventHandler(new FireEventHandler() {
            @Override
            public void handle(FireEvent fe) {
                // Add bullet here
                fireQueue.add(fe.projectile);
            }
        });
        addEntity(p);
        obstacles.add(p);
        addObstacle(375, 200, 50, 400);
        addObstacle(200, 375, 400, 50);
        time.play();
    }

    public void draw(){
        for(Entity e: entities){
            e.draw();
        }
    }

    private void update(){
        for(Entity e: entities){
            e.update();
        }
        Iterator it = fireQueue.iterator();
        while(it.hasNext()){
            Projectile p = (Projectile)it.next();
            addProjectile(p);
            it.remove();
        }
        it = projectiles.iterator();
        while(it.hasNext()){
            Projectile p = (Projectile)it.next();
            if(!p.isAlive()){
                it.remove();
                removeEntity(p);
            }
        }
    }

    private float checkCollisions(float timeLeft){
        float firstCollisionTime = timeLeft; // looks for first collision
        float tempTime;
        // reset collisions for each player
        for(Entity e: entities){
            e.reset();
            if((tempTime = e.checkCollisions(timeLeft, obstacles)) < firstCollisionTime){
                firstCollisionTime = tempTime;
            }
        }
        return firstCollisionTime;
    }

    private void move(float time){
        for(Entity e: entities){
            e.move(time);
        }
    }


    public void calculateFrame(){
        float timeLeft = 1.00f;
        float firstCollisionTime;
        update();

        do {
            // Check if each particle hits the box boundaries (must be done first as it resets collision)
            firstCollisionTime = checkCollisions(timeLeft);

            move(firstCollisionTime);

            timeLeft -= firstCollisionTime;

        }while(timeLeft > 0.01f);
    }

    private void addProjectile(Projectile p){
        addEntity(p);
        projectiles.add(p);
    }

    private void addEntity(Entity e){
        entities.add(e);
        this.getChildren().add(e.getVisuals());
    }

    private void removeEntity(Entity e){
        entities.remove(e);
        this.getChildren().remove(e.getVisuals());
    }

    private void addObstacle(int xpos, int ypos, int width, int height){
        Obstacle o = new Obstacle(xpos, ypos, width, height);
        getChildren().add(o);
        obstacles.add(o);
    }

}
