package Physics;

import Global.Settings;
import Objects.Entities.Entity;
import Objects.GameMap;
import Objects.ICollidable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

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
     * @param angle the angle of the vector in radiians
     * @return the x component of the vector
     */
    public static float xComponent(float mag, float angle){
        return mag*(float)Math.sin(angle);

    }

    /**
     * Reduces a vector into its y component
     * @param mag the magnitude of the vector
     * @param angle the angle of the vector in radiians
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
     * Converts an angle from radiians into degrees
     * @param angle the angle in radiians
     * @return the same angle but in degrees
     */
    public static float toDegrees(float angle){
        return (float)(angle * 180 / Math.PI);
    }

    /**
     * Takes an angle (in degrees) and normalizes to within the range 0 to 360
     * @param angle the angle in degrees
     * @return the same angle but between 0 and 360
     */
    public static float normalize(float angle){
        while(angle < 0){
            angle += 360;
        }
        while(angle > 360){
            angle -= 360;
        }
        return angle;
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

    /**
     * Checks if a player is within the line of sight given
     * @param los the line of sight
     * @param collidable the player
     * @return true if the object is within the line of sight
     */
    public static boolean checkLOS(Line los, ICollidable collidable){
        // Get the line along each edge of the object's hit box
        Line[] lines = new Line[]{
                new Line(collidable.leftX(), collidable.topY(), collidable.rightX(), collidable.topY()), // top line
                new Line(collidable.leftX(), collidable.bottomY(), collidable.rightX(), collidable.bottomY()), // bottom line
                new Line(collidable.leftX(), collidable.topY(), collidable.leftX(), collidable.bottomY()), // left line
                new Line(collidable.rightX(), collidable.topY(), collidable.rightX(), collidable.bottomY()) // right line
        };

        // If the line of site crosses any of the lines, return true
        for(Line line: lines){
            if(los.intersects(line)){
                return true;
            }
        }

        return false;

    }

    /**
     * Create a line representing the line from the given point outwards at the given angle that goes as far as the
     * nearest obstacle
     * @param startx the starting point x coordinate
     * @param starty the starting point y coordinate
     * @param angle the angle of the line
     * @return the representitive line
     */
    public static Line getLOS(float startx, float starty, float angle){

        // Get the diagonal length of the screen, which is the max length line possible
        float a = Settings.getWindowWidth();
        float b = Settings.getWindowHeight();
        float screenDiagonal =  (float)Math.sqrt((a*a) + (b*b));

        // Create a line of the maximum length from the start point at the given angle
        Line los = new Line(startx, starty, angle, screenDiagonal, true);

        // Loop through each obstacle, finding the distance from the start point if the line crosses the obstacle
        // If the distance is shorter than the distance to any previous obstacle, store it as the new closest
        float closestLosDist = screenDiagonal;
        float temp;
        for(ICollidable collidable: GameMap.getObstacles()){
            if((temp = getLOSDist(startx, starty, los, collidable)) < closestLosDist){
                closestLosDist = temp;
            }
        }

        // Return the same line except with the shortest distance to an obstacle it crosses for its length
        return new Line(startx, starty, angle, closestLosDist, true);
    }

    /**
     * If the line given crosses the obstacle given, return the distance from the line's start point
     * @param startx the start point x coordinate
     * @param starty the start point y coordinate
     * @param los the line being checked for crossing the obstacle
     * @param collidable the obstacle being checked for the line crossing
     * @return the distance from the start point to where the line crosses the obstacle, or FLoat.MAX if it does not cross
     */
    public static float getLOSDist(float startx, float starty, Line los, ICollidable collidable){

        float closestIntersection = Float.MAX_VALUE;

        // Get the line from each side of the object's collision box
        Line topLine = new Line(collidable.leftX(), collidable.topY(), collidable.rightX(), collidable.topY()); // top line
        Line bottomLine = new Line(collidable.leftX(), collidable.bottomY(), collidable.rightX(), collidable.bottomY()); // bottom line
        Line leftLine = new Line(collidable.leftX(), collidable.topY(), collidable.leftX(), collidable.bottomY()); // left line
        Line rightLine = new Line(collidable.rightX(), collidable.topY(), collidable.rightX(), collidable.bottomY()); // right line

        // If the line is horizonatl, only check the obstacle's vertical lines
        float dist;
        if(los.isHorizontal()){
            if(los.intersects(rightLine)){
                dist = Math.abs(starty - topLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
            if(los.intersects(topLine)){
                dist = Math.abs(starty - topLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
        }
        // If the line is vertical, only check the obstacle's horizonatl lines
        else if(los.isVertical()){
            if(los.intersects(topLine)){
                dist = Math.abs(starty - topLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
            if(los.intersects(bottomLine)){
                dist = Math.abs(starty - topLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
        }

        // If neither horizontal or vertical, check all 4 lines
        else{
            if(los.intersects(topLine)){
                dist = Physics.getDistance(startx, starty, los.getXAt(topLine.p1.y), topLine.p1.y);
                debugAddDot(los.getXAt(topLine.p1.y), topLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
            if(los.intersects(bottomLine)){
                dist = Physics.getDistance(startx, starty, los.getXAt(bottomLine.p1.y), bottomLine.p1.y);
                debugAddDot(los.getXAt(bottomLine.p1.y), bottomLine.p1.y);
                if(dist < closestIntersection) closestIntersection = dist;
            }
            if(los.intersects(rightLine)){
                dist = Physics.getDistance(startx, starty, rightLine.p1.x, los.getYAt(rightLine.p1.x));
                debugAddDot(rightLine.p1.x, los.getYAt(rightLine.p1.x));
                if(dist < closestIntersection) closestIntersection = dist;
            }
            if(los.intersects(leftLine)){
                dist = Physics.getDistance(startx, starty, leftLine.p1.x, los.getYAt(leftLine.p1.x));
                debugAddDot(leftLine.p1.x, los.getYAt(leftLine.p1.x));
                if(dist < closestIntersection) closestIntersection = dist;
            }
        }

        return closestIntersection;
    }

    /**
     * Adds a simple visual dot to the screen at a given coord for debugging
     * @param x
     * @param y
     */
    private static void debugAddDot(float x, float y){
        if(Settings.isDebug()){
            Circle c = new Circle(4);
            c.setFill(Color.RED);
            c.setTranslateX(x - 2);
            c.setTranslateY(y - 2);
            Settings.addDebugVisual(c);
        }
    }


}