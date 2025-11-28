package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserHierarchyCriteriaTest {

    @Test
    void newUserHierarchyCriteriaHasAllFiltersNullTest() {
        var userHierarchyCriteria = new UserHierarchyCriteria();
        assertThat(userHierarchyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userHierarchyCriteriaFluentMethodsCreatesFiltersTest() {
        var userHierarchyCriteria = new UserHierarchyCriteria();

        setAllFilters(userHierarchyCriteria);

        assertThat(userHierarchyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userHierarchyCriteriaCopyCreatesNullFilterTest() {
        var userHierarchyCriteria = new UserHierarchyCriteria();
        var copy = userHierarchyCriteria.copy();

        assertThat(userHierarchyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userHierarchyCriteria)
        );
    }

    @Test
    void userHierarchyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userHierarchyCriteria = new UserHierarchyCriteria();
        setAllFilters(userHierarchyCriteria);

        var copy = userHierarchyCriteria.copy();

        assertThat(userHierarchyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userHierarchyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userHierarchyCriteria = new UserHierarchyCriteria();

        assertThat(userHierarchyCriteria).hasToString("UserHierarchyCriteria{}");
    }

    private static void setAllFilters(UserHierarchyCriteria userHierarchyCriteria) {
        userHierarchyCriteria.id();
        userHierarchyCriteria.relationshipType();
        userHierarchyCriteria.tenantId();
        userHierarchyCriteria.parentUserId();
        userHierarchyCriteria.childUserId();
        userHierarchyCriteria.distinct();
    }

    private static Condition<UserHierarchyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRelationshipType()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getParentUserId()) &&
                condition.apply(criteria.getChildUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserHierarchyCriteria> copyFiltersAre(
        UserHierarchyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRelationshipType(), copy.getRelationshipType()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getParentUserId(), copy.getParentUserId()) &&
                condition.apply(criteria.getChildUserId(), copy.getChildUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
