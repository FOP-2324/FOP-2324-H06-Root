package h06;

import com.fasterxml.jackson.databind.node.ArrayNode;
import h06.mock.TestWorld;
import h06.problems.MazeSolver;
import h06.problems.MazeSolverIterative;
import h06.problems.MazeSolverRecursive;
import h06.world.DirectionVector;
import h06.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static h06.TutorUtils.assertIterative;
import static h06.TutorUtils.buildWorldContext;
import static h06.TutorUtils.getMethodLink;
import static h06.TutorUtils.getTypeLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for {@link MazeSolverIterative}.
 */
@DisplayName("H4 | MazeSolverIterative")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestForSubmission
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H4_MazeSolverIterativeTest {

    /**
     * The {@link TypeLink} to {@link MazeSolverRecursive} used for context information.
     *
     * @return the {@link TypeLink} to {@link MazeSolverRecursive}.
     */
    private TypeLink getType() {
        return getTypeLink(Package.PROBLEMS, MazeSolverIterative.class);
    }

    /**
     * The {@link MethodLink} to a {@link MazeSolverRecursive} method used for context information.
     *
     * @param name the name of the method.
     * @return the {@link MethodLink} to a {@link MazeSolverRecursive} method.
     */
    private MethodLink getMethod(String name) {
        return getMethodLink(Package.PROBLEMS, getType().reflection(), name);
    }

    /**
     * Defines unit tests for {@link MazeSolverIterative#nextStep(World, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("H4.1 | nextStep(World, Point, DirectionVector)")
    public class NextStepTest {

        /**
         * The {@link MazeSolver} to test.
         */
        private final MazeSolver solver = new MazeSolverIterative();

        /**
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly.
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        private void assertNextStep(WorldProperties properties, Point p, DirectionVector d, DirectionVector expected) {
            MethodLink method = getMethod("nextStep");
            World world = properties.createWorld(TestWorld::new);
            DirectionVector actual = solver.nextStep(world, p, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
                .add("p", p)
                .add("d", d)
                .add("Expected", expected)
                .add("Actual", actual)
                .build();

            assertEquals(
                expected, actual, context,
                result -> "MazeSolverRecursive#nextStep(%s, %s, %s) should return %s, but was %s."
                    .formatted(world, p, d, expected, actual)
            );
        }

        /**
         * Tests whether {@link MazeSolverIterative#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for simple inputs. (Only top and down)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("25 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen oben und "
            + "unten korrekte Werte zurück.")
        @JsonClasspathSource(value = {
            "MazeSolver/nextStep/up.json",
            "MazeSolver/nextStep/down.json",
        })
        public void testUpDown(
            @Property("properties") WorldProperties properties,
            @Property("p") Point p,
            @Property("d") DirectionVector d,
            @Property("expected") DirectionVector expected) {
            assertNextStep(properties, p, d, expected);
        }

        /**
         * Tests whether {@link MazeSolverIterative#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for simple inputs. (Only left and right)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("26 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen links und "
            + "rechts korrekte Werte zurück.")
        @JsonClasspathSource(value = {
            "MazeSolver/nextStep/left.json",
            "MazeSolver/nextStep/right.json",
        })
        public void testLeftRight(
            @Property("properties") WorldProperties properties,
            @Property("p") Point p,
            @Property("d") DirectionVector d,
            @Property("expected") DirectionVector expected) {
            assertNextStep(properties, p, d, expected);
        }

        /**
         * Tests whether {@link MazeSolverIterative#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for complex inputs. (Only top and down)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("27 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen oben und "
            + "unten korrekte Werte zurück.")
        @JsonClasspathSource(value = {
            "MazeSolver/nextStep/up_complex.json",
            "MazeSolver/nextStep/down_complex.json",
        })
        public void testUpDownComplex(
            @Property("properties") WorldProperties properties,
            @Property("p") Point p,
            @Property("d") DirectionVector d,
            @Property("expected") DirectionVector expected) {
            assertNextStep(properties, p, d, expected);
        }

        /**
         * Tests whether {@link MazeSolverIterative#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for complex inputs. (Only left and right)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("28 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen links und "
            + "rechts korrekte Werte zurück.")
        @JsonClasspathSource(value = {
            "MazeSolver/nextStep/left_complex.json",
            "MazeSolver/nextStep/right_complex.json",
        })
        public void testLeftRightComplex(
            @Property("properties") WorldProperties properties,
            @Property("p") Point p,
            @Property("d") DirectionVector d,
            @Property("expected") DirectionVector expected) {
            assertNextStep(properties, p, d, expected);
        }

        @DisplayName("29 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = ((BasicMethodLink) getMethod("nextStep"));
            Context context = contextBuilder().subject(method).build();
            assertIterative(method.getCtElement(), "MazeSolverIterative#nextStep(World, Point, DirectionVector)", context);
        }
    }

    @Nested
    @DisplayName("H4.2 | numberOfSteps(World, Point, Point, Direction)")
    public class NumberOfStepsTest {

        /**
         * The {@link MazeSolver} to test.
         */
        private final MazeSolver solver = new TestNextStepWorld();

        /**
         * Returns the method for context information.
         *
         * @return the method for context information
         */
        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H4_MazeSolverIterativeTest.this.getMethod("numberOfSteps");
        }

        /**
         * Tests whether {@link MazeSolverIterative#numberOfSteps(World, Point, Point, DirectionVector)} computes
         * the number of steps.
         */
        @DisplayName("30 | numberOfSteps(World, Point, Point, Direction) enthält eine Variable, die die Anzahl der "
            + "bisherigen berechneten Schritte merkt.")
        @Test
        public void testCounterVariable() {
            BasicMethodLink method = getMethod();
            List<CtVariable<?>> variables = Stream.concat(
                    method.getCtElement().getParent().filterChildren(it -> it instanceof CtField<?>)
                        .<CtVariable<?>>list()
                        .stream(),
                    method.getCtElement().filterChildren(it -> it instanceof CtVariable<?>)
                        .<CtVariable<?>>list()
                        .stream()
                )
                .filter(it -> it.getType().getActualClass().equals(int.class))
                .toList();
            Context context = contextBuilder().subject(method)
                .add("Local variables", variables)
                .build();
            assertFalse(
                variables.isEmpty(), context,
                result -> "numberOfSteps(World, Point, Point, Direction) should at least contain one local variable "
                    + "for computing the total number of steps, found %s".formatted(variables));
        }

        /**
         * Tests whether {@link MazeSolverIterative#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        private void assertNumberOfSteps(
            WorldProperties properties,
            Point s,
            Point e,
            DirectionVector d,
            int expected
        ) {
            MethodLink method = getMethod();
            World world = properties.createWorld(TestWorld::new);
            int actual = solver.numberOfSteps(world, s, e, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
                .add("s", s)
                .add("e", e)
                .add("Expected", expected)
                .add("Actual", actual)
                .build();

            assertEquals(
                expected, actual, context,
                result -> "MazeSolverIterative#numberOfSteps(%s, %s, %s, %s) should return %s, but was %s"
                    .formatted(world, s, e, d, expected, actual)
            );
        }

        /**
         * Tests whether {@link MazeSolverIterative#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("31 | numberOfSteps(World, Point, Point, Direction) gibt die korrekte Anzahl an Schritten "
            + "zurück, wenn Start- und Endpunkt gleich sind.")
        @JsonClasspathSource(value = {
            "MazeSolver/numberOfSteps/path_se.json",
        })
        public void testStartEndEqual(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") int expected
        ) {
            assertNumberOfSteps(properties, s, e, d, expected);
        }

        /**
         * Tests whether {@link MazeSolverIterative#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("32 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten zurück, wenn "
            + "der Pfad nur aus Start- und Endpunt besteht.")
        @JsonClasspathSource(value = {
            "MazeSolver/numberOfSteps/path_spluse.json",
        })
        public void testPathStartEnd(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") int expected
        ) {
            assertNumberOfSteps(properties, s, e, d, expected);
        }

        /**
         * Tests whether {@link MazeSolverIterative#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("33 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten für komplexere "
            + "Pfade zurück.")
        @JsonClasspathSource(value = {
            "MazeSolver/numberOfSteps/path_complex1.json",
            "MazeSolver/numberOfSteps/path_complex2.json",
            "MazeSolver/numberOfSteps/path_complex3.json",
        })
        public void testComplexPath(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") int expected
        ) {
            assertNumberOfSteps(properties, s, e, d, expected);
        }

        @DisplayName("34 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = getMethod();
            Context context = contextBuilder().subject(method).build();
            assertIterative(method.getCtElement(), "MazeSolverIterative#numberOfSteps(World, Point, Point))", context);
        }
    }

    /**
     * Defines unit tests for {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("H4.3 | solve(World, Point, Point, Direction)")
    public class SolveTest {

        /**
         * The {@link MazeSolver} to test.
         */
        private final MazeSolver solver = new TestSolverWorld();

        /**
         * Returns the method for context information.
         *
         * @return the method
         */
        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H4_MazeSolverIterativeTest.this.getMethod("solve");
        }

        /**
         * Tests whether {@link MazeSolverIterative#solve(World, Point, Point, DirectionVector)} returns the correct
         * number of steps.
         */
        @DisplayName("35 | solve(World, Point, Point, Direction) berechnet die korrekte Anzahl an Schritten und "
            + "speichert dies entsprechend ab.")
        @Test
        public void testComputeArraySize() {
            BasicMethodLink method = getMethod();
            List<? extends CtExecutableReference<?>> calls = method.getCtElement()
                .filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> (CtInvocation<?>) it)
                .map(CtAbstractInvocation::getExecutable)
                .toList();
            List<? extends CtExecutableReference<?>> found = calls.stream()
                .filter(it -> it.getSimpleName().equals("numberOfSteps"))
                .toList();
            Context context = contextBuilder().subject(method)
                .add("Method invocations", calls)
                .build();
            assertFalse(
                found.isEmpty(), context,
                result -> "solve(World, Point, Point, Direction) should at least call numberOfSteps once, found %s"
                    .formatted(found.size()));
        }

        /**
         * Tests whether {@link MazeSolverIterative#solve(World, Point, Point, DirectionVector)} returns the correct
         * array size.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("36 | solve(World, Point, Point, Direction) gibt ein Array zurück, dessen Länge der Anzahl der "
            + "Schritte entspricht.")
        @JsonClasspathSource(value = {
            "MazeSolver/solve/path_complex1.json",
            "MazeSolver/solve/path_complex2.json",
            "MazeSolver/solve/path_complex3.json",
        })
        public void testCorrectArraySize(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") ArrayNode node
        ) {
            Point[] expected = new Point[node.size()];
            for (int i = 0; i < node.size(); i++) {
                expected[i] = new Point(node.get(i).get("x").asInt(), node.get(i).get("y").asInt());
            }
            MethodLink method = getMethod();
            World world = properties.createWorld(TestWorld::new);
            Point[] actual = solver.solve(world, s, e, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
                .add("s", s)
                .add("e", e)
                .add("d", d)
                .add("Expected", expected.length)
                .add("Actual", actual.length)
                .build();

            assertEquals(expected.length, actual.length, context,
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should return an array of size %s, but was %s"
                    .formatted(world, s, e, d, expected.length, actual.length));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * start and end point.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("37 | solve(World, Point, Point, Direction) gibt ein Array zurück, das die Start- und Endpunkt "
            + "korrekt enthält.")
        @JsonClasspathSource(value = {
            "MazeSolver/solve/path_complex1.json",
            "MazeSolver/solve/path_complex2.json",
            "MazeSolver/solve/path_complex3.json",
        })
        public void testContainsStartEnd(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") ArrayNode node
        ) {
            Point[] expected = new Point[node.size()];
            for (int i = 0; i < node.size(); i++) {
                expected[i] = new Point(node.get(i).get("x").asInt(), node.get(i).get("y").asInt());
            }
            MethodLink method = getMethod();
            World world = properties.createWorld(TestWorld::new);
            Point[] actual = solver.solve(world, s, e, d);

            Context.Builder<?> context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
                .add("s", s)
                .add("e", e)
                .add("d", d)
                .add("Expected", expected[0])
                .add("Actual", actual[0]);

            assertEquals(expected[0], actual[0], context.build(),
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the start point %s, but was %s"
                    .formatted(world, s, e, d, expected[0], actual[0]));

            context.add("Expected", expected[expected.length - 1])
                .add("Actual", actual[actual.length - 1]);
            assertEquals(expected[0], actual[0], context.build(),
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the end point %s, but was %s"
                    .formatted(world, s, e, d, expected[expected.length - 1], actual[actual.length - 1]));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * points.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("38 | solve(World, Point, Point, Direction) gibt ein Array zurück, das alle Punkte des Pfades "
            + "korrekt enthält. (Ausgenommen Start- und Endpunkt)")
        @JsonClasspathSource(value = {
            "MazeSolver/solve/path_complex1.json",
            "MazeSolver/solve/path_complex2.json",
            "MazeSolver/solve/path_complex3.json",
        })
        public void testContainsAll(
            @Property("properties") WorldProperties properties,
            @Property("s") Point s,
            @Property("e") Point e,
            @Property("d") DirectionVector d,
            @Property("expected") ArrayNode node
        ) {
            Point[] expected = new Point[node.size()];
            for (int i = 0; i < node.size(); i++) {
                expected[i] = new Point(node.get(i).get("x").asInt(), node.get(i).get("y").asInt());
            }

            MethodLink method = getMethod();
            World world = properties.createWorld(TestWorld::new);
            Point[] actual = solver.solve(world, s, e, d);

            for (int i = 1; i < expected.length - 1; i++) {
                Context context = contextBuilder().subject(method)
                    .add(buildWorldContext(properties))
                    .add("s", s)
                    .add("e", e)
                    .add("d", d)
                    .add("Expected", Arrays.toString(expected))
                    .add("Actual", Arrays.toString(actual))
                    .add("Expected at %s".formatted(i), expected[i])
                    .add("Actual at %s".formatted(i), actual[i])
                    .build();
                int index = i;
                assertEquals(expected[i], actual[i], context,
                    result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the point %s at %s, but was %s"
                        .formatted(world, s, e, d, expected[index], index, actual[index]));
            }
        }

        @DisplayName("39 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = getMethod();
            Context context = contextBuilder().subject(method).build();
            assertIterative(method.getCtElement(), "MazeSolverIterative#solve(World, Point, Point, Direction)", context);
        }
    }

    /**
     * Used to make testing of implementation independent of next step.
     */
    @SkipCheck
    private static class TestNextStepWorld extends MazeSolverIterative {

        @Override
        public DirectionVector nextStep(World world, Point p, DirectionVector d) {
            DirectionVector next = TutorUtils.rotate270(d);
            for (int i = 0; i < DirectionVector.values().length; i++) {
                if (!world.isBlocked(p, next)) {
                    return next;
                }
                next = TutorUtils.rotate90(next);
            }
            return d;
        }
    }

    /**
     * Used to make testing of implementation independent of the next step and number of steps.
     */
    @SkipCheck
    private static class TestSolverWorld extends TestNextStepWorld {

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
    }
}
