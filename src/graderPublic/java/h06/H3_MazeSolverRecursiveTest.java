package h06;

import com.fasterxml.jackson.databind.node.ArrayNode;
import h06.problems.MazeSolver;
import h06.problems.MazeSolverIterative;
import h06.problems.MazeSolverRecursive;
import h06.world.DirectionVector;
import h06.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.reference.CtExecutableReference;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static h06.TutorUtils.buildWorldContext;
import static h06.TutorUtils.getMethodLink;
import static h06.TutorUtils.getTypeLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for {@link MazeSolverRecursive}.
 *
 * @author Nhan Huynh
 */
@DisplayName("H3 | MazeSolverRecursive")
public class H3_MazeSolverRecursiveTest {

    /**
     * The {@link MazeSolver} to test.
     */
    private final MazeSolver solver = new MazeSolverIterative();

    /**
     * The {@link TypeLink} to {@link MazeSolverRecursive} used for context information.
     *
     * @return the {@link TypeLink} to {@link MazeSolverRecursive}.
     */
    private TypeLink getType() {
        return getTypeLink(Package.PROBLEMS, MazeSolverRecursive.class);
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
     * Defines unit tests for {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("nextStep(World, Point, DirectionVector)")
    public class NextStepTest {

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
            World world = properties.createWorld();
            DirectionVector actual = solver.nextStep(world, p, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
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
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for simple inputs. (Only top and down)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("06 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen oben und "
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
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for simple inputs. (Only left and right)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("07 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen links und "
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
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for complex inputs. (Only top and down)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("08 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen oben und "
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
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for complex inputs. (Only left and right)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("09 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen links und "
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
    }

    /**
     * Defines unit tests for the method {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("numberOfSteps(World, Point, Point, Direction)")
    public class NumberOfStepsTest {

        /**
         * Returns the method for context information.
         *
         * @return the method for context information
         */
        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("numberOfSteps");
        }

        /**
         * Returns the helper method for context information.
         *
         * @return the helper method for context information
         */
        private BasicMethodLink getHelperMethod() {
            return (BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("numberOfStepsHelper");
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} calls
         * the helper method.
         */
        @DisplayName("10 | numberOfSteps(World, Point, Point, Direction) soll die Aufgabe an numberOfStepsHelper "
            + "delegieren.")
        @Test
        public void testCallHelper() {
            BasicMethodLink method = getMethod();
            List<? extends CtExecutableReference<?>> calls = method.getCtElement()
                .filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> (CtInvocation<?>) it)
                .map(CtAbstractInvocation::getExecutable)
                .toList();
            List<? extends CtExecutableReference<?>> found = calls.stream()
                .filter(it -> it.getSimpleName().equals("numberOfStepsHelper"))
                .toList();
            Context context = contextBuilder().subject(method)
                .add("Method invocations", calls)
                .build();
            assertEquals(
                1, found.size(), context,
                result -> "numberOfSteps(World, Point, Point, Direction) should call numberOfStepsHelper, but found %s."
                    .formatted(found));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} computes
         * the number of steps.
         */
        @DisplayName("11 | numberOfStepsHelper(World, Point, Point, Direction) berechnet die Anzahl der Schritte im "
            + "Rekursionsschritt.")
        @Test
        public void testCounting() {
            BasicMethodLink method = getHelperMethod();
            List<CtBinaryOperator<?>> binaryOps = method.getCtElement()
                .filterChildren(it -> it instanceof CtBinaryOperator<?>)
                .list();
            Context context = contextBuilder().subject(method).build();
            Consumer<CtExpression<?>> assertLiteral = it -> assertTrue(it instanceof CtLiteral<?>, context,
                result -> "Expected literal, but was %s.".formatted(it.getClass().getSimpleName()));
            Consumer<CtExpression<?>> assertInvocation = it -> assertTrue(
                it instanceof CtInvocation<?> call
                    && call.getExecutable().getSimpleName().equals("numberOfStepsHelper"),
                context, result -> "Expected call to numberOfStepsHelper, but was %s."
                    .formatted(it.getClass().getSimpleName())
            );
            BiConsumer<CtBinaryOperator<?>, Consumer<CtExpression<?>>> assertOp = (op, assertion) -> {
                try {
                    assertion.accept(op.getLeftHandOperand());
                } catch (AssertionFailedError e) {
                    assertion.accept(op.getRightHandOperand());
                }
            };

            for (CtBinaryOperator<?> op : binaryOps) {
                assertOp.accept(op, assertLiteral);
                assertOp.accept(op, assertInvocation);
                break;
            }
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} contains
         * a variable that keeps track of the number of steps.
         */
        @DisplayName("12 | numberOfSteps(World, Point, Point, Direction) enthält eine Variable, die die Anzahl der "
            + "bisherigen berechneten Schritte merkt.")
        @Test
        public void testNextStep() {
            BasicMethodLink method = getHelperMethod();
            List<? extends CtExecutableReference<?>> calls = method.getCtElement()
                .filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> (CtInvocation<?>) it)
                .map(CtAbstractInvocation::getExecutable)
                .toList();
            List<? extends CtExecutableReference<?>> found = calls.stream()
                .filter(it -> it.getSimpleName().equals("nextStep"))
                .toList();
            Context context = contextBuilder().subject(method)
                .add("Method invocations", calls)
                .build();
            assertFalse(
                found.isEmpty(), context,
                result -> "numberOfSteps(World, Point, Point, Direction) should at least call numberOfSteps once, "
                    + "found %s.".formatted(found.size()));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        private void assertNumberOfSteps(WorldProperties properties, Point s, Point e, DirectionVector d, int expected) {
            MethodLink method = getMethod();
            World world = properties.createWorld();
            int actual = solver.numberOfSteps(world, s, e, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
                .add("s", s)
                .add("e", e)
                .add("Expected", expected)
                .add("Actual", actual)
                .build();

            assertEquals(
                expected, actual, context,
                result -> "MazeSolverIterative#numberOfSteps(%s, %s, %s, %s) should return %s, but was %s."
                    .formatted(world, s, e, d, expected, actual)
            );
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("13 | numberOfSteps(World, Point, Point, Direction) gibt die korrekte Anzahl an Schritten "
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
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("14 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten zurück, wenn "
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
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} returns the
         * correct number of steps.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param expected   the expected number of steps
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("15 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten für komplexere "
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


    }

    /**
     * Defines unit tests for {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("solve(World, Point, Point, Direction)")
    public class SolveTest {

        /**
         * Returns the method for context information.
         *
         * @return the method
         */
        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("solve");
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * number of steps.
         */
        @DisplayName("16 | solve(World, Point, Point, Direction) berechnet die korrekte Anzahl an Schritten und "
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
                result -> "solve(World, Point, Point, Direction) should at least call numberOfSteps once, found %s."
                    .formatted(found.size()));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * array size.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("17 | solve(World, Point, Point, Direction) gibt ein Array zurück, dessen Länge der Anzahl der "
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
            World world = properties.createWorld();
            Point[] actual = solver.solve(world, s, e, d);
            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
                .add("s", s)
                .add("e", e)
                .add("d", d)
                .add("Expected", expected.length)
                .add("Actual", actual.length)
                .build();

            assertEquals(expected.length, actual.length, context,
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should return an array of size %s, but was %s."
                    .formatted(world, s, e, d, expected.length, actual.length));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * start point.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("18 | solve(World, Point, Point, Direction) gibt ein Array zurück, das die Startpunkt korrekt "
            + "enthält.")
        @JsonClasspathSource(value = {
            "MazeSolver/solve/path_complex1.json",
            "MazeSolver/solve/path_complex2.json",
            "MazeSolver/solve/path_complex3.json",
        })
        public void testContainsStart(
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
            World world = properties.createWorld();
            Point[] actual = solver.solve(world, s, e, d);

            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
                .add("s", s)
                .add("e", s)
                .add("d", d)
                .add("Expected", expected[0])
                .add("Actual", actual[0])
                .build();

            assertEquals(expected[0], actual[0], context,
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the start point %s, but was %s."
                    .formatted(world, s, e, d, expected[0], actual[0]));
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * end point.
         *
         * @param properties the properties of the world
         * @param s          the start point to test
         * @param e          the end point to test
         * @param d          the direction to test
         * @param node       the expected array
         */
        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("19 | solve(World, Point, Point, Direction) gibt ein Array zurück, das den Endpunkt korrekt "
            + "enthält.")
        @JsonClasspathSource(value = {
            "MazeSolver/solve/path_complex1.json",
            "MazeSolver/solve/path_complex2.json",
            "MazeSolver/solve/path_complex3.json",
        })
        public void testContainsEnd(
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
            World world = properties.createWorld();
            Point[] actual = solver.solve(world, s, e, d);

            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
                .add("s", s)
                .add("e", s)
                .add("d", d)
                .add("Expected", expected[expected.length - 1])
                .add("Actual", actual[actual.length - 1])
                .build();

            assertEquals(expected[expected.length - 1], actual[actual.length - 1], context,
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the end point %s, but was %s."
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
        @DisplayName("20 | solve(World, Point, Point, Direction) gibt ein Array zurück, das alle Punkte des Pfades "
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
            World world = properties.createWorld();
            Point[] actual = solver.solve(world, s, e, d);

            for (int i = 1; i < expected.length - 1; i++) {
                Context context = contextBuilder().subject(method)
                    .add(buildWorldContext(properties, world))
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
                    result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the point %s at %s, but was %s."
                        .formatted(world, s, e, d, expected[index], index, actual[index]));
            }
        }
    }
}
