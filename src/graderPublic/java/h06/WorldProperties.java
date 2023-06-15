package h06;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import h06.serialization.PointArrayDeserializer;
import h06.world.World;

import java.awt.Point;


/**
 * Defines the properties of the world.
 *
 * @author Nhan Huynh
 */
public class WorldProperties {

    /**
     * The width of the world.
     */
    public int width;

    /**
     * The height of the world.
     */
    public int height;

    /**
     * The horizontal walls.
     */
    @JsonDeserialize(using = PointArrayDeserializer.class)
    public Point[] horizontal;

    /**
     * The vertical walls.
     */
    @JsonDeserialize(using = PointArrayDeserializer.class)
    public Point[] vertical;

    /**
     * Constructs a world with the given properties.
     *
     * @return the world with the given properties
     */
    public World createWorld() {
        World world = new World(width, height);
        for (Point p : horizontal) {
            world.placeWall(p.x, p.y, true);
        }
        for (Point p : vertical) {
            world.placeWall(p.x, p.y, false);
        }
        return world;
    }
}

