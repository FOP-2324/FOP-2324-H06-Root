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
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
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
import java.util.stream.Collectors;

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
     * @param skips      defines the methods to skip
     */
    @SafeVarargs
    public static void assertRecursive(
        MethodLink method,
        String methodName,
        Context.Builder<?> context,
        Matcher<MethodLink>... skips
    ) {
        var calls = getIterativeCalls(method, skips);
        var callsName = calls.stream().map(MethodLink::name).collect(Collectors.toSet());
        assertTrue(calls.isEmpty(), context.add("Method calls (iterative)", callsName).build(),
            result -> "The %s should be recursive, but found a loop(s) in %s".formatted(methodName, callsName)
        );
    }

    /**
     * Tests whether the given method is iterative.
     *
     * @param method     the method to test
     * @param methodName the name of the method
     * @param context    the context
     */
    @SafeVarargs
    public static void assertIterative(
        MethodLink method, String methodName,
        Context.Builder<?> context,
        Matcher<MethodLink>... skips
    ) {
        var calls = getRecursiveCalls(method, skips);
        var callsName = calls.stream().map(MethodLink::name).collect(Collectors.toSet());
        assertTrue(calls.isEmpty(), context.add("Method calls (iterative)", callsName).build(),
            result -> "The %s should be iterative, but found a recursion in %s".formatted(methodName, callsName)
        );
    }

    /**
     * Returns the method calls in the given method.
     *
     * @param method  the method to get the method calls for
     * @param visited the visited methods so far (to prevent infinite recursion)
     * @param skips   defines the methods to skip
     * @return the method calls in the given method
     */
    private static Set<MethodLink> getMethodCalls(
        MethodLink method,
        Set<MethodLink> visited,
        Matcher<MethodLink> skips
    ) {
        CtMethod<?> ctMethod;
        try {
            ctMethod = ((BasicMethodLink) method).getCtElement();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            // java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
            // java.lang.NullPointerException: Cannot invoke "String.toCharArray()" because "this.content" is null
            // Occurs if we read src code from stdlib - skip them
            return Set.of();
        }
        return ctMethod.filterChildren(it -> it instanceof CtInvocation<?>)
            .list()
            .stream()
            .filter(element -> element instanceof CtInvocation<?> invocation)
            .map(element -> (CtInvocation<?>) element)
            .map(CtInvocation::getExecutable)
            .map(CtExecutableReference::getActualMethod)
            .filter(Objects::nonNull)
            .map(BasicMethodLink::of)
            .filter(methodLink -> !visited.contains(methodLink))
            .filter(methodLink -> skips.match(methodLink).negative())
            .collect(Collectors.toSet());
    }

    /**
     * Returns the recursive calls in the given method (including all method-calls in the method).
     *
     * @param method the method to get the recursive calls for
     * @param skips  defines the methods to skip
     * @return the recursive calls in the given method
     */
    @SafeVarargs
    public static Set<MethodLink> getRecursiveCalls(MethodLink method, Matcher<MethodLink>... skips) {
        Set<MethodLink> recursion = new HashSet<>();
        computeRecursiveCalls(method, recursion, new HashSet<>(), Arrays.stream(skips)
            .reduce(Matcher::or)
            .orElse(Matcher.never()));
        return recursion;
    }

    /**
     * Computes the recursive calls in the given method (including all method-calls in the method).
     *
     * @param method  the method to get the recursive calls for
     * @param found   the so far found recursive calls
     * @param visited the visited methods so far (to prevent infinite recursion)
     * @param skips   defines the methods to skip
     */
    private static void computeRecursiveCalls(
        MethodLink method, Set<MethodLink> found,
        Set<MethodLink> visited,
        Matcher<MethodLink> skips
    ) {
        var methodCalls = getMethodCalls(method, visited, skips);
        if (methodCalls.stream().anyMatch(m -> m.equals(method))) {
            found.add(method);
        }
        visited.addAll(methodCalls);
        for (MethodLink methodLink : methodCalls) {
            computeRecursiveCalls(methodLink, found, visited, skips);
        }
    }

    /**
     * Returns the iterative calls in the given method (including all method-calls in the method).
     *
     * @param method the method to get the iterative calls for
     * @param skips  defines the methods to skip
     * @return the iterative calls in the given method
     */
    @SafeVarargs
    public static Set<MethodLink> getIterativeCalls(MethodLink method, Matcher<MethodLink>... skips) {
        Set<MethodLink> recursion = new HashSet<>();
        computeIterativeCalls(method, recursion, new HashSet<>(), Arrays.stream(skips)
            .reduce(Matcher::or)
            .orElse(Matcher.never()));
        return recursion;
    }

    /**
     * Computes the iterative calls in the given method (including all method-calls in the method).
     *
     * @param method  the method to get the iterative calls for
     * @param found   the so far found iterative calls
     * @param visited the visited methods so far (to prevent infinite recursion)
     * @param skips   defines the methods to skip
     */
    private static void computeIterativeCalls(MethodLink method, Set<MethodLink> found, Set<MethodLink> visited, Matcher<MethodLink> skips) {
        CtMethod<?> ctMethod;
        try {
            ctMethod = ((BasicMethodLink) method).getCtElement();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            // java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
            // java.lang.NullPointerException: Cannot invoke "String.toCharArray()" because "this.content" is null
            // Occurs if we read src code from stdlib - skip them
            return;
        }
        if (!ctMethod.filterChildren(it -> it instanceof CtLoop).list().isEmpty()) {
            found.add(method);
        }
        var methodCalls = getMethodCalls(method, visited, skips);
        visited.addAll(methodCalls);
        for (MethodLink methodLink : methodCalls) {
            computeIterativeCalls(methodLink, found, visited, skips);
        }
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
        if (calls.stream().anyMatch(m -> m.getSimpleName().equals(method.getSimpleName()))) {
            return true;
        }
        for (CtExecutableReference<?> call : calls) {
            List<? extends CtExecutableReference<?>> newCalls = call.filterChildren(it -> it instanceof CtInvocation<?>)
                .list()
                .stream()
                .map(it -> it instanceof CtInvocation<?> ? ((CtInvocation<?>) it).getExecutable() : null)
                .filter(m -> !visited.contains(m))
                .peek(visited::add)
                .toList();
            if (newCalls.stream().anyMatch(m -> !m.filterChildren(statement -> statement instanceof CtLoop).list().isEmpty())) {
                return false;
            }
            return isRecursive(method, newCalls, visited);
        }
        return true;
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
