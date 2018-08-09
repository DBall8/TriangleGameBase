package Objects;

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
        }
    }

    public static Obstacle[] getObstacles(){ return getInstance().obstacles; }


    private static class SingletonHelper{
        private final static GameMap instance = new GameMap();
    }

    private static GameMap getInstance(){ return SingletonHelper.instance; }
}
