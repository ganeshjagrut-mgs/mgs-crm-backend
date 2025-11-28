package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DepartmentCriteriaTest {

    @Test
    void newDepartmentCriteriaHasAllFiltersNullTest() {
        var departmentCriteria = new DepartmentCriteria();
        assertThat(departmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void departmentCriteriaFluentMethodsCreatesFiltersTest() {
        var departmentCriteria = new DepartmentCriteria();

        setAllFilters(departmentCriteria);

        assertThat(departmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void departmentCriteriaCopyCreatesNullFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void departmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var departmentCriteria = new DepartmentCriteria();
        setAllFilters(departmentCriteria);

        var copy = departmentCriteria.copy();

        assertThat(departmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(departmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var departmentCriteria = new DepartmentCriteria();

        assertThat(departmentCriteria).hasToString("DepartmentCriteria{}");
    }

    private static void setAllFilters(DepartmentCriteria departmentCriteria) {
        departmentCriteria.id();
        departmentCriteria.name();
        departmentCriteria.nameSearch();
        departmentCriteria.type();
        departmentCriteria.isActive();
        departmentCriteria.tenantId();
        departmentCriteria.headUserId();
        departmentCriteria.distinct();
    }

    private static Condition<DepartmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameSearch()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getHeadUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DepartmentCriteria> copyFiltersAre(DepartmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameSearch(), copy.getNameSearch()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getHeadUserId(), copy.getHeadUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
