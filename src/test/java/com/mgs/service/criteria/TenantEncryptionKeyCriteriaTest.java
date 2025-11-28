package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TenantEncryptionKeyCriteriaTest {

    @Test
    void newTenantEncryptionKeyCriteriaHasAllFiltersNullTest() {
        var tenantEncryptionKeyCriteria = new TenantEncryptionKeyCriteria();
        assertThat(tenantEncryptionKeyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tenantEncryptionKeyCriteriaFluentMethodsCreatesFiltersTest() {
        var tenantEncryptionKeyCriteria = new TenantEncryptionKeyCriteria();

        setAllFilters(tenantEncryptionKeyCriteria);

        assertThat(tenantEncryptionKeyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tenantEncryptionKeyCriteriaCopyCreatesNullFilterTest() {
        var tenantEncryptionKeyCriteria = new TenantEncryptionKeyCriteria();
        var copy = tenantEncryptionKeyCriteria.copy();

        assertThat(tenantEncryptionKeyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantEncryptionKeyCriteria)
        );
    }

    @Test
    void tenantEncryptionKeyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tenantEncryptionKeyCriteria = new TenantEncryptionKeyCriteria();
        setAllFilters(tenantEncryptionKeyCriteria);

        var copy = tenantEncryptionKeyCriteria.copy();

        assertThat(tenantEncryptionKeyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantEncryptionKeyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tenantEncryptionKeyCriteria = new TenantEncryptionKeyCriteria();

        assertThat(tenantEncryptionKeyCriteria).hasToString("TenantEncryptionKeyCriteria{}");
    }

    private static void setAllFilters(TenantEncryptionKeyCriteria tenantEncryptionKeyCriteria) {
        tenantEncryptionKeyCriteria.id();
        tenantEncryptionKeyCriteria.keyVersion();
        tenantEncryptionKeyCriteria.encryptedDataKey();
        tenantEncryptionKeyCriteria.pinHash();
        tenantEncryptionKeyCriteria.pinSalt();
        tenantEncryptionKeyCriteria.isActive();
        tenantEncryptionKeyCriteria.tenantId();
        tenantEncryptionKeyCriteria.distinct();
    }

    private static Condition<TenantEncryptionKeyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKeyVersion()) &&
                condition.apply(criteria.getEncryptedDataKey()) &&
                condition.apply(criteria.getPinHash()) &&
                condition.apply(criteria.getPinSalt()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TenantEncryptionKeyCriteria> copyFiltersAre(
        TenantEncryptionKeyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKeyVersion(), copy.getKeyVersion()) &&
                condition.apply(criteria.getEncryptedDataKey(), copy.getEncryptedDataKey()) &&
                condition.apply(criteria.getPinHash(), copy.getPinHash()) &&
                condition.apply(criteria.getPinSalt(), copy.getPinSalt()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
