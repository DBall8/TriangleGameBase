package physics;

public class Bounds {

    float x, y;
    float width, height;

    public Bounds(float x, float y, float width, float height){
        this.x = x - width/2;
        this.y = y - height/2;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Bounds bounds){
        return bounds.pointIsInside(x, y) ||
                bounds.pointIsInside(x + width, y) ||
                bounds.pointIsInside(x, y + height) ||
                bounds.pointIsInside(x + width, y + height) ||
                pointIsInside(bounds.x, bounds.y);
    }

    private boolean pointIsInside(float px, float py){
        boolean insideX = x <= px && px <= x + width;
        boolean insideY = y <= py && py <= y + height;

        return  insideX && insideY;
    }

    public void updatePosition(float x, float y){
        this.x = x - width/2;
        this.y = y - height/2;
    }
    public float getWidth(){ return width; }
    public float getHeight(){ return height; }

    public float getX(){ return x; }
    public float getY(){ return y; }
}
