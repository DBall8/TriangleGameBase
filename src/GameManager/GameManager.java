package GameManager;

import GameManager.FrameEvent.ClientFrameEvent;
import GameManager.FrameEvent.IFrameEvent;
import GameManager.FrameEvent.FrameEventHandler;
import GameManager.FrameEvent.ServerFrameEvent;
import Global.Settings;
import Objects.Entities.Entity;
import Objects.Entities.Player;
import Objects.Entities.Projectile;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Objects.ICollidable;
import Objects.Obstacle;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.*;

public class GameManager extends Pane {

    private HashMap<String,Player> players = new HashMap<>();
    private HashMap<String, Projectile> projectiles = new HashMap<>();
    private List<Projectile> projectileQueue = new ArrayList<>();
    private List<Entity> playerQueue = new ArrayList<>();
    private List<ICollidable> obstacles = new ArrayList<>();
    private int width, height;
    private GameTime time;

    private Player p1;

    private FrameEventHandler frameHandler;

    public GameManager(FrameEventHandler frameHandler){
        super();
        this.width = Settings.getWindowWidth();
        this.height = Settings.getWindowHeight();

        this.frameHandler = frameHandler;

        time = new GameTime(this);
    }

    public void start(){
        time.play();
    }

    public void start(Scene scene, boolean asServer){
        if(!asServer){
            p1 = new Player("Ply-" + System.currentTimeMillis(), 50, 50);
            p1.initializeAsPlayer1(scene, new FireEventHandler() {
                @Override
                public void handle(FireEvent fe) {
                    // Add bullet here
                    projectileQueue.add(fe.projectile);
                }
            });
            addPlayer(p1);
        }
        addObstacle(375, 200, 50, 400);
        addObstacle(200, 375, 400, 50);
        time.play();
    }

    public void draw(){
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.draw();
        }

        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.draw();
        }
    }

    private void update(){
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Player p = (Player)((Map.Entry)it.next()).getValue();
            p.update();
        }
        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Projectile p = (Projectile) ((Map.Entry)it.next()).getValue();
            p.update();
            if(!p.isAlive()){
                it.remove();
                removeProjectile(p);
            }
        }

        updateFromQueues();
    }

    private void updateFromQueues(){
        // Add projectiles from queue
        Iterator it = projectileQueue.iterator();
        while(it.hasNext()){
            Projectile p = (Projectile)it.next();
            enterProjectile(p);
            it.remove();
        }

        // Add entities from queue
        it = playerQueue.iterator();
        while(it.hasNext()){
            Player p = (Player) it.next();
            enterPlayer(p);
            it.remove();
        }
    }

    private float checkCollisions(float timeLeft){
        float firstCollisionTime = timeLeft; // looks for first collision
        float tempTime;
        // reset collisions for each player
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.reset();
            if((tempTime = e.checkCollisions(timeLeft, obstacles)) < firstCollisionTime){
                firstCollisionTime = tempTime;
            }
        }

        it = projectiles.entrySet().iterator();
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

        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.move(time);
        }

        it = projectiles.entrySet().iterator();
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

        if(p1 != null) {
            frameHandler.handle(new ClientFrameEvent(p1));
        }
        else{
            frameHandler.handle(new ServerFrameEvent(players.entrySet().iterator()));
        }
    }

    public void addPlayer(Player p){
        safeAdd(playerQueue, p);
    }

    private void enterPlayer(Player p){
        if(!players.containsKey(p.getID())) {
            players.put(p.getID(), p);
            obstacles.add(p);
            this.getChildren().add(p.getVisuals());
        }
    }

    public void removePlayer(String ID){
        Player p = players.get(ID);
        obstacles.remove(p);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().remove(p.getVisuals());
            }
        });
        players.remove(ID);
    }

    public Entity getPlayer(String id){
        if(players.containsKey(id)){
            return players.get(id);
        }
        else{
            return null;
        }
    }

    public void updatePlayer(String ID, float x, float y, float xvel, float yvel, float angle){
        if(players.containsKey(ID)){
            players.get(ID).updateState(x, y, xvel, yvel, angle);
        }
    }

    public void addProjectile(Projectile p){
        safeAdd(projectileQueue, p);
    }

    private void enterProjectile(Projectile p){
        if(!projectiles.containsKey(p.getID())){
            projectiles.put(p.getID(), p);
            getChildren().add(p.getVisuals());
        }

    }

    private void removeProjectile(Projectile p){
        getChildren().remove(p.getVisuals());
    }


    private void addObstacle(int xpos, int ypos, int width, int height){
        Obstacle o = new Obstacle(xpos, ypos, width, height);
        getChildren().add(o);
        obstacles.add(o);
    }

    private synchronized <T> void safeAdd(List<T> list, T item){
        list.add(item);
    }

    private synchronized <T> void safeRemove(List<T> list, T item){
        list.remove(item);
    }

    public String getPlayerID(){
        if(p1 != null) {
            return p1.getID();
        }
        else{
            return "";
        }
    }

}
