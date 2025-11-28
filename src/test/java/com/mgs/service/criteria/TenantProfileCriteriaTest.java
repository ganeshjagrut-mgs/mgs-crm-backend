package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TenantProfileCriteriaTest {

    @Test
    void newTenantProfileCriteriaHasAllFiltersNullTest() {
        var tenantProfileCriteria = new TenantProfileCriteria();
        assertThat(tenantProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tenantProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var tenantProfileCriteria = new TenantProfileCriteria();

        setAllFilters(tenantProfileCriteria);

        assertThat(tenantProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tenantProfileCriteriaCopyCreatesNullFilterTest() {
        var tenantProfileCriteria = new TenantProfileCriteria();
        var copy = tenantProfileCriteria.copy();

        assertThat(tenantProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantProfileCriteria)
        );
    }

    @Test
    void tenantProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tenantProfileCriteria = new TenantProfileCriteria();
        setAllFilters(tenantProfileCriteria);

        var copy = tenantProfileCriteria.copy();

        assertThat(tenantProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tenantProfileCriteria = new TenantProfileCriteria();

        assertThat(tenantProfileCriteria).hasToString("TenantProfileCriteria{}");
    }

    private static void setAllFilters(TenantProfileCriteria tenantProfileCriteria) {
        tenantProfileCriteria.id();
        tenantProfileCriteria.subdomain();
        tenantProfileCriteria.customDomain();
        tenantProfileCriteria.domainVerified();
        tenantProfileCriteria.defaultLocale();
        tenantProfileCriteria.timezone();
        tenantProfileCriteria.tenantId();
        tenantProfileCriteria.addressId();
        tenantProfileCriteria.distinct();
    }

    private static Condition<TenantProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSubdomain()) &&
                condition.apply(criteria.getCustomDomain()) &&
                condition.apply(criteria.getDomainVerified()) &&
                condition.apply(criteria.getDefaultLocale()) &&
                condition.apply(criteria.getTimezone()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TenantProfileCriteria> copyFiltersAre(
        TenantProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSubdomain(), copy.getSubdomain()) &&
                condition.apply(criteria.getCustomDomain(), copy.getCustomDomain()) &&
                condition.apply(criteria.getDomainVerified(), copy.getDomainVerified()) &&
                condition.apply(criteria.getDefaultLocale(), copy.getDefaultLocale()) &&
                condition.apply(criteria.getTimezone(), copy.getTimezone()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
