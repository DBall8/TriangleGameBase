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
        if( // If the bounds are so far they couldnt possible intersect, return false;
                x + width < bounds.x || bounds.x + bounds.width < x ||
                y + height < bounds.y || bounds.y + bounds.height < y
            ){
            return false;
        }
        return true;
        //return  boundsInside(bounds) || bounds.boundsInside(this);
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
