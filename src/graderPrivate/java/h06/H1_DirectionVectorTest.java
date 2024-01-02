package h06;

import h06.world.DirectionVector;
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
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static h06.TutorUtils.getMethodLink;
import static h06.TutorUtils.getTypeLink;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

/**
 * Defines unit tests for {@link DirectionVector}.
 *
 * @author Nhan Huynh
 */
@DisplayName("H1 | DirectionVector")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H1_DirectionVectorTest {

    /**
     * Defines unit tests for the method {@link DirectionVector#rotate270()}.
     */
    @DisplayName("H1.1 | rotate270()")
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

        @DisplayName("02 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            BasicMethodLink method = ((BasicMethodLink) getMethodLink(Package.WORLD, DirectionVector.class, "rotate270"));
            Context context = contextBuilder().subject(method).build();
            CtExpression<?> expression = method.getCtElement().filterChildren(it -> it instanceof CtReturn<?>)
                .<CtReturn<?>>list().get(0).getReturnedExpression();
            boolean condRet = expression instanceof CtConditional<?>;
            boolean condAndVarRead = expression instanceof CtVariableRead<?>
                && !method.getCtElement().filterChildren(it -> it instanceof CtConditional<?>).list().isEmpty();
            boolean condAndAssign = expression instanceof CtAssignment<?, ?> assignment
                && assignment.getAssignment() instanceof CtConditional<?>;
            assertTrue(condRet || condAndVarRead || condAndAssign, context,
                result -> "DirectionVector#rotate270() should contain exactly one conditional statement, but found none"
            );
        }
    }

    /**
     * Defines unit tests for the method {@link DirectionVector#rotate90()}.
     */
    @DisplayName("H1.2 | rotate90()")
    @Nested
    public class Rotate90Test {

        /**
         * Tests whether the input vector is rotated correctly.
         *
         * @param input    the input vector
         * @param expected the expected vector
         */
        @ParameterizedTest(name = "Input: {0}")
        @DisplayName("03 | Methode rotate90() gibt in allen Fällen den korrekten Vektor zurück.")
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

        @DisplayName("04 | Verbindliche Anforderungen")
        @Test
        public void testRequirements() {
            TypeLink type = getTypeLink(Package.WORLD, DirectionVector.class);
            BasicMethodLink method = ((BasicMethodLink) getMethodLink(Package.WORLD, DirectionVector.class, "rotate90"));
            Context context = contextBuilder().subject(method).build();

            List<CtElement> elements = method.getCtElement().filterChildren(it -> it instanceof CtIf).list();

            assertFalse(elements.isEmpty(), context,
                result -> "DirectionVector#rotate90() should contain exactly one if-else statement, but found %s"
                    .formatted(elements));
        }
    }
}
