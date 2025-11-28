package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContactCriteriaTest {

    @Test
    void newContactCriteriaHasAllFiltersNullTest() {
        var contactCriteria = new ContactCriteria();
        assertThat(contactCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contactCriteriaFluentMethodsCreatesFiltersTest() {
        var contactCriteria = new ContactCriteria();

        setAllFilters(contactCriteria);

        assertThat(contactCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contactCriteriaCopyCreatesNullFilterTest() {
        var contactCriteria = new ContactCriteria();
        var copy = contactCriteria.copy();

        assertThat(contactCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contactCriteria)
        );
    }

    @Test
    void contactCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contactCriteria = new ContactCriteria();
        setAllFilters(contactCriteria);

        var copy = contactCriteria.copy();

        assertThat(contactCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contactCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contactCriteria = new ContactCriteria();

        assertThat(contactCriteria).hasToString("ContactCriteria{}");
    }

    private static void setAllFilters(ContactCriteria contactCriteria) {
        contactCriteria.id();
        contactCriteria.tenantId();
        contactCriteria.customerId();
        contactCriteria.addressId();
        contactCriteria.ownerUserId();
        contactCriteria.distinct();
    }

    private static Condition<ContactCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getOwnerUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContactCriteria> copyFiltersAre(ContactCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getOwnerUserId(), copy.getOwnerUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
