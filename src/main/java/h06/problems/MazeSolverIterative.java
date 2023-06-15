package h06.problems;

import h06.world.DirectionVector;
import h06.world.World;

import java.awt.Point;

/**
 * An iterative implementation of a maze solver. The solver uses the left-hand rule to find a path from the start to
 * the end of the maze.
 *
 * @author Nhan Huynh
 */
public class MazeSolverIterative implements MazeSolver {

    /**
     * Constructs an iterative maze solver.
     */
    public MazeSolverIterative() {
    }

    @Override
    public DirectionVector nextStep(World world, Point p, DirectionVector d) {
        DirectionVector next = d.rotate270();
        for (int i = 0; i < DirectionVector.values().length; i++) {
            if (!world.isBlocked(p, next)) {
                return next;
            }

            next = next.rotate90();
        }
        return d;
    }

    @Override
    public int numberOfSteps(World world, Point s, Point e, DirectionVector d) {
        int steps = 0;
        Point next = s;
        DirectionVector nextDir = d;
        while (!next.equals(e)) {
            nextDir = nextStep(world, next, nextDir);
            next = nextDir.getMovement(next);
            steps++;
        }
        steps++;
        return steps;
    }

    @Override
    public Point[] solve(World world, Point s, Point e, DirectionVector d) {
        int size = numberOfSteps(world, s, e, d);
        Point[] path = new Point[size];
        int index = 0;
        Point next = s;
        DirectionVector dir = d;
        while (!next.equals(e)) {
            path[index++] = next;
            dir = nextStep(world, next, dir);
            next = dir.getMovement(next);
        }
        path[index] = next;
        return path;
    }
}
