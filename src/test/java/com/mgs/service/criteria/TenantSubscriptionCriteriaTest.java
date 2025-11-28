package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TenantSubscriptionCriteriaTest {

    @Test
    void newTenantSubscriptionCriteriaHasAllFiltersNullTest() {
        var tenantSubscriptionCriteria = new TenantSubscriptionCriteria();
        assertThat(tenantSubscriptionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tenantSubscriptionCriteriaFluentMethodsCreatesFiltersTest() {
        var tenantSubscriptionCriteria = new TenantSubscriptionCriteria();

        setAllFilters(tenantSubscriptionCriteria);

        assertThat(tenantSubscriptionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tenantSubscriptionCriteriaCopyCreatesNullFilterTest() {
        var tenantSubscriptionCriteria = new TenantSubscriptionCriteria();
        var copy = tenantSubscriptionCriteria.copy();

        assertThat(tenantSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantSubscriptionCriteria)
        );
    }

    @Test
    void tenantSubscriptionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tenantSubscriptionCriteria = new TenantSubscriptionCriteria();
        setAllFilters(tenantSubscriptionCriteria);

        var copy = tenantSubscriptionCriteria.copy();

        assertThat(tenantSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantSubscriptionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tenantSubscriptionCriteria = new TenantSubscriptionCriteria();

        assertThat(tenantSubscriptionCriteria).hasToString("TenantSubscriptionCriteria{}");
    }

    private static void setAllFilters(TenantSubscriptionCriteria tenantSubscriptionCriteria) {
        tenantSubscriptionCriteria.id();
        tenantSubscriptionCriteria.status();
        tenantSubscriptionCriteria.startDate();
        tenantSubscriptionCriteria.endDate();
        tenantSubscriptionCriteria.trialEndDate();
        tenantSubscriptionCriteria.lastRenewedAt();
        tenantSubscriptionCriteria.nextBillingAt();
        tenantSubscriptionCriteria.tenantId();
        tenantSubscriptionCriteria.planId();
        tenantSubscriptionCriteria.distinct();
    }

    private static Condition<TenantSubscriptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getTrialEndDate()) &&
                condition.apply(criteria.getLastRenewedAt()) &&
                condition.apply(criteria.getNextBillingAt()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getPlanId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TenantSubscriptionCriteria> copyFiltersAre(
        TenantSubscriptionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getTrialEndDate(), copy.getTrialEndDate()) &&
                condition.apply(criteria.getLastRenewedAt(), copy.getLastRenewedAt()) &&
                condition.apply(criteria.getNextBillingAt(), copy.getNextBillingAt()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getPlanId(), copy.getPlanId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
