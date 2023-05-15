package h06.ui;

import h06.problems.ProblemSolver;
import h06.world.World;

import java.awt.Point;

/**
 * Visualizes a problem.
 *
 * @author Nhan Huynh
 */
public interface ProblemVisualizer {

    /**
     * Initializes the visualizer with the given world to visualize.
     *
     * @param world the world to visualize
     */
    void init(World world);

    /**
     * Shows or hides the visualizer.
     *
     * @param visible whether the visualizer should be visible
     */
    void show(boolean visible);

    /**
     * Runs the visualizer with the given solver and start and end points to solve the problem.
     *
     * @param solver the solver to use
     * @param s      the start point
     * @param e      the end point
     */
    void run(ProblemSolver solver, Point s, Point e);

    /**
     * Sets the delay between each step in milliseconds.
     *
     * @param delay the delay between each step in milliseconds
     */
    void setDelay(int delay);
}
