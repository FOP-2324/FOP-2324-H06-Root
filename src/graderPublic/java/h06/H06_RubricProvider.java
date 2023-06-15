package h06;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import static h06.TutorUtils.criterion;

public class H06_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H06")
        .addChildCriteria(
            criterion(H1_DirectionVectorTest.class),
            criterion(H2_WorldTest.class),
            criterion(H3_MazeSolverRecursiveTest.class),
            criterion(H4_MazeSolverIterativeTest.class)
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
