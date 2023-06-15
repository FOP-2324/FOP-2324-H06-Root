package h06;

import com.fasterxml.jackson.databind.node.ArrayNode;
import h06.problems.MazeSolver;
import h06.problems.MazeSolverIterative;
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
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.reference.CtExecutableReference;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import static h06.TutorUtils.buildWorldContext;
import static h06.TutorUtils.getMethodLink;
import static h06.TutorUtils.getTypeLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@DisplayName("H4 | MazeSolverIterative")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class H4_MazeSolverIterativeTest {

    private final MazeSolver solver = new MazeSolverIterative();

    private TypeLink getType() {
        return getTypeLink(Package.PROBLEMS, MazeSolverIterative.class);
    }

    private MethodLink getMethod(String name) {
        return getMethodLink(Package.PROBLEMS, getType().reflection(), name);
    }

    @Nested
    @DisplayName("nextStep(World, Point, DirectionVector)")
    public class NextStepTest {

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
                result -> "MazeSolverIterative#nextStep(%s, %s, %s) should return %s, but was %s"
                    .formatted(world, p, d, expected, actual)
            );
        }

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("20 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen oben und "
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

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("21 | nextStep(World, Point, DirectionVector) für einfache Fälle für die Richtungen links und "
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

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("22 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen oben und "
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

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("23 | nextStep(World, Point, DirectionVector) für komplexere Fälle für die Richtungen links und "
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

    @Nested
    @DisplayName("numberOfSteps(World, Point, Point, Direction)")
    public class NumberOfStepsTest {

        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H4_MazeSolverIterativeTest.this.getMethod("numberOfSteps");
        }

        @DisplayName("24 | numberOfSteps(World, Point, Point, Direction) enthält eine Variable, die die Anzahl der "
            + "bisherigen berechneten Schritte merkt.")
        @Test
        public void testCounterVariable() {
            BasicMethodLink method = getMethod();
            List<CtLocalVariable<?>> variables = method.getCtElement()
                .filterChildren(it -> it instanceof CtLocalVariable<?>)
                .list();
            List<CtLocalVariable<?>> found = variables.stream()
                .filter(it -> it.getType().getActualClass().equals(int.class))
                .toList();
            Context context = contextBuilder().subject(method)
                .add("Local variables", variables)
                .build();
            assertFalse(
                found.isEmpty(), context,
                result -> "numberOfSteps(World, Point, Point, Direction) should at least contain one local variable "
                    + "for computing the total number of steps, found %s".formatted(found.size()));
        }

        private void assertNumberOfSteps(
            WorldProperties properties,
            Point s,
            Point e,
            DirectionVector d,
            int expected
        ) {
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
                result -> "MazeSolverIterative#numberOfSteps(%s, %s, %s, %s) should return %s, but was %s"
                    .formatted(world, s, e, d, expected, actual)
            );
        }

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("26 | numberOfSteps(World, Point, Point, Direction) gibt die korrekte Anzahl an Schritten "
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

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("27 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten zurück, wenn "
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

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("28 | numberOfSteps(World, Point, Point) gibt die korrekte Anzahl an Schritten für komplexere "
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

    @Nested
    @DisplayName("solve(World, Point, Point, Direction)")
    public class SolveTest {

        private BasicMethodLink getMethod() {
            return (BasicMethodLink) H4_MazeSolverIterativeTest.this.getMethod("solve");
        }

        @DisplayName("29 | solve(World, Point, Point, Direction) berechnet die korrekte Anzahl an Schritten und "
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

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("30 | solve(World, Point, Point, Direction) gibt ein Array zurück, dessen Länge der Anzahl der "
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
                result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should return an array of size %s, but was %s"
                    .formatted(world, s, e, d, expected.length, actual.length));
        }

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("31 | solve(World, Point, Point, Direction) gibt ein Array zurück, das die Start- und Endpunkt "
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
            World world = properties.createWorld();
            Point[] actual = solver.solve(world, s, e, d);

            Context.Builder<?> context = contextBuilder().subject(method)
                .add(buildWorldContext(properties, world))
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

        @ParameterizedTest(name = "Startpunkt: {1}, Endpunkt: {2}, Richtung: {3}")
        @DisplayName("32 | solve(World, Point, Point, Direction) gibt ein Array zurück, das alle Punkte des Pfades "
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
                    result -> "MazeSolverIterative#solve(%s, %s, %s, %s) should contain the point %s at %s, but was %s"
                        .formatted(world, s, e, d, expected[index], index, actual[index]));
            }
        }
    }
}
