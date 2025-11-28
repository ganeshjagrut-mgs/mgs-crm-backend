package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DealCriteriaTest {

    @Test
    void newDealCriteriaHasAllFiltersNullTest() {
        var dealCriteria = new DealCriteria();
        assertThat(dealCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dealCriteriaFluentMethodsCreatesFiltersTest() {
        var dealCriteria = new DealCriteria();

        setAllFilters(dealCriteria);

        assertThat(dealCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dealCriteriaCopyCreatesNullFilterTest() {
        var dealCriteria = new DealCriteria();
        var copy = dealCriteria.copy();

        assertThat(dealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dealCriteria)
        );
    }

    @Test
    void dealCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dealCriteria = new DealCriteria();
        setAllFilters(dealCriteria);

        var copy = dealCriteria.copy();

        assertThat(dealCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dealCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dealCriteria = new DealCriteria();

        assertThat(dealCriteria).hasToString("DealCriteria{}");
    }

    private static void setAllFilters(DealCriteria dealCriteria) {
        dealCriteria.id();
        dealCriteria.dealNumber();
        dealCriteria.dealValue();
        dealCriteria.status();
        dealCriteria.currency();
        dealCriteria.startDate();
        dealCriteria.closeDate();
        dealCriteria.notes();
        dealCriteria.tenantId();
        dealCriteria.customerId();
        dealCriteria.contactId();
        dealCriteria.leadId();
        dealCriteria.distinct();
    }

    private static Condition<DealCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDealNumber()) &&
                condition.apply(criteria.getDealValue()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getCloseDate()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getContactId()) &&
                condition.apply(criteria.getLeadId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DealCriteria> copyFiltersAre(DealCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDealNumber(), copy.getDealNumber()) &&
                condition.apply(criteria.getDealValue(), copy.getDealValue()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getCloseDate(), copy.getCloseDate()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getContactId(), copy.getContactId()) &&
                condition.apply(criteria.getLeadId(), copy.getLeadId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
