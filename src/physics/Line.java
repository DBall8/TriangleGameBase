package physics;

public class Line {

    Point p1, p2;
    float slope;
    float intercept;
    float length = -1;

    public Line(float x1, float y1, float x2, float y2){
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
    }

    // angle in degrees
    public Line(float x, float y, float angle, float length, boolean fromAngle){
        p1 = new Point(x, y);
        float x2 = x + Physics.xComponent(length, Physics.toRadiians(angle));
        float y2 = y + Physics.yComponent(length, Physics.toRadiians(angle));
        p2 = new Point(x2, y2);
    }

    public float getXAt(float y) {
        if(isHorizontal()) return 0;
        getSlopeAndIntercept();
        return (intercept - y) / -slope;
    }

    public float getYAt(float x) {
        if(isVertical()) return 0;
        getSlopeAndIntercept();
        return slope * x + intercept;
    }

    private void getSlopeAndIntercept(){
        if(!isVertical()){
            slope = (p2.y - p1.y) / (p2.x - p1.x);
            intercept = p1.y - slope*p1.x;
        }
    }

    public boolean isVertical(){
        return p1.x == p2.x;
    }

    public boolean isHorizontal(){
        return p1.y == p2.y;
    }

    public boolean intersects(Line l2){
        Orientation o1 = getOrientation(p1, p2, l2.p1);
        Orientation o2 = getOrientation(p1, p2, l2.p2);
        Orientation o3 = getOrientation(l2.p1, l2.p2, p1);
        Orientation o4 = getOrientation(l2.p1, l2.p2, p2);

        if(o1 != o2 && o3 != o4) return true;

        if(o1 == Orientation.COLINEAR && onSegment(p1, l2.p1, p2)) return true;

        if(o2 == Orientation.COLINEAR && onSegment(p1, l2.p1, p2)) return true;

        if(o3 == Orientation.COLINEAR && onSegment(l2.p1, p1, l2.p2)) return true;

        if(o4 == Orientation.COLINEAR && onSegment(l2.p1, p2, l2.p2)) return true;

        return false;
    }

    // Given that all 3 points are colinear, does p2 lie on the line between p1 and p3?
    private boolean onSegment(Point p1, Point p2, Point p3){
        boolean insideX, insideY;
        if(p1.x < p3.x){
            insideX = p1.x <= p2.x && p2.x <= p3.x;
        }
        else{
            insideX = p3.x <= p2.x && p2.x <= p1.x;
        }

        if(p1.y < p3.y){
            insideY = p1.y <= p2.y && p2.y <= p3.y;
        }
        else{
            insideY = p3.y <= p2.y && p2.y <= p1.y;
        }

        return insideX && insideX;
    }

    private Orientation getOrientation(Point p1, Point p2, Point p3){
        float t1 = (p2.y - p1.y) * (p3.x - p2.x);
        float t2 = (p3.y - p2.y) * (p2.x - p1.x);

        float t = t1 - t2;
        if(t < 0){
            return Orientation.COUNTERCLOCKWISE;
        }
        else if(t > 0){
            return Orientation.CLOCKWISE;
        }
        else{
            return Orientation.COLINEAR;
        }
    }

    private enum Orientation{
        CLOCKWISE,
        COLINEAR,
        COUNTERCLOCKWISE
    }

    public float getLength() {
        if (length >= 0) {
            return length;
        }
        else{
            return Physics.getDistance(p1.x, p1.y, p2.x, p2.y);
        }

    }

}
