package h06;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import h06.serialization.PointArrayDeserializer;
import h06.world.World;

import java.awt.Point;


public class WorldProperties {

    public int width;


    public int height;

    @JsonDeserialize(using = PointArrayDeserializer.class)
    public Point[] horizontal;


    @JsonDeserialize(using = PointArrayDeserializer.class)
    public Point[] vertical;

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

