package h06.world;

import fopbot.Field;
import fopbot.FieldEntity;

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
        fields[x][y].getEntities().add(new Wall(x, y, horizontal));
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
    public boolean isBlocked(Point p, DirectionVector d) {
        // Outside
        if (isOutside(p, d)) {
            return true;
        }
        Point r = d.getMovement(p);
        int x;
        int y;
        boolean horizontal;
        // 1 point for vertical
        // 1 point for special case
        if (p.x > r.x) {
            // Right, vertical check
            x = r.x;
            y = r.y;
            horizontal = false;
        } else if (p.x < r.x) {
            // Left vertical check, the wall is on the right of the field, that's why we need to check the left of
            // the field
            x = r.x - 1;
            y = r.y;
            if (x < 0) {
                return false;
            }
            horizontal = false;
        } else if (p.y > r.y) {
            // Down horizontal check
            x = r.x;
            y = r.y;
            horizontal = true;
        } else {
            // Up horizontal check, the wall is on the top of the field, that's why we need to check the bottom of
            // the field
            x = r.x;
            y = r.y - 1;
            horizontal = true;
            if (y < 0) {
                return false;
            }
        }
        return isBlocked(x, y, horizontal);
    }
}
