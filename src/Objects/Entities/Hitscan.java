package Objects.Entities;

import Objects.ICollidable;
import Physics.Line;

import java.util.List;

public class Hitscan extends Entity{

    private final static int WIDTH = 4;

    public Hitscan(String id, int x, int y, Line los) {
        super(id, x, y, WIDTH, (int)los.getLength());
    }

    @Override
    public void move(float time){}

    @Override
    public float checkCollisions(float time, List<ICollidable> obstacles){
        return time;
    }
}
