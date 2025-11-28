package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserRoleCriteriaTest {

    @Test
    void newUserRoleCriteriaHasAllFiltersNullTest() {
        var userRoleCriteria = new UserRoleCriteria();
        assertThat(userRoleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var userRoleCriteria = new UserRoleCriteria();

        setAllFilters(userRoleCriteria);

        assertThat(userRoleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userRoleCriteriaCopyCreatesNullFilterTest() {
        var userRoleCriteria = new UserRoleCriteria();
        var copy = userRoleCriteria.copy();

        assertThat(userRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userRoleCriteria)
        );
    }

    @Test
    void userRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userRoleCriteria = new UserRoleCriteria();
        setAllFilters(userRoleCriteria);

        var copy = userRoleCriteria.copy();

        assertThat(userRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userRoleCriteria = new UserRoleCriteria();

        assertThat(userRoleCriteria).hasToString("UserRoleCriteria{}");
    }

    private static void setAllFilters(UserRoleCriteria userRoleCriteria) {
        userRoleCriteria.id();
        userRoleCriteria.tenantId();
        userRoleCriteria.userId();
        userRoleCriteria.roleId();
        userRoleCriteria.distinct();
    }

    private static Condition<UserRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserRoleCriteria> copyFiltersAre(UserRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
