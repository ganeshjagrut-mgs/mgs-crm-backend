package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SystemUserCriteriaTest {

    @Test
    void newSystemUserCriteriaHasAllFiltersNullTest() {
        var systemUserCriteria = new SystemUserCriteria();
        assertThat(systemUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void systemUserCriteriaFluentMethodsCreatesFiltersTest() {
        var systemUserCriteria = new SystemUserCriteria();

        setAllFilters(systemUserCriteria);

        assertThat(systemUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void systemUserCriteriaCopyCreatesNullFilterTest() {
        var systemUserCriteria = new SystemUserCriteria();
        var copy = systemUserCriteria.copy();

        assertThat(systemUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(systemUserCriteria)
        );
    }

    @Test
    void systemUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var systemUserCriteria = new SystemUserCriteria();
        setAllFilters(systemUserCriteria);

        var copy = systemUserCriteria.copy();

        assertThat(systemUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(systemUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var systemUserCriteria = new SystemUserCriteria();

        assertThat(systemUserCriteria).hasToString("SystemUserCriteria{}");
    }

    private static void setAllFilters(SystemUserCriteria systemUserCriteria) {
        systemUserCriteria.id();
        systemUserCriteria.email();
        systemUserCriteria.passwordHash();
        systemUserCriteria.isSuperAdmin();
        systemUserCriteria.distinct();
    }

    private static Condition<SystemUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPasswordHash()) &&
                condition.apply(criteria.getIsSuperAdmin()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SystemUserCriteria> copyFiltersAre(SystemUserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPasswordHash(), copy.getPasswordHash()) &&
                condition.apply(criteria.getIsSuperAdmin(), copy.getIsSuperAdmin()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
