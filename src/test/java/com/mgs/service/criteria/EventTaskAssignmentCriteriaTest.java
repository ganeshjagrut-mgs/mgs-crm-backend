package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventTaskAssignmentCriteriaTest {

    @Test
    void newEventTaskAssignmentCriteriaHasAllFiltersNullTest() {
        var eventTaskAssignmentCriteria = new EventTaskAssignmentCriteria();
        assertThat(eventTaskAssignmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void eventTaskAssignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var eventTaskAssignmentCriteria = new EventTaskAssignmentCriteria();

        setAllFilters(eventTaskAssignmentCriteria);

        assertThat(eventTaskAssignmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void eventTaskAssignmentCriteriaCopyCreatesNullFilterTest() {
        var eventTaskAssignmentCriteria = new EventTaskAssignmentCriteria();
        var copy = eventTaskAssignmentCriteria.copy();

        assertThat(eventTaskAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(eventTaskAssignmentCriteria)
        );
    }

    @Test
    void eventTaskAssignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventTaskAssignmentCriteria = new EventTaskAssignmentCriteria();
        setAllFilters(eventTaskAssignmentCriteria);

        var copy = eventTaskAssignmentCriteria.copy();

        assertThat(eventTaskAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(eventTaskAssignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventTaskAssignmentCriteria = new EventTaskAssignmentCriteria();

        assertThat(eventTaskAssignmentCriteria).hasToString("EventTaskAssignmentCriteria{}");
    }

    private static void setAllFilters(EventTaskAssignmentCriteria eventTaskAssignmentCriteria) {
        eventTaskAssignmentCriteria.id();
        eventTaskAssignmentCriteria.eventId();
        eventTaskAssignmentCriteria.taskId();
        eventTaskAssignmentCriteria.assignedToId();
        eventTaskAssignmentCriteria.distinct();
    }

    private static Condition<EventTaskAssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventId()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getAssignedToId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventTaskAssignmentCriteria> copyFiltersAre(
        EventTaskAssignmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventId(), copy.getEventId()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getAssignedToId(), copy.getAssignedToId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
