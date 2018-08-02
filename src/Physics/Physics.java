package Physics;

import Objects.Entities.Entity;
import Objects.ICollidable;

/**
 * A class full of static methods for mathematical calculations
 */
public class Physics {
    final static float T_EPSILON = 0.005f;

    /**
     * Gets the distance between to points
     * @param x1 x coord of point 1
     * @param y1 y coord of point 1
     * @param x2 x coord of point 2
     * @param y2 y coord of point 2
     * @return the distance between the two points
     */
    public static float getDistance(float x1, float y1, float x2, float y2){
        float xdist = x1 - x2;
        float ydist = y1 - y2;
        return (float)Math.sqrt(xdist*xdist + ydist*ydist);
    }

    /**
     * Reduces a vector into its x component
     * @param mag the magnitude of the vector
     * @param angle the angle of the vector
     * @return the x component of the vector
     */
    public static float xComponent(float mag, float angle){
        return mag*(float)Math.sin(angle);

    }

    /**
     * Reduces a vector into its y component
     * @param mag the magnitude of the vector
     * @param angle the angle of the vector
     * @return the y component of the vector
     */
    public static float yComponent(float mag, float angle){
        return -mag*(float)Math.cos(angle);
    }

    /**
     * Converts an angle from degrees into radiians
     * @param angle the angle in degrees
     * @return the same angle but in radiians
     */
    public static float toRadiians(float angle){
        return (float)(angle * Math.PI / 180);
    }

    /**
     * Finds the angle of the line from point 1 to point 2
     * @param x1 x coord of point 1
     * @param y1 y coord of point 1
     * @param x2 x coord of point 2
     * @param y2 y coord of point 2
     * @return the angle of the line
     */
    public static float findAngle(float x1, float y1, float x2, float y2){
        // Find the change in X and change in Y
        float dx = -1*(x2 - x1);
        float dy = y2 - y1;

        // Get the angle of the line
        float angle = (float)Math.atan(dx/dy);

        // adjust for fact that arctan can only return a value from -90 to 90
        if(dy > 0){
            angle += Math.PI;
        }
        return angle;
    }

    /**
     * Check if an entity collides with the edges of the game screen
     * @param e the entity that could collide
     * @param xmin the coordinate of the left side of the screen (likely 0)
     * @param ymin the coordinate of the top of the screen (likely 0)
     * @param xmax the coordinate of the right side of the screen
     * @param ymax the coordinate of the bottom of the screen
     * @param t the amount of time to check for collisions in
     * @param C the collision object in which to store the earliest collision found
     */
    public static void checkBoxCollision(Entity e, float xmin, float ymin,
                                         float xmax, float ymax, float t, Collision C){

        // Check the right side
        if(e.getXVel() > 0) {
            checkVerticalLine(e.getX(), e.getXVel(), e.getXRadius(), xmax, t, C);
        }

        // left side
        if(e.getXVel() < 0) {
            checkVerticalLine(e.getX(), e.getXVel(), e.getXRadius(), xmin, t, C);
        }

        // top side
        if(e.getYVel() < 0) {
            checkHorizontalLine(e.getY(), e.getYVel(), e.getYRadius(), ymin, t, C);
        }

        // bottom side
        if(e.getYVel() > 0) {
            checkHorizontalLine(e.getY(), e.getYVel(), e.getYRadius(), ymax, t, C);
        }

    }

    /**
     * Checks if an entity collides with an infinite vertical line
     * @param xpos the beginning x position of the entity
     * @param xvol the beginning x velocity of the entit
     * @param radius half the width of the entity
     * @param linex the x coordinate of the infinite vertical line
     * @param tmax the amount of time being checked
     * @param C the collision object to store the earliest collision found
     */
    public static void checkVerticalLine(float xpos, float xvol, float radius, float linex,
                                         float tmax, Collision C){
        // Use a temporary collision object to track the current earliest collision
        Collision tempC = new Collision();

        if(xvol == 0){ // no collision if nothing moves
            return;
        }

        // distance from border (negative is a left move, positive is a right move)
        float distance;
        if(linex < xpos){
            distance = xpos - (linex + radius);
        }
        else{
            distance = xpos - (linex - radius);
        }
        distance = -1*distance;

        // Find the amount of time until the entity will collide with this line
        float timetocollision = distance/xvol;
        // Store it if the entity will hit the line within the time step
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.xcollide = true; // stop x
        }

        // If the collision is earlier than the entity's earliest, save it
        if(tempC.t < C.t){
            C.copy(tempC);
        }

    }

    /**
     * Checks if an entity collides with an infinite horizontal line
     * @param ypos the beginning y position of the entity
     * @param yvol the beginning y velocity of the entity
     * @param radius half the width of the entity
     * @param liney the y coordinate of the infinite vertical line
     * @param tmax the amount of time being checked
     * @param C the collision object to store the earliest collision found
     */
    public static void checkHorizontalLine(float ypos, float yvol, float radius, float liney,
                                           float tmax, Collision C){
        // Use a temporary collision object to track the current earliest collision
        Collision tempC = new Collision();

        if(yvol == 0){ // no collision if nothing moves
            return;
        }

        // distance from border (negative is an up move, positive is a down move)
        float distance;
        if(liney < ypos){
            distance = ypos - (liney + radius);
        }
        else{
            distance = ypos - (liney - radius);
        }
        distance = -1*distance;

        // Find the amount of time until the entity will collide with this line
        float timetocollision = distance/yvol;
        // Store it if the entity will hit the line within the time step
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.ycollide = true; // stop y
        }

        // If the collision is earlier than the entity's earliest, save it
        if(tempC.t < C.t){
            C.copy(tempC);
        }
    }

    /**
     * Check if an entity hits a collidable objects
     * @param e the entity
     * @param o the collidable object
     * @param t the time step to check
     * @param C the collision object of the entity in which to store the earliest collision
     */
    public static void checkObstacleCollision(Entity e, ICollidable o, float t, Collision C){

        if(e.getXVel() == 0 && e.getYVel() == 0){ // not moving so no collisions
            return;
        }

        float ydist, xdist;
        float fX, fY;
        float timetocollision;
        Collision tempC = new Collision(); // temporary collision for storing the current earliest collision

        // Check if the entity will hit the object's top Line
        if(e.getYVel() > 0) {
            // get the distance from the line
            ydist = o.topY() - e.getY() - e.getYRadius();
            // get the time until the entity will cross this line
            timetocollision = ydist / e.getYVel();
            // Is the entity going to hit this line during the time step AND is it earlier than its earliest collision?
            if (timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t) {
                // Calculate how much the entity moved on the other plane during this time
                fX = e.getX() + (e.getXVel() * timetocollision);
                // Only store the collision if the entity will be within the bounds of the wall when the collision occurs
                if (fX - e.getXRadius() <= o.rightX() && fX + e.getXRadius() >= o.leftX()) {
                    tempC.t = timetocollision;
                    tempC.xcollide = false;
                    tempC.ycollide = true;
                }
            }
        }

        // Check if the entity will hit the object's bottom Line (See top line for more detailed comments)
        if(e.getYVel() < 0) {
            ydist = o.bottomY() - e.getY() + e.getYRadius();
            timetocollision = ydist / e.getYVel();
            if (timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t) {
                fX = e.getX() + (e.getXVel() * timetocollision);
                if (fX - e.getXRadius() <= o.rightX() && fX + e.getXRadius() >= o.leftX()) {
                    tempC.t = timetocollision;
                    tempC.xcollide = false;
                    tempC.ycollide = true;
                }
            }
        }

        // Check if the entity will hit the object's left Line (See top line for more detailed comments)
        if(e.getXVel() > 0) {
            xdist = o.leftX() - e.getX() - e.getXRadius();
            timetocollision = xdist / e.getXVel();
            if (timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t) {
                fY = e.getY() + (e.getYVel() * timetocollision);
                if (fY - e.getYRadius() <= o.bottomY() && fY + e.getYRadius() >= o.topY()) {
                    tempC.t = timetocollision;
                    tempC.xcollide = true;
                    tempC.ycollide = false;
                }
            }
        }

        // Check if the entity will hit the object's right Line (See top line for more detailed comments)
        if(e.getXVel() < 0) {
            xdist = o.rightX() - e.getX() + e.getXRadius();
            timetocollision = xdist / e.getXVel();
            if (timetocollision >= 0 && timetocollision < t && timetocollision <= tempC.t) {
                fY = e.getY() + (e.getYVel() * timetocollision);
                if (fY - e.getYRadius() <= o.bottomY() && fY + e.getYRadius() >= o.topY()) {
                    tempC.t = timetocollision;
                    tempC.xcollide = true;
                    tempC.ycollide = false;
                }
            }
        }

        // Copy the collision over
        C.copy(tempC);

    }
}