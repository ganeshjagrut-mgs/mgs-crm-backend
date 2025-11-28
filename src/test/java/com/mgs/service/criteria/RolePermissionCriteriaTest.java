package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RolePermissionCriteriaTest {

    @Test
    void newRolePermissionCriteriaHasAllFiltersNullTest() {
        var rolePermissionCriteria = new RolePermissionCriteria();
        assertThat(rolePermissionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void rolePermissionCriteriaFluentMethodsCreatesFiltersTest() {
        var rolePermissionCriteria = new RolePermissionCriteria();

        setAllFilters(rolePermissionCriteria);

        assertThat(rolePermissionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void rolePermissionCriteriaCopyCreatesNullFilterTest() {
        var rolePermissionCriteria = new RolePermissionCriteria();
        var copy = rolePermissionCriteria.copy();

        assertThat(rolePermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(rolePermissionCriteria)
        );
    }

    @Test
    void rolePermissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rolePermissionCriteria = new RolePermissionCriteria();
        setAllFilters(rolePermissionCriteria);

        var copy = rolePermissionCriteria.copy();

        assertThat(rolePermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(rolePermissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rolePermissionCriteria = new RolePermissionCriteria();

        assertThat(rolePermissionCriteria).hasToString("RolePermissionCriteria{}");
    }

    private static void setAllFilters(RolePermissionCriteria rolePermissionCriteria) {
        rolePermissionCriteria.id();
        rolePermissionCriteria.canRead();
        rolePermissionCriteria.canCreate();
        rolePermissionCriteria.canUpdate();
        rolePermissionCriteria.canDelete();
        rolePermissionCriteria.roleId();
        rolePermissionCriteria.moduleId();
        rolePermissionCriteria.distinct();
    }

    private static Condition<RolePermissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCanRead()) &&
                condition.apply(criteria.getCanCreate()) &&
                condition.apply(criteria.getCanUpdate()) &&
                condition.apply(criteria.getCanDelete()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getModuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RolePermissionCriteria> copyFiltersAre(
        RolePermissionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCanRead(), copy.getCanRead()) &&
                condition.apply(criteria.getCanCreate(), copy.getCanCreate()) &&
                condition.apply(criteria.getCanUpdate(), copy.getCanUpdate()) &&
                condition.apply(criteria.getCanDelete(), copy.getCanDelete()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getModuleId(), copy.getModuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
