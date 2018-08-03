package Events;

public class HitEvent{

    private String playerID;
    private int x;
    private int y;
    private int damage;

    public HitEvent(String playerID, int x, int y, int damage) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.damage = damage;
    }

    public String getPlayerID(){ return playerID; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }
}
