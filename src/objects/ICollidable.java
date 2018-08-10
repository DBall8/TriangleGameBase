package objects;

/**
 * Interfacce for creating objects that can be collided with. Simply need to know the 4 sides of the rectangular
 * bounding box to use when looking for collisions
 */
public interface ICollidable {

    public float rightX();

    public float leftX();

    public float bottomY();

    public float topY();
}
