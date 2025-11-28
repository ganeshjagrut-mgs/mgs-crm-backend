package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TaskCommentCriteriaTest {

    @Test
    void newTaskCommentCriteriaHasAllFiltersNullTest() {
        var taskCommentCriteria = new TaskCommentCriteria();
        assertThat(taskCommentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void taskCommentCriteriaFluentMethodsCreatesFiltersTest() {
        var taskCommentCriteria = new TaskCommentCriteria();

        setAllFilters(taskCommentCriteria);

        assertThat(taskCommentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void taskCommentCriteriaCopyCreatesNullFilterTest() {
        var taskCommentCriteria = new TaskCommentCriteria();
        var copy = taskCommentCriteria.copy();

        assertThat(taskCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(taskCommentCriteria)
        );
    }

    @Test
    void taskCommentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var taskCommentCriteria = new TaskCommentCriteria();
        setAllFilters(taskCommentCriteria);

        var copy = taskCommentCriteria.copy();

        assertThat(taskCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(taskCommentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var taskCommentCriteria = new TaskCommentCriteria();

        assertThat(taskCommentCriteria).hasToString("TaskCommentCriteria{}");
    }

    private static void setAllFilters(TaskCommentCriteria taskCommentCriteria) {
        taskCommentCriteria.id();
        taskCommentCriteria.comment();
        taskCommentCriteria.taskId();
        taskCommentCriteria.createdByUserId();
        taskCommentCriteria.distinct();
    }

    private static Condition<TaskCommentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getComment()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TaskCommentCriteria> copyFiltersAre(TaskCommentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getComment(), copy.getComment()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
