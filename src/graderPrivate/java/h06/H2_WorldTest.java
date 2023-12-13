package h06;

import h06.world.DirectionVector;
import h06.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

import static h06.TutorUtils.buildWorldContext;
import static h06.TutorUtils.getMethodLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for {@link World}.
 *
 * @see H1_DirectionVectorTest
 */
@DisplayName("H2 | World")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H2_WorldTest {

    /**
     * Returns the method {@link World#isBlocked(Point, DirectionVector)} used for context information.
     *
     * @return the method {@link World#isBlocked(Point, DirectionVector)} used for context information
     */
    private MethodLink getMethod() {
        return getMethodLink(
            Package.WORLD,
            World.class,
            "isBlocked",
            BasicReflectionMatchers.sameTypes(BasicTypeLink.of(Point.class), BasicTypeLink.of(DirectionVector.class))
        );
    }

    /**
     * Tests whether {@link World#isBlocked(Point, DirectionVector)} returns the expected value.
     *
     * @param properties the properties of the world
     * @param p          the point to test
     * @param d          the direction vector to test
     * @param expected   the expected value
     */
    private void assertIsBlocked(WorldProperties properties, Point p, DirectionVector d, boolean expected) {
        MethodLink method = getMethod();

        World world = properties.createWorld();
        boolean actual = world.isBlocked(p, d);

        Context context = contextBuilder().subject(method)
            .add(buildWorldContext(properties))
            .build();

        assertEquals(
            expected, actual, context,
            result -> "World#isBlocked(%s, %s) should return %s, but was %s.".formatted(p, d, expected, result)
        );
    }


    /**
     * Tests whether {@link World#isBlocked(Point, DirectionVector)} returns the correct value for points outside
     * the world.
     *
     * @param properties the properties of the world
     * @param p          the point to test
     * @param d          the direction vector to test
     */
    @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
    @DisplayName("05 | isBlocked(Point, DirectionVector) gibt für Koordinaten außerhalb der Welt false true.")
    @JsonClasspathSource(value = {
        "World/isBlocked/outside_1.json",
        "World/isBlocked/outside_2.json",
        "World/isBlocked/outside_3.json",
        "World/isBlocked/outside_4.json",
        "World/isBlocked/outside_5.json",
    })
    public void testIsBlockedOutside(
        @Property("properties") WorldProperties properties,
        @Property("p") Point p,
        @Property("d") DirectionVector d) {
        assertIsBlocked(properties, p, d, true);
    }

    /**
     * Tests whether {@link World#isBlocked(Point, DirectionVector)} returns the correct value for points inside
     * the world. (Only direction left and up).
     *
     * @param properties the properties of the world
     * @param p          the point to test
     * @param d          the direction vector to test
     * @param expected   the expected value
     */
    @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
    @DisplayName("06 | isBlocked(Point, DirectionVector) gibt für Koordinaten innerhalb der Welt mit "
        + "Richtungsvektor links und oben korrekte Werte zurück.")
    @JsonClasspathSource(value = {
        "World/isBlocked/leftup_1.json",
        "World/isBlocked/leftup_2.json",
        "World/isBlocked/leftup_3.json",
        "World/isBlocked/leftup_4.json",
        "World/isBlocked/leftup_5.json",
    })
    public void testIsBlockedRightAndDown(
        @Property("properties") WorldProperties properties,
        @Property("p") Point p,
        @Property("d") DirectionVector d,
        @Property("expected") boolean expected) {
        assertIsBlocked(properties, p, d, expected);
    }

    /**
     * Tests whether {@link World#isBlocked(Point, DirectionVector)} returns the correct value for points inside
     * the world. (Only direction right and down).
     *
     * @param properties the properties of the world
     * @param p          the point to test
     * @param d          the direction vector to test
     * @param expected   the expected value
     */
    @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
    @DisplayName("07 | isBlocked(Point, DirectionVector) gibt für Koordinaten innerhalb der Welt mit "
        + "Richtungsvektor rechts und unten korrekte Werte zurück.")
    @JsonClasspathSource(value = {
        "World/isBlocked/rightdown_1.json",
        "World/isBlocked/rightdown_2.json",
        "World/isBlocked/rightdown_3.json",
        "World/isBlocked/rightdown_4.json",
    })
    public void testIsBlockedLeftAndUp(
        @Property("properties") WorldProperties properties,
        @Property("p") Point p,
        @Property("d") DirectionVector d,
        @Property("expected") boolean expected) {
        assertIsBlocked(properties, p, d, expected);
    }

    @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
    @DisplayName("08 | isBlocked(Point, DirectionVector) ruft die Methode isBlocked(int, int, boolean) mit der "
        + "richtigen Wand Orientierung auf.")
    @JsonClasspathSource(value = {
        "World/isBlocked/orientation_left.json",
        "World/isBlocked/orientation_right.json",
        "World/isBlocked/orientation_up.json",
        "World/isBlocked/orientation_down.json",
    })
    public void testOrientation(
        @Property("properties") WorldProperties properties,
        @Property("p") Point p,
        @Property("d") DirectionVector d,
        @Property("expected") boolean expected) {
        MethodLink method = getMethod();

        World world = properties.createWorld();
        boolean actual = world.isBlocked(p, d);

        Context context = contextBuilder().subject(method)
            .add(buildWorldContext(properties))
            .build();

        assertEquals(
            expected, actual, context,
            result -> "World#isBlocked(%s, %s) should call World#isBlocked(int, int, boolean) with".formatted(p, d)
                + "the orientation %s, but was %s.".formatted(expected, actual)
        );
    }

    /**
     * Tests whether {@link World#isBlocked(Point, DirectionVector)} calls the method
     * {@link World#isBlocked(int, int, boolean)} with the correct wall orientation.
     */
    private static class OrientationWorld extends World {

        /**
         * Creates a new world with the given width and height.
         *
         * @param width  the width of the world
         * @param height the height of the world
         */
        public OrientationWorld(int width, int height) {
            super(width, height);
        }

        @Override
        public boolean isBlocked(int x, int y, boolean horizontal) {
            return horizontal;
        }
    }
}
