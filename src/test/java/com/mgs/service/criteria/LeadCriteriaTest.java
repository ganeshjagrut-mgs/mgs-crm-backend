package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LeadCriteriaTest {

    @Test
    void newLeadCriteriaHasAllFiltersNullTest() {
        var leadCriteria = new LeadCriteria();
        assertThat(leadCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void leadCriteriaFluentMethodsCreatesFiltersTest() {
        var leadCriteria = new LeadCriteria();

        setAllFilters(leadCriteria);

        assertThat(leadCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void leadCriteriaCopyCreatesNullFilterTest() {
        var leadCriteria = new LeadCriteria();
        var copy = leadCriteria.copy();

        assertThat(leadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(leadCriteria)
        );
    }

    @Test
    void leadCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var leadCriteria = new LeadCriteria();
        setAllFilters(leadCriteria);

        var copy = leadCriteria.copy();

        assertThat(leadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(leadCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var leadCriteria = new LeadCriteria();

        assertThat(leadCriteria).hasToString("LeadCriteria{}");
    }

    private static void setAllFilters(LeadCriteria leadCriteria) {
        leadCriteria.id();
        leadCriteria.title();
        leadCriteria.status();
        leadCriteria.estimatedValue();
        leadCriteria.currency();
        leadCriteria.notes();
        leadCriteria.tenantId();
        leadCriteria.customerId();
        leadCriteria.contactId();
        leadCriteria.sourceId();
        leadCriteria.pipelineId();
        leadCriteria.stageId();
        leadCriteria.ownerUserId();
        leadCriteria.distinct();
    }

    private static Condition<LeadCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getEstimatedValue()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getContactId()) &&
                condition.apply(criteria.getSourceId()) &&
                condition.apply(criteria.getPipelineId()) &&
                condition.apply(criteria.getStageId()) &&
                condition.apply(criteria.getOwnerUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LeadCriteria> copyFiltersAre(LeadCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getEstimatedValue(), copy.getEstimatedValue()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getContactId(), copy.getContactId()) &&
                condition.apply(criteria.getSourceId(), copy.getSourceId()) &&
                condition.apply(criteria.getPipelineId(), copy.getPipelineId()) &&
                condition.apply(criteria.getStageId(), copy.getStageId()) &&
                condition.apply(criteria.getOwnerUserId(), copy.getOwnerUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
