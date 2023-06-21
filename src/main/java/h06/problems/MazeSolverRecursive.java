package h06.problems;

import h06.world.DirectionVector;
import h06.world.World;

import java.awt.Point;

/**
 * A recursive implementation of a maze solver. The solver uses the left-hand rule to find a path from the start to
 * the end of the maze.
 *
 * @author Nhan Huynh
 */
public class MazeSolverRecursive implements MazeSolver {

    /**
     * Constructs a recursive maze solver.
     */
    public MazeSolverRecursive() {
    }

    @Override
    public DirectionVector nextStep(World world, Point p, DirectionVector d) {
        return !world.isBlocked(p, d) ? d : nextStep(world, p, d.rotate90());
    }

    @Override
    public int numberOfSteps(World world, Point s, Point e, DirectionVector d) {
        if (s.equals(e)) {
            return 1;
        }
        DirectionVector next = nextStep(world, s, d.rotate270());
        return 1 + numberOfSteps(world, next.getMovement(s), e, next);
    }

    @Override
    public Point[] solve(World world, Point s, Point e, DirectionVector d) {
        int size = numberOfSteps(world, s, e, d);
        Point[] path = new Point[size];
        solveHelper(world, s, e, d, path, 0);
        return path;
    }

    /**
     * Helper method for solve. Returns the path from p to end.
     *
     * @param world the world to solve the maze in
     * @param p     the current point
     * @param e     the end point
     * @param d     the current direction
     * @param path  the path calculated so far from s to p
     * @param index the index of the next free spot in path
     */
    private void solveHelper(World world, Point p, Point e, DirectionVector d, Point[] path, int index) {
        if (p.equals(e)) {
            path[index] = p;
            return;
        }
        path[index++] = p;
        DirectionVector next = nextStep(world, p, d.rotate270());
        solveHelper(world, next.getMovement(p), e, next, path, index);
    }
}
