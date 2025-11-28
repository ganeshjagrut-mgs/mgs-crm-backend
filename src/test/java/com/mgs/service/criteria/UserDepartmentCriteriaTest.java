package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserDepartmentCriteriaTest {

    @Test
    void newUserDepartmentCriteriaHasAllFiltersNullTest() {
        var userDepartmentCriteria = new UserDepartmentCriteria();
        assertThat(userDepartmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userDepartmentCriteriaFluentMethodsCreatesFiltersTest() {
        var userDepartmentCriteria = new UserDepartmentCriteria();

        setAllFilters(userDepartmentCriteria);

        assertThat(userDepartmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userDepartmentCriteriaCopyCreatesNullFilterTest() {
        var userDepartmentCriteria = new UserDepartmentCriteria();
        var copy = userDepartmentCriteria.copy();

        assertThat(userDepartmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userDepartmentCriteria)
        );
    }

    @Test
    void userDepartmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userDepartmentCriteria = new UserDepartmentCriteria();
        setAllFilters(userDepartmentCriteria);

        var copy = userDepartmentCriteria.copy();

        assertThat(userDepartmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userDepartmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userDepartmentCriteria = new UserDepartmentCriteria();

        assertThat(userDepartmentCriteria).hasToString("UserDepartmentCriteria{}");
    }

    private static void setAllFilters(UserDepartmentCriteria userDepartmentCriteria) {
        userDepartmentCriteria.id();
        userDepartmentCriteria.tenantId();
        userDepartmentCriteria.userId();
        userDepartmentCriteria.departmentId();
        userDepartmentCriteria.distinct();
    }

    private static Condition<UserDepartmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserDepartmentCriteria> copyFiltersAre(
        UserDepartmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
