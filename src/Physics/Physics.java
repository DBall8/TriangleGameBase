package Physics;

import Objects.Entities.Entity;
import Objects.ICollidable;

public class Physics {
    final static float T_EPSILON = 0.005f;

    public static float getDistance(float x1, float y1, float x2, float y2){
        float xdist = x1 - x2;
        float ydist = y1 - y2;
        return (float)Math.sqrt(xdist*xdist + ydist*ydist);
    }

    public static float xComponent(float mag, float angle){
        return mag*(float)Math.sin(angle);

    }

    public static float yComponent(float mag, float angle){
        return -mag*(float)Math.cos(angle);
    }

    public static float findAngle(float x1, float y1, float x2, float y2){
        float dx = -1*(x2 - x1);
        float dy = y2 - y1;
        // goal angle to turn to
        float angle = (float)Math.atan(dx/dy);

        // adjust for fact that arctan can only return a value from -90 to 90
        if(dy > 0){
            angle += Math.PI;
        }
        return angle;
    }

    public static float toRadiians(float angle){
        return (float)(angle * Math.PI / 180);
    }

    // check if the Objects.Entities.Entity collides with the bounding box
    public static void checkBoxCollision(Entity e, float xmin, float ymin,
                                         float xmax, float ymax, float t, Collision C){

        // right
        checkVerticalLine(e.getX(), e.getXVel(), e.getYVel(), e.getXRadius(), xmax, t, C);


        // left
        checkVerticalLine(e.getX(), e.getXVel(), e.getYVel(), e.getXRadius(), xmin, t, C);

        // up
        checkHorizontalLine(e.getY(), e.getXVel(), e.getYVel(), e.getYRadius(), ymin, t, C);

        // down
        checkHorizontalLine(e.getY(), e.getXVel(), e.getYVel(), e.getYRadius(), ymax, t, C);

    }

    // check if a particle hits a vertical line
    public static void checkVerticalLine(float xpos, float xvol, float yvol, float radius, float linex,
                                         float tmax, Collision C){
        Collision tempC = new Collision();

        if(xvol == 0){ // no collision if nothing moves
            return;
        }

        float distance; // distance from border (negative is a left move, positive is a right move)
        if(linex < xpos){
            distance = xpos - (linex + radius);
        }
        else{
            distance = xpos - (linex - radius);
        }
        distance = -1*distance;
        float timetocollision = distance/xvol;
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.xcollide = true; // stop x
        }

        if(tempC.t < C.t){
            C.copy(tempC);
        }

    }

    // check if a particle hits a horizontal line
    public static void checkHorizontalLine(float ypos, float xvol, float yvol, float radius, float liney,
                                           float tmax, Collision C){

        Collision tempC = new Collision();

        if(yvol == 0){ // no collision if nothing moves
            return;
        }

        float distance; // distance from border (negative is an up move, positive is a down move)
        if(liney < ypos){
            distance = ypos - (liney + radius);
        }
        else{
            distance = ypos - (liney - radius);
        }
        distance = -1*distance;
        float timetocollision = distance/yvol;
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.ycollide = true; // stop y
        }

        if(tempC.t < C.t){
            C.copy(tempC);
        }
    }

    public static void checkObstacleCollision(Entity e, ICollidable o, float t, Collision C){

        if(e.getXVel() == 0 && e.getYVel() == 0){ // not moving so no collisions
            return;
        }

        float ydist, xdist;
        float fX, fY;
        float timetocollision;
        Collision tempC = new Collision();

        // Top Line
        ydist = o.topY() - e.getY() - e.getYRadius();
        timetocollision = ydist/e.getYVel();
        if(timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t){
            fX = e.getX() + (e.getXVel() * timetocollision);
            if(fX - e.getXRadius() <= o.rightX() && fX + e.getXRadius() >= o.leftX()) {
                tempC.t = timetocollision;
                tempC.xcollide = false;
                tempC.ycollide = true;
            }
        }

        // Bottom Line
        ydist = o.bottomY() - e.getY() + e.getYRadius();
        timetocollision = ydist/e.getYVel();
        if(timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t){
            fX = e.getX() + (e.getXVel() * timetocollision);
            if(fX - e.getXRadius() <= o.rightX() && fX + e.getXRadius() >= o.leftX()) {
                tempC.t = timetocollision;
                tempC.xcollide = false;
                tempC.ycollide = true;
            }
        }

        // Left Line
        xdist = o.leftX() - e.getX() - e.getXRadius();
        timetocollision = xdist/e.getXVel();
        if(timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t){
            fY = e.getY() + (e.getYVel() * timetocollision);
            if(fY - e.getYRadius() <= o.bottomY() && fY + e.getYRadius() >= o.topY()) {
                tempC.t = timetocollision;
                tempC.xcollide = true;
                tempC.ycollide = false;
            }
        }

        // Right Line
        xdist = o.rightX() - e.getX() + e.getXRadius();
        timetocollision = xdist/e.getXVel();
        if(timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t){
            fY = e.getY() + (e.getYVel() * timetocollision);
            if(fY - e.getYRadius() <= o.bottomY() && fY + e.getYRadius() >= o.topY()) {
                tempC.t = timetocollision;
                tempC.xcollide = true;
                tempC.ycollide = false;
            }
        }

        C.copy(tempC);

    }
}
