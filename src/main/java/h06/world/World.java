package h06.world;

import fopbot.FieldEntity;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.awt.Point;

/**
 * Represents a 2D world.
 *
 * @author Nhan Huynh
 */
public class World {

    /**
     * The width of the world.
     */
    private final int width;

    /**
     * The height of the world.
     */
    private final int height;

    /**
     * The fields of the world.
     */
    private final Field[][] fields;

    /**
     * Creates a new world with the given width and height.
     *
     * @param width  the width of the world
     * @param height the height of the world
     */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y] = new Field();
            }
        }
    }

    /**
     * Returns the width of the world.
     *
     * @return the width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the world.
     *
     * @return the height of the world
     */
    public int getHeight() {
        return height;
    }

    /**
     * Places a wall at the given position. The wall will separate the field from its neighbor on the right or below.
     *
     * @param x          the x coordinate of the wall
     * @param y          the y coordinate of the wall
     * @param horizontal the orientation of the wall
     */
    public void placeWall(int x, int y, boolean horizontal) {
        fields[x][y].addEntity(new Wall(x, y, horizontal));
    }

    /**
     * Checks if the given position is outside the world.
     *
     * @param x the x coordinate to check
     * @param y the y coordinate to check
     * @return {@code true} if the position is outside the world, {@code false} otherwise
     */
    public boolean isOutside(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }

    /**
     * Check if the given point plus the direction vector is outside the world.
     *
     * @param p the point to check
     * @param d the direction vector to add to the point
     * @return {@code true} if the position is outside the world, {@code false} otherwise
     */
    public boolean isOutside(Point p, DirectionVector d) {
        Point pos = d.getMovement(p);
        return isOutside(pos.x, pos.y);
    }

    /**
     * Checks if the given position is blocked by any boundaries.
     *
     * @param x          the x coordinate to check
     * @param y          the y coordinate to check
     * @param horizontal the orientation of the wall
     * @return {@code true} if the position is blocked, {@code false} otherwise
     */
    public boolean isBlocked(int x, int y, boolean horizontal) {
        if (isOutside(x, y)) {
            return true;
        }

        for (FieldEntity entity : fields[x][y].getEntities()) {
            if (entity instanceof Wall wall) {
                if (wall.isHorizontal() == horizontal) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given position plus direction vector is blocked by any boundaries.
     *
     * @param p the point to check
     * @param d the direction vector to add to the point
     * @return {@code true} if the position is blocked, {@code false} otherwise
     */
    @StudentImplementationRequired
    public boolean isBlocked(Point p, DirectionVector d) {
        if (isOutside(p, d)) {
            return true;
        }

        Point next = d.getMovement(p);

        // Get direction vector from p to next
        Point delta = new Point(next.x - p.x, next.y - p.y);
        DirectionVector dir = DirectionVector.from(delta);

        return switch (dir) {
            //  The wall is on the top of the field, that's why we need to check the bottom of the field
            case UP -> isBlocked(p.x, p.y, true);
            // The wall is on the right of the field, that's why we need to check the left of the field
            case RIGHT -> isBlocked(p.x, p.y, false);
            case DOWN -> isBlocked(p.x, next.y, true);
            case LEFT -> isBlocked(next.x, p.y, false);
        };
    }
}
