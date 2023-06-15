package h06;

import h06.world.DirectionVector;
import h06.world.World;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;

import java.awt.Point;

import static h06.TutorUtils.buildWorldContext;
import static h06.TutorUtils.getMethodLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@DisplayName("H2 | World")
public class H2_WorldTest {

    private void assertIsBlocked(WorldProperties properties, Point p, DirectionVector d, boolean expected) {
        MethodLink method = getMethodLink(
            Package.WORLD,
            World.class,
            "isBlocked",
            BasicReflectionMatchers.sameTypes(BasicTypeLink.of(Point.class), BasicTypeLink.of(DirectionVector.class))
        );

        World world = properties.createWorld();
        boolean actual = world.isBlocked(p, d);

        Context context = contextBuilder().subject(method)
            .add(buildWorldContext(properties, world))
            .build();

        assertEquals(
            expected, actual, context,
            result -> "World#isBlocked(%s, %s) should return %s, but was %s.".formatted(p, d, expected, result)
        );
    }


    @DisplayName("isBlocked(Point, DirectionVector)")
    @Nested
    public class IsBlockedTest {

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("03 | isBlocked(Point, DirectionVector) gibt für Koordinaten außerhalb der Welt false true.")
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

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("04 | isBlocked(Point, DirectionVector) gibt für Koordinaten innerhalb der Welt mit "
            + "Richtungsvektor rechts und unten korrekte Werte zurück.")
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

        @ParameterizedTest(name = "Koordinate: {1}, Richtung: {2}")
        @DisplayName("05 | isBlocked(Point, DirectionVector) gibt für Koordinaten innerhalb der Welt mit "
            + "Richtungsvektor links und oben korrekte Werte zurück.")
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
    }
}
