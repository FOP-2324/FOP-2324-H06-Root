package h06;

import h06.world.DirectionVector;
import org.junit.jupiter.api.DisplayName;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatcherFactories;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertFalse;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertMethodExists;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions3.assertTypeExists;

/**
 * Defines the utilities for H06 testing.
 *
 * @author Nhan Huynh
 */
public class TutorUtils {

    /**
     * The string matcher factory for retrieving links.
     */
    private static final MatcherFactories.StringMatcherFactory STRING_MATCHER_FACTORY = BasicStringMatchers::identical;

    /**
     * Prevents instantiation.
     */
    private TutorUtils() {

    }

    /**
     * Returns the type link for the given package and class.
     *
     * @param p     the package to get the type link for
     * @param clazz the class in the package to get the type link for
     * @return the type link
     */
    public static TypeLink getTypeLink(Package p, Class<?> clazz) {
        return assertTypeExists(
            BasicPackageLink.of(p.getName()),
            STRING_MATCHER_FACTORY.matcher(clazz.getSimpleName())
        );
    }

    /**
     * Returns the method link for the given method name.
     *
     * @param p          the package to get the method link for
     * @param clazz      the class in the package to get the method link for
     * @param methodName the name of the method to get the method link for
     * @param matchers   the matchers to match the method properties
     * @return the retrieved method link
     */
    @SafeVarargs
    public static MethodLink getMethodLink(Package p, Class<?> clazz, String methodName, Matcher<MethodLink>... matchers) {
        return assertMethodExists(
            getTypeLink(p, clazz),
            Arrays.stream(matchers).reduce(STRING_MATCHER_FACTORY.matcher(methodName), Matcher::and)
        );
    }

    /**
     * Returns the context information for a world.
     *
     * @param properties the properties of the world
     * @return the context information for a world
     */
    public static Context buildWorldContext(WorldProperties properties) {
        return contextBuilder().subject("World")
            .add("Width", properties.width)
            .add("Height", properties.height)
            .add("Vertical walls", Arrays.toString(properties.vertical))
            .add("Horizontal walls", Arrays.toString(properties.horizontal))
            .build();
    }

    /**
     * Returns the criterion for the given test class.
     *
     * @param source the test class containing the criteria
     * @return the criterion for the given test class
     */
    public static Criterion criterion(Class<?> source) {
        Class<?>[] nested = source.getClasses();
        if (nested.length == 0) {
            return singleCriterion(source);
        } else {
            return criterionNested(source);
        }
    }

    /**
     * Returns a criterion builder for the given method with default configuration.
     *
     * @param method the method to build the criterion for
     * @return the criterion builder for the given method
     */
    private static Criterion.Builder criterionBuilder(Method method) {
        return Criterion.builder().shortDescription(shortDescription(method))
            .grader(
                Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(method))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            );
    }

    /**
     * Returns the short description for the given method.
     *
     * @param method the method to get the short description for
     * @return the short description for the given method
     */
    private static String shortDescription(Method method) {
        // Skip display name prefix: XX | Description
        String description = method.getAnnotation(DisplayName.class).value();
        return description.substring(5);
    }

    /**
     * Returns the criterion for a single test class.
     *
     * @param source the test class containing the criteria
     * @return the criterion for a single test class
     */
    private static Criterion singleCriterion(Class<?> source) {
        Criterion[] criteria = Arrays.stream(source.getDeclaredMethods())
            .filter(it -> it.isAnnotationPresent(DisplayName.class))
            .sorted(Comparator.comparing(it -> it.getAnnotation(DisplayName.class).value()))
            .map(it -> criterionBuilder(it).build())
            .toArray(Criterion[]::new);
        return Criterion.builder()
            .shortDescription(source.getAnnotation(DisplayName.class).value())
            .addChildCriteria(criteria)
            .build();
    }

    /**
     * Returns the criterion for a nested test class.
     *
     * @param source the test class containing the criteria
     * @return the criterion for a nested test class
     */
    public static Criterion criterionNested(Class<?> source) {
        Class<?>[] classes = Arrays.stream(source.getDeclaredClasses())
            .filter(clazz -> !clazz.isAnnotationPresent(SkipCheck.class))
            .toArray(Class[]::new);
        Criterion[] mainCriteria = new Criterion[classes.length];
        for (int i = 0; i < classes.length; i++) {
            Class<?> clazz = classes[i];
            List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(DisplayName.class))
                .sorted(Comparator.comparing(it -> it.getAnnotation(DisplayName.class).value()))
                .toList();
            Method requirements = methods.stream()
                .filter(it -> it.getAnnotation(DisplayName.class).value().endsWith("Verbindliche Anforderungen"))
                .findFirst().orElse(null);
            List<Method> collected = requirements == null ? methods : methods.subList(0, methods.size() - 1);
            List<Criterion> criteria = collected.stream()
                .map(it -> criterionBuilder(it).build())
                .collect(Collectors.toList());
            if (requirements != null) {
                criteria.add(criterionBuilder(requirements)
                    .minPoints(-collected.size())
                    .maxPoints(0)
                    .build());
            }
            mainCriteria[i] = Criterion.builder()
                .shortDescription(clazz.getAnnotation(DisplayName.class).value())
                .minPoints(0)
                .addChildCriteria(criteria.toArray(Criterion[]::new))
                .build();
        }
        return Criterion.builder()
            .shortDescription(source.getAnnotation(DisplayName.class).value())
            .addChildCriteria(mainCriteria)
            .build();
    }

    /**
     * Tests whether the given method is recursive.
     *
     * @param method     the method to test
     * @param methodName the name of the method
     * @param context    the context
     */
    public static void assertRecursive(CtMethod<?> method, String methodName, Context context) {
        assertTrue(isRecursive(method), context,
            result -> "The %s should be recursive, but found a loop".formatted(methodName)
        );
    }

    /**
     * Tests whether the given method is iterative.
     *
     * @param method     the method to test
     * @param methodName the name of the method
     * @param context    the context
     */
    public static void assertIterative(CtMethod<?> method, String methodName, Context context) {
        assertFalse(isRecursive(method), context,
            result -> "The %s should be iterative, but found a recursion".formatted(methodName)
        );
    }

    /**
     * Returns {@code true} if the given method is recursive.
     *
     * @param method the method to test
     * @return {@code true} if the given method is recursive
     */
    public static boolean isRecursive(CtMethod<?> method) {
        List<? extends CtExecutableReference<?>> calls = method.filterChildren(it -> it instanceof CtInvocation<?>)
            .list()
            .stream()
            .map(it -> it instanceof CtInvocation<?> ? ((CtInvocation<?>) it).getExecutable() : null)
            .filter(Predicate.not(Objects::isNull))
            .toList();
        if (!method.filterChildren(it -> it instanceof CtLoop).list().isEmpty()) {
            return false;
        }
        return isRecursive(method, calls, new HashSet<>(calls));
    }

    /**
     * Returns {@code true} if the given method is recursive.
     *
     * @param method  the method to test
     * @param calls   the calls of the method to test
     * @param visited the visited methods so far
     * @return {@code true} if the given method is recursive
     */
    private static boolean isRecursive(
        CtMethod<?> method,
        List<? extends CtExecutableReference<?>> calls,
        Set<CtExecutableReference<?>> visited
    ) {
        if (calls.stream().anyMatch(it -> it.getSimpleName().equals(method.getSimpleName()))) {
            return true;
        }
        for (CtExecutableReference<?> call : calls) {
            List<? extends CtExecutableReference<?>> newCalls = call.filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> it instanceof CtInvocation<?> ? ((CtInvocation<?>) it).getExecutable() : null)
                .filter(it -> !visited.contains(it))
                .peek(visited::add)
                .toList();
            if (newCalls.stream().anyMatch(it -> !it.filterChildren(it2 -> it2 instanceof CtLoop).list().isEmpty())) {
                return false;
            }
            return isRecursive(method, newCalls, visited);
        }
        return false;
    }

    /**
     * Returns the direction vector counterclockwise to given direction vector (90 degrees to the left).
     *
     * @param d the direction vector to rotate
     * @return the direction vector counterclockwise to the given direction vector
     */
    public static DirectionVector rotate270(DirectionVector d) {
        return switch (d) {
            case UP -> DirectionVector.LEFT;
            case RIGHT -> DirectionVector.UP;
            case DOWN -> DirectionVector.RIGHT;
            case LEFT -> DirectionVector.DOWN;
        };
    }

    /**
     * Returns the direction vector clockwise to given direction vector (90 degrees to the right).
     *
     * @param d the direction vector to rotate
     * @return the direction vector clockwise to the given direction vector
     */
    public static DirectionVector rotate90(DirectionVector d) {
        return switch (d) {
            case UP -> DirectionVector.RIGHT;
            case RIGHT -> DirectionVector.DOWN;
            case DOWN -> DirectionVector.LEFT;
            case LEFT -> DirectionVector.UP;
        };
    }
}
