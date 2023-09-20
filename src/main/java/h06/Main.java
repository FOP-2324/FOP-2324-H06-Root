package h06;

import h06.problems.MazeSolverRecursive;
import h06.problems.ProblemSolver;
import h06.ui.MazeVisualizer;
import h06.ui.ProblemVisualizer;
import h06.world.DirectionVector;
import h06.world.World;

import java.awt.Point;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        World world = new World(5, 5);
        Point start = new Point(2, 0);
        Point end = new Point(2, 4);
        DirectionVector direction = DirectionVector.UP;

        world.placeWall(0, 0, false);
        world.placeWall(0, 1, false);
        world.placeWall(0, 2, false);
        world.placeWall(0, 3, false);
        world.placeWall(1, 3, false);
        world.placeWall(1, 4, false);
        world.placeWall(2, 0, false);
        world.placeWall(3, 1, false);
        world.placeWall(3, 3, false);

        world.placeWall(1, 1, true);
        world.placeWall(2, 0, true);
        world.placeWall(2, 1, true);
        world.placeWall(2, 2, true);
        world.placeWall(3, 1, true);
        world.placeWall(3, 3, true);
        world.placeWall(4, 2, true);


        ProblemVisualizer visualizer = new MazeVisualizer();
        ProblemSolver solver = new MazeSolverRecursive();
        visualizer.init(world);
        visualizer.show();
        visualizer.run(solver, start, end, direction);
    }
}
