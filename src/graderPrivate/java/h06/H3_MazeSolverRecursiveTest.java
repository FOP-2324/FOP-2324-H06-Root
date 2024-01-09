package h06;

import com.fasterxml.jackson.databind.node.ArrayNode;
import h06.mock.TestWorld;
import h06.problems.MazeSolver;
import h06.problems.MazeSolverRecursive;
import h06.world.DirectionVector;
import h06.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.reference.CtExecutableReference;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static h06.TutorUtils.assertRecursive;
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
@TestForSubmission
@TestMethodOrder(MethodOrderer.DisplayName.class)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H3_MazeSolverRecursiveTest {

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
    @DisplayName("H3.1 | nextStep(World, Point, DirectionVector)")
    public class NextStepTest {

        /**
         * The {@link MazeSolver} to test.
         */
        private final MazeSolver solver = new MazeSolverRecursive();

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
         * Tests whether {@link MazeSolverRecursive#nextStep(World, Point, DirectionVector)} computes the next step
         * correctly for simple inputs. (Only top and down)
         *
         * @param properties the properties of the world
         * @param p          the point to test
         * @param d          the direction to test
         * @param expected   the expected result
         */
        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("09 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen oben und "
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
        @DisplayName("10 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen links und "
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
        @DisplayName("11 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen oben und "
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
        @DisplayName("12 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen links und "
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

        @DisplayName("13 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = ((BasicMethodLink) getMethod("nextStep"));
            Context.Builder<?> context = contextBuilder().subject(method);
            int conds = method.getCtElement().filterChildren(it -> it instanceof CtConditional<?>).list().size();


            assertEquals(1, conds, context.build(),
                result -> "MazeSolverRecursive#nextStep(World, Point, DirectionVector) should contain exactly one "
                    + "conditional statement, but found %s".formatted(conds));
            List<CtReturn<?>> returns = method.getCtElement().filterChildren(it -> it instanceof CtReturn<?>)
                .list();
            CtExpression<?> expression = method.getCtElement().filterChildren(it -> it instanceof CtReturn<?>)
                .<CtReturn<?>>list().get(0).getReturnedExpression();
            boolean condRet = expression instanceof CtConditional<?>;
            boolean condAndVarRead = expression instanceof CtVariableRead<?>
                && !method.getCtElement().filterChildren(it -> it instanceof CtConditional<?>).list().isEmpty();
            boolean condAndAssign = expression instanceof CtAssignment<?, ?> assignment
                && assignment.getAssignment() instanceof CtConditional<?>;

            assertTrue(condRet || condAndVarRead || condAndAssign, context.build(),
                result -> "MazeSolverRecursive#nextStep(World, Point, DirectionVector) should contain exactly one "
                    + "conditional statement, but found %s"
                    .formatted(returns.stream().map(CtReturn::getReturnedExpression).toList()));
            List<? extends CtExecutableReference<?>> calls = method.getCtElement()
                .filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> (CtInvocation<?>) it)
                .map(CtAbstractInvocation::getExecutable)
                .filter(it -> {
                    String name = it.getSimpleName();
                    return !name.equals("isBlocked")
                        && !name.equals("nextStep")
                        && !name.equals("rotate90")
                        && !name.equals("rotate270");
                })
                .toList();
            assertTrue(calls.isEmpty(), context.build(),
                result -> "MazeSolverRecursive#nextStep(World, Point, DirectionVector) should not contain any "
                    + "invocations, but found %s".formatted(calls));
            assertRecursive(method, "MazeSolverRecursive#nextStep(World, Point, DirectionVector)", context,
                BasicStringMatchers.identical("rotate270"),
                BasicStringMatchers.identical("rotate90"),
                BasicStringMatchers.identical("isBlocked")
            );
        }
    }

    /**
     * Defines unit tests for the method {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("H3.2 | numberOfSteps(World, Point, Point, Direction)")
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
            return (BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("numberOfSteps");
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} computes
         * the number of steps.
         */
        @DisplayName("14 | numberOfSteps(World, Point, Point, Direction) berechnet die Anzahl der Schritte im "
            + "Rekursionsschritt.")
        @Test
        public void testCounting() {
            BasicMethodLink method = getMethod();
            List<CtBinaryOperator<?>> binaryOps = method.getCtElement()
                .filterChildren(it -> it instanceof CtBinaryOperator<?>)
                .list();
            Context context = contextBuilder().subject(method).build();
            Consumer<CtExpression<?>> assertLiteral = it -> assertTrue(it instanceof CtLiteral<?>, context,
                result -> "Expected literal, but was %s.".formatted(it.getClass().getSimpleName()));

            BiConsumer<CtBinaryOperator<?>, Consumer<CtExpression<?>>> assertOp = (op, assertion) -> {
                try {
                    assertion.accept(op.getLeftHandOperand());
                } catch (AssertionFailedError e) {
                    assertion.accept(op.getRightHandOperand());
                }
            };

            for (CtBinaryOperator<?> op : binaryOps) {
                assertOp.accept(op, assertLiteral);
                break;
            }
        }

        /**
         * Tests whether {@link MazeSolverRecursive#numberOfSteps(World, Point, Point, DirectionVector)} contains
         * a variable that keeps track of the number of steps.
         */
        @DisplayName("15 | numberOfSteps(World, Point, Point, Direction) enthält eine Variable, die die Anzahl der "
            + "bisherigen berechneten Schritte merkt.")
        @Test
        public void testNextStep() {
            BasicMethodLink method = getMethod();
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
        @DisplayName("16 | numberOfSteps(World, Point, Point, Direction) gibt die korrekte Anzahl an Schritten "
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
        @DisplayName("17 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten zurück, wenn "
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
        @DisplayName("18 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten für komplexere "
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

        @DisplayName("19 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = ((BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("numberOfSteps"));
            Context.Builder<?> context = contextBuilder().subject(method);
            assertRecursive(method, "MazeSolverRecursive#numberOfSteps(World, Point, Point)", context,
                BasicStringMatchers.identical("equals"),
                BasicStringMatchers.identical("nextStep"),
                BasicStringMatchers.identical("getMovement"),
                BasicStringMatchers.identical("from")
            );
        }
    }

    /**
     * Defines unit tests for {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)}.
     */
    @Nested
    @DisplayName("H3.3 | solve(World, Point, Point, Direction)")
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
            return (BasicMethodLink) H3_MazeSolverRecursiveTest.this.getMethod("solve");
        }

        /**
         * Tests whether {@link MazeSolverRecursive#solve(World, Point, Point, DirectionVector)} returns the correct
         * number of steps.
         */
        @DisplayName("20 | solve(World, Point, Point, Direction) berechnet die korrekte Anzahl an Schritten und "
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
        @DisplayName("20 | solve(World, Point, Point, Direction) gibt ein Array zurück, dessen Länge der Anzahl der "
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
        @DisplayName("21 | solve(World, Point, Point, Direction) gibt ein Array zurück, das die Startpunkt korrekt "
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
            World world = properties.createWorld(TestWorld::new);
            Point[] actual = solver.solve(world, s, e, d);

            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
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
        @DisplayName("22 | solve(World, Point, Point, Direction) gibt ein Array zurück, das den Endpunkt korrekt "
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
            World world = properties.createWorld(TestWorld::new);
            Point[] actual = solver.solve(world, s, e, d);

            Context context = contextBuilder().subject(method)
                .add(buildWorldContext(properties))
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
        @DisplayName("23 | solve(World, Point, Point, Direction) gibt ein Array zurück, das alle Punkte des Pfades "
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
                    result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the point %s at %s, but was %s."
                        .formatted(world, s, e, d, expected[index], index, actual[index]));
            }
        }

        @DisplayName("24 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = getMethod();
            Context.Builder<?> context = contextBuilder().subject(method);
            assertRecursive(method, "MazeSolverRecursive#solve(World, Point, Point, Direction))", context,
                BasicStringMatchers.identical("equals"),
                BasicStringMatchers.identical("numberOfSteps"),
                BasicStringMatchers.identical("nextStep"),
                BasicStringMatchers.identical("getMovement"),
                BasicStringMatchers.identical("from")
            );
        }
    }

    /**
     * Used to make testing of implementation independent of next step.
     */
    @SkipCheck
    private static class TestNextStepWorld extends MazeSolverRecursive {

        @Override
        public DirectionVector nextStep(World world, Point p, DirectionVector d) {
            return !world.isBlocked(p, TutorUtils.rotate270(d))
                ? TutorUtils.rotate270(d) :
                nextStep(world, p, TutorUtils.rotate90(d));
        }
    }

    /**
     * Used to make testing of implementation independent of the next step and number of steps.
     */
    @SkipCheck
    private static class TestSolverWorld extends TestNextStepWorld {

        @Override
        public int numberOfSteps(World world, Point s, Point e, DirectionVector d) {
            if (s.equals(e)) {
                return 1;
            }
            DirectionVector next = nextStep(world, s, d);
            return 1 + numberOfSteps(world, next.getMovement(s), e, next);
        }
    }
}
