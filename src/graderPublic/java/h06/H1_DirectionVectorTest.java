package h06;

import h06.world.DirectionVector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import static h06.TutorUtils.getMethodLink;
import static h06.TutorUtils.getTypeLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for {@link DirectionVector}.
 *
 * @author Nhan Huynh
 */
@DisplayName("H1 | DirectionVector")
public class H1_DirectionVectorTest {


    /**
     * Defines unit tests for the method {@link DirectionVector#rotate270()}.
     */
    @DisplayName("rotate270()")
    @Nested
    public class Rotate270Test {

        /**
         * Tests whether the input vector is rotated correctly.
         *
         * @param input    the input vector
         * @param expected the expected vector
         */
        @ParameterizedTest(name = "Input: {0}")
        @DisplayName("01 | Methode rotate270() gibt in allen Fällen den korrekten Vektor zurück.")
        @JsonClasspathSource(value = {
            "DirectionVector/rotate270/up.json",
            "DirectionVector/rotate270/right.json",
            "DirectionVector/rotate270/down.json",
            "DirectionVector/rotate270/left.json",
        })
        public void testRotate270(
            @Property("input") DirectionVector input,
            @Property("expected") DirectionVector expected
        ) {
            TypeLink type = getTypeLink(Package.WORLD, DirectionVector.class);
            MethodLink method = getMethodLink(Package.WORLD, DirectionVector.class, "rotate270");
            DirectionVector actual = input.rotate270();
            Context context = contextBuilder().subject(method)
                .add("Input", input)
                .add("Expected", expected)
                .add("Actual", actual)
                .build();
            assertEquals(
                expected, actual, context,
                result -> "DirectionVector#rotate270() should return %s, but was %s.".formatted(expected, result)
            );
        }
    }

    /**
     * Defines unit tests for the method {@link DirectionVector#rotate90()}.
     */
    @DisplayName("rotate90()")
    @Nested
    public class Rotate90Test {

        /**
         * Tests whether the input vector is rotated correctly.
         *
         * @param input    the input vector
         * @param expected the expected vector
         */
        @ParameterizedTest(name = "Input: {0}")
        @DisplayName("02 | Methode rotate90() gibt in allen Fällen den korrekten Vektor zurück.")
        @JsonClasspathSource(value = {
            "DirectionVector/rotate90/up.json",
            "DirectionVector/rotate90/right.json",
            "DirectionVector/rotate90/down.json",
            "DirectionVector/rotate90/left.json",
        })
        public void testRotate90(
            @Property("input") DirectionVector input,
            @Property("expected") DirectionVector expected
        ) {
            MethodLink method = getMethodLink(Package.WORLD, DirectionVector.class, "rotate90");
            DirectionVector actual = input.rotate90();
            Context context = contextBuilder().subject(method)
                .add("Input", input)
                .add("Expected", expected)
                .add("Actual", actual)
                .build();
            assertEquals(
                expected, actual, context,
                result -> "DirectionVector#rotate90() should return %s, but was %s.".formatted(expected, actual)
            );
        }
    }
}
