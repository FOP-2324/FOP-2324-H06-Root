package h06;

import h06.world.World;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

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
     * @param world      the world
     * @return the context information for a world
     */
    public static Context buildWorldContext(WorldProperties properties, World world) {
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
        Criterion[] criteria = Stream.concat(
                Arrays.stream(new Class[]{source}),
                Arrays.stream(source.getDeclaredClasses())
            ).flatMap(it -> Arrays.stream(it.getDeclaredMethods()))
            .filter(it -> it.isAnnotationPresent(DisplayName.class))
            .sorted(Comparator.comparing(it -> it.getAnnotation(DisplayName.class).value()))
            .map(it -> {
                // Skip display name prefix: XX | Description
                String description = it.getAnnotation(DisplayName.class).value();
                return Criterion.builder().shortDescription(description.substring(5))
                    .grader(
                        Grader.testAwareBuilder()
                            .requirePass(JUnitTestRef.ofMethod(it))
                            .pointsPassedMax()
                            .pointsFailedMin()
                            .build()
                    )
                    .build();
            }).toArray(Criterion[]::new);
        return Criterion.builder()
            .shortDescription(source.getAnnotation(DisplayName.class).value())
            .addChildCriteria(criteria)
            .build();
    }
}
