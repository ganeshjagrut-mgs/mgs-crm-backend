package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LeadSourceCriteriaTest {

    @Test
    void newLeadSourceCriteriaHasAllFiltersNullTest() {
        var leadSourceCriteria = new LeadSourceCriteria();
        assertThat(leadSourceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void leadSourceCriteriaFluentMethodsCreatesFiltersTest() {
        var leadSourceCriteria = new LeadSourceCriteria();

        setAllFilters(leadSourceCriteria);

        assertThat(leadSourceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void leadSourceCriteriaCopyCreatesNullFilterTest() {
        var leadSourceCriteria = new LeadSourceCriteria();
        var copy = leadSourceCriteria.copy();

        assertThat(leadSourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(leadSourceCriteria)
        );
    }

    @Test
    void leadSourceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var leadSourceCriteria = new LeadSourceCriteria();
        setAllFilters(leadSourceCriteria);

        var copy = leadSourceCriteria.copy();

        assertThat(leadSourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(leadSourceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var leadSourceCriteria = new LeadSourceCriteria();

        assertThat(leadSourceCriteria).hasToString("LeadSourceCriteria{}");
    }

    private static void setAllFilters(LeadSourceCriteria leadSourceCriteria) {
        leadSourceCriteria.id();
        leadSourceCriteria.name();
        leadSourceCriteria.nameSearch();
        leadSourceCriteria.isActive();
        leadSourceCriteria.tenantId();
        leadSourceCriteria.distinct();
    }

    private static Condition<LeadSourceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameSearch()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LeadSourceCriteria> copyFiltersAre(LeadSourceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameSearch(), copy.getNameSearch()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
