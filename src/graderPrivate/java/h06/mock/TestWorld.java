package h06.mock;

import h06.world.DirectionVector;
import h06.world.World;

import java.awt.Point;

/**
 * A test world with correct implementation used to check method independently.
 *
 * @author Nhan Huynh
 */
public class TestWorld extends World {

    /**
     * Constructs a new world with the given size.
     *
     * @param width  the width of the world
     * @param height the height of the world
     */
    public TestWorld(int width, int height) {
        super(width, height);
    }

    @Override
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
