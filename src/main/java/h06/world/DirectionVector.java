package h06.world;

import java.awt.Point;

/**
 * The DirectionVector enum represents vectors in four directions: UP, RIGHT, DOWN, and LEFT.
 *
 * <p>Each DirectionVector has an x and y component that can be used for movement or other purposes.
 *
 * @author Nhan Huynh
 */
public enum DirectionVector {

    /**
     * The direction vector pointing up.
     */
    UP(0, 1),

    /**
     * The direction vector pointing right.
     */
    RIGHT(1, 0),

    /**
     * The direction vector pointing down.
     */
    DOWN(0, -1),

    /**
     * The direction vector pointing left.
     */
    LEFT(-1, 0);

    /**
     * The x component of the direction vector.
     */
    private final int x;

    /**
     * The y component of the direction vector.
     */
    private final int y;

    /**
     * Constructs a new direction vector with the given x and y components.
     *
     * @param x the x component of the direction vector
     * @param y the y component of the direction vector
     */
    DirectionVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the direction vector counterclockwise to this direction vector (90 degrees to the left).
     *
     * @return the direction vector counterclockwise to this direction vector
     */
    public DirectionVector left() {
        return values()[(ordinal() + values().length - 1) % values().length];
    }

    /**
     * Returns the direction vector clockwise to this direction vector (90 degrees to the right).
     *
     * @return the direction vector clockwise to this direction vector
     */
    public DirectionVector right() {
        return values()[(ordinal() + 1) % values().length];
    }

    /**
     * Returns the x component of the direction vector.
     *
     * @return the x component of the direction vector
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y component of the direction vector.
     *
     * @return the y component of the direction vector
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a point that is the result of adding this direction vector to the given point.
     *
     * @param p The Point to add to.
     * @return a point that is the result of adding this direction vector to the given point
     */
    public Point plus(Point p) {
        return new Point(p.x + x, p.y + y);
    }
}
