package GameManager;

import GameManager.FrameEvent.ClientFrameEvent;
import GameManager.FrameEvent.FrameEventHandler;
import GameManager.FrameEvent.ServerFrameEvent;
import Objects.Entities.Entity;
import Objects.Entities.Player;
import Objects.Entities.Projectile;
import Objects.FireEvent.FireEvent;
import Objects.FireEvent.FireEventHandler;
import Objects.ICollidable;
import Objects.Obstacle;
import Visuals.Background;
import Visuals.HUD;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * A class for creating a game simulation
 */
public class GameManager extends Pane {

    private Background background;
    private Group foreground;
    private HUD hud;

    private HashMap<String,Player> players = new HashMap<>(); // a map of all players by their ID
    private HashMap<String, Projectile> projectiles = new HashMap<>(); // a map of all projectiles by their ID (should it be a list???)
    private List<Projectile> projectileQueue = new ArrayList<>(); // a list of projectiles to add at next frame
    private List<Entity> playerQueue = new ArrayList<>(); // a list of players to add at next frame
    private List<ICollidable> obstacles = new ArrayList<>(); // a list of all obstacles
    private GameTime time; // The class responsible for running the game frame updating at the correct period

    private Player p1; // the player being controlled

    private FrameEventHandler frameHandler; // the object to pass completed frames to

    // Constructor
    public GameManager(FrameEventHandler frameHandler){
        super();

        background = new Background();
        foreground = new Group();
        hud = new HUD();

        getChildren().addAll(background, foreground, hud);

        this.frameHandler = frameHandler;

        // build new Game Time
        time = new GameTime(this);
    }

    /**
     * Starts the simulation with no visuals
     */
    public void start(){
        time.play();
    }

    /**
     * Starts the simulation with visuals
     * @param scene the scene to display in
     * @param asServer true if the simulation is a server simulaiton, and false if it is a client simulation
     */
    public void start(Scene scene, boolean asServer){
        // If run by a client, set up a controllable player
        if(!asServer){
            p1 = new Player("Ply-" + System.currentTimeMillis(), 50, 50);
            p1.initializeAsPlayer1(scene, new FireEventHandler() {
                @Override
                public void handle(FireEvent fe) {
                    // Add bullet here
                    enterProjectile(fe.projectile);
                    p1.addNewShot(fe.projectile);
                }
            });
            addPlayer(p1);
        }
        // add map obstacles
        addObstacle(375, 200, 50, 400);
        addObstacle(200, 375, 400, 50);

        // start playing the game
        time.play();
    }

    /**
     * Updates the visuals of all entities
     */
    public void draw(){
        // update all player visuals
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.draw();
        }

        // update all projectile visuals
        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.draw();
        }
    }

    /**
     * Runs the update function of every entity
     */
    private void update(){
        // Updates all players
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Player p = (Player)((Map.Entry)it.next()).getValue();
            p.update();
        }

        // updates all projectiles
        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Projectile p = (Projectile) ((Map.Entry)it.next()).getValue();
            p.update();
            if(!p.isAlive()){
                it.remove();
                removeProjectile(p);
            }
        }

        // Add any entities waiting to be added to the game
        updateFromQueues();
    }

    /**
     * Takes all new entities received from outside the GameManager and adds them to the game
     */
    private synchronized void updateFromQueues(){
        // Add projectiles from queue
        Iterator it = projectileQueue.iterator();
        while(it.hasNext()){
            Projectile p = (Projectile)it.next();
            enterProjectile(p);
            it.remove();
        }

        // Add players from queue
        it = playerQueue.iterator();
        while(it.hasNext()){
            Player p = (Player) it.next();
            enterPlayer(p);
            it.remove();
        }
    }

    /**
     * Finds the earliest collision in the time step
     * @param timeLeft the remaining time for the time step
     * @return the time of the earliest collision
     */
    private float checkCollisions(float timeLeft){
        float firstCollisionTime = timeLeft; // looks for first collision
        float tempTime;

        // checks for each player's earliest collision
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.reset();
            if((tempTime = e.checkCollisions(timeLeft, obstacles)) < firstCollisionTime){
                firstCollisionTime = tempTime;
            }
        }

        // checks for each projectile's earliest collision
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

    /**
     * Moves each entity through time by the given amount
     * @param time the amount of time to move each entity by
     */
    private void move(float time){
        // Move each player
        Iterator it = players.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.move(time);
        }

        // Move each projectile
        it = projectiles.entrySet().iterator();
        while(it.hasNext()){
            Entity e = (Entity)((Map.Entry)it.next()).getValue();
            e.move(time);
        }
    }

    /**
     * Calculates the next frame of the game simulation
     */
    protected void calculateFrame(){
        float timeLeft = 1.00f; // the amount of time left (starts as 1.0 == 100%)
        float firstCollisionTime; // the first collision time found

        // Update all entities
        update();

        // Run until time step is completed
        do {
            // Check if each entity hits any collidable object
            firstCollisionTime = checkCollisions(timeLeft);

            // move each entity to the time of the first detected collision (if any)
            move(firstCollisionTime);

            // mark down the remaining time in the time step
            timeLeft -= firstCollisionTime;

        }while(timeLeft > 0.01f);

        // Once frame is completed, trigger a frame event

        // Do a client frame event if this is a client game
        if(p1 != null) {
            frameHandler.handle(new ClientFrameEvent(p1));
        }
        // Do a server frame event if this is a server game
        else{
            frameHandler.handle(new ServerFrameEvent(players.entrySet().iterator()));
        }
    }

    /**
     * Adds a player to the queue for adding to the game at the next frame
     * @param p the player to add
     */
    public void addPlayer(Player p){
        safeAdd(playerQueue, p);
    }

    /**
     * Enters the player into the game
     * @param p the player to enter
     */
    private void enterPlayer(Player p){
        // Make sure the player doesnt already exist
        if(!players.containsKey(p.getID())) {
            players.put(p.getID(), p); // add to map
            obstacles.add(p); // add to collidable objects
            foreground.getChildren().add(p.getVisuals()); // add to visuals

            p.attachHUD(hud.addNewPlayerUI());
        }
    }

    /**
     * Removes player from the game
     * @param ID the ID of the player to remove
     */
    public void removePlayer(String ID){
        // remove the player from the list of collidables
        Player p = players.get(ID);
        obstacles.remove(p);

        // remove the visuals from the UI thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                foreground.getChildren().remove(p.getVisuals());
                hud.removePlayerUI(p.getUI());
            }
        });



        // remove the player from the map
        players.remove(ID);
    }

    /**
     * Retreives a player
     * @param id the ID of the player to retreive
     * @return the player instance, or null if the player's ID is not present
     */
    public Entity getPlayer(String id){
        if(players.containsKey(id)){
            return players.get(id);
        }
        else{
            return null;
        }
    }

    /**
     * Retreives a projectile
     * @param id the ID of the projectile to retreive
     * @return the projectile instance, or null if the projectile's ID is not present
     */
    public Projectile getProjectile(String id){
        if(projectiles.containsKey(id)){
            return projectiles.get(id);
        }
        else{
            return null;
        }
    }

    /**
     * Updates the status of a player
     * @param ID the ID of the player to upate
     * @param x the player's new x position
     * @param y the player's new y position
     * @param xvel the player's new x velocity
     * @param yvel the player's new y velocity
     * @param angle the player's new angle
     */
    public void updatePlayer(String ID, float x, float y, float xvel, float yvel, float angle){
        if(players.containsKey(ID)){
            players.get(ID).updateState(x, y, xvel, yvel, angle);
        }
    }

    /**
     * Adds a projectile to a list of projectiles to add at the next frame
     * @param p the projectile to add
     */
    public void addProjectile(Projectile p){
        safeAdd(projectileQueue, p);
        if(p1 == null){
            Player owner = players.get(p.getOwnerID());
            if(owner != null){
                owner.addNewShot(p);
            }
        }
    }

    /**
     * Enters a projectile into the game simulation
     * @param p the projectile to enter
     */
    private void enterProjectile(Projectile p){
        // Make sure the projectile does not already exist
        if(!projectiles.containsKey(p.getID())){
            projectiles.put(p.getID(), p); // put in projectiles map
            foreground.getChildren().add(p.getVisuals()); // add the visuals
        }

    }

    /**
     * Remove the projectile from the game (only done locally on the UI thread)
     * @param p the projectile to remove
     */
    private void removeProjectile(Projectile p){
        foreground.getChildren().remove(p.getVisuals());
    }

    /**
     * Adss a new collidable obstacle to the game
     * @param xpos the obstacle's left edge coordinate
     * @param ypos the obstacle's top edge coordinate
     * @param width the obstacle's width
     * @param height the obstacle's height
     */
    private void addObstacle(int xpos, int ypos, int width, int height){
        // create the obstacle
        Obstacle o = new Obstacle(xpos, ypos, width, height);
        // add the visuals
        foreground.getChildren().add(o);
        // add to the list
        obstacles.add(o);
    }

    /**
     * A method for adding to a synchronized list
     * @param list the list to add to
     * @param item the item to add
     * @param <T> the type of the time
     */
    private synchronized <T> void safeAdd(List<T> list, T item){
        list.add(item);
    }

    /**
     * A method for removing from a synchronized list
     * @param list the list to remove from
     * @param item the item to remove
     * @param <T> the type of the time
     */
    private synchronized <T> void safeRemove(List<T> list, T item){
        list.remove(item);
    }

    /**
     * Gets the ID of the controlled player, or an empty string if no player is controlled
     * @return
     */
    public String getPlayerID(){
        if(p1 != null) {
            return p1.getID();
        }
        else{
            return "";
        }
    }

}
