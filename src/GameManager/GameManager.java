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

import java.util.*;

public class GameManager extends Pane {

    private HashMap<String,Entity> entities = new HashMap<>();
    private HashMap<String, Projectile> projectiles = new HashMap<>();
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

    public void start(){
        time.play();
    }

    public void start(Scene scene){
        Player p = new Player("Ply-1", 50, 50);
        p.initializeAsPlayer1(scene, new FireEventHandler() {
            @Override
            public void handle(FireEvent fe) {
                // Add bullet here
                fireQueue.add(fe.projectile);
            }
        });
        addPlayer(p);
        addPlayer(new Player ("Ply2", 750, 750));
        addObstacle(375, 200, 50, 400);
        addObstacle(200, 375, 400, 50);
        time.play();
    }

    public void draw(){
        Iterator it = entities.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.draw();
        }
    }

    private void update(){
        Iterator it = entities.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.update();
        }

        // Add projectiles from queue
        it = fireQueue.iterator();
        while(it.hasNext()){
            Projectile p = (Projectile)it.next();
            addProjectile(p);
            it.remove();
        }
        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Projectile p = (Projectile) ((Map.Entry)it.next()).getValue();
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
        Iterator it = entities.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.reset();
            if((tempTime = e.checkCollisions(timeLeft, obstacles)) < firstCollisionTime){
                firstCollisionTime = tempTime;
            }
        }

        return firstCollisionTime;
    }

    private void move(float time){

        Iterator it = entities.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.move(time);
        }
    }


    protected void calculateFrame(){
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

    public void addPlayer(Player p){
        addEntity(p);
        obstacles.add(p);
    }

    public void addProjectile(Projectile p){
        addEntity(p);
        projectiles.put(p.getID(), p);
    }

    public void addEntity(Entity e){
        entities.put(e.getID(), e);
        this.getChildren().add(e.getVisuals());
    }

    public void removeEntity(Entity e){
        entities.remove(e.getID());
        this.getChildren().remove(e.getVisuals());
    }

    public Entity getEntity(String id){
        if(entities.containsKey(id)){
            return entities.get(id);
        }
        else{
            System.err.println("Entity with id " + id + " does not exist.");
            return null;
        }
    }

    public void updateEntity(String ID, float x, float y, float xvel, float yvel, float angle){
        if(entities.containsKey(ID)){
            entities.get(ID).updateState(x, y, xvel, yvel, angle);
        }
    }

    private void addObstacle(int xpos, int ypos, int width, int height){
        Obstacle o = new Obstacle(xpos, ypos, width, height);
        getChildren().add(o);
        obstacles.add(o);
    }

}
