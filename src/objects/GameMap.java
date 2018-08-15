package objects;

public class GameMap {

    private static Obstacle[] obstacles;

    private GameMap(){}

    public static void buildMap(String name){
        switch(name){
            default:
            case "Cross":
                getInstance().obstacles = new Obstacle[]{
                        new Obstacle(375, 200, 50, 400),
                    new Obstacle(200, 375, 400, 50)
                };
                break;
            case "Big":
                getInstance().obstacles = new Obstacle[]{
                        new Obstacle(375, 200, 50, 400),
                        new Obstacle(200, 375, 400, 50),
                        new Obstacle(1375, 400, 50, 400),
                        new Obstacle(1200, 575, 400, 50)
                };
                break;
        }
    }

    public static Obstacle[] getObstacles(){ return getInstance().obstacles; }


    private static class SingletonHelper{
        private final static GameMap instance = new GameMap();
    }

    private static GameMap getInstance(){ return SingletonHelper.instance; }
}
