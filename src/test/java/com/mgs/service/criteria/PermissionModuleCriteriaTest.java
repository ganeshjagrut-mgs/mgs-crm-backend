package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PermissionModuleCriteriaTest {

    @Test
    void newPermissionModuleCriteriaHasAllFiltersNullTest() {
        var permissionModuleCriteria = new PermissionModuleCriteria();
        assertThat(permissionModuleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void permissionModuleCriteriaFluentMethodsCreatesFiltersTest() {
        var permissionModuleCriteria = new PermissionModuleCriteria();

        setAllFilters(permissionModuleCriteria);

        assertThat(permissionModuleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void permissionModuleCriteriaCopyCreatesNullFilterTest() {
        var permissionModuleCriteria = new PermissionModuleCriteria();
        var copy = permissionModuleCriteria.copy();

        assertThat(permissionModuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(permissionModuleCriteria)
        );
    }

    @Test
    void permissionModuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var permissionModuleCriteria = new PermissionModuleCriteria();
        setAllFilters(permissionModuleCriteria);

        var copy = permissionModuleCriteria.copy();

        assertThat(permissionModuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(permissionModuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var permissionModuleCriteria = new PermissionModuleCriteria();

        assertThat(permissionModuleCriteria).hasToString("PermissionModuleCriteria{}");
    }

    private static void setAllFilters(PermissionModuleCriteria permissionModuleCriteria) {
        permissionModuleCriteria.id();
        permissionModuleCriteria.name();
        permissionModuleCriteria.description();
        permissionModuleCriteria.distinct();
    }

    private static Condition<PermissionModuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PermissionModuleCriteria> copyFiltersAre(
        PermissionModuleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
