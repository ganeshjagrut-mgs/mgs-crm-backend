package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuotationItemCriteriaTest {

    @Test
    void newQuotationItemCriteriaHasAllFiltersNullTest() {
        var quotationItemCriteria = new QuotationItemCriteria();
        assertThat(quotationItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quotationItemCriteriaFluentMethodsCreatesFiltersTest() {
        var quotationItemCriteria = new QuotationItemCriteria();

        setAllFilters(quotationItemCriteria);

        assertThat(quotationItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quotationItemCriteriaCopyCreatesNullFilterTest() {
        var quotationItemCriteria = new QuotationItemCriteria();
        var copy = quotationItemCriteria.copy();

        assertThat(quotationItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quotationItemCriteria)
        );
    }

    @Test
    void quotationItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quotationItemCriteria = new QuotationItemCriteria();
        setAllFilters(quotationItemCriteria);

        var copy = quotationItemCriteria.copy();

        assertThat(quotationItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quotationItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quotationItemCriteria = new QuotationItemCriteria();

        assertThat(quotationItemCriteria).hasToString("QuotationItemCriteria{}");
    }

    private static void setAllFilters(QuotationItemCriteria quotationItemCriteria) {
        quotationItemCriteria.id();
        quotationItemCriteria.productName();
        quotationItemCriteria.productSku();
        quotationItemCriteria.productDescription();
        quotationItemCriteria.unitLabel();
        quotationItemCriteria.quantity();
        quotationItemCriteria.unitPrice();
        quotationItemCriteria.discountType();
        quotationItemCriteria.discountValue();
        quotationItemCriteria.discountAmount();
        quotationItemCriteria.taxableAmount();
        quotationItemCriteria.taxRate();
        quotationItemCriteria.taxAmount();
        quotationItemCriteria.lineTotal();
        quotationItemCriteria.sortOrder();
        quotationItemCriteria.tenantId();
        quotationItemCriteria.quotationId();
        quotationItemCriteria.productId();
        quotationItemCriteria.distinct();
    }

    private static Condition<QuotationItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProductName()) &&
                condition.apply(criteria.getProductSku()) &&
                condition.apply(criteria.getProductDescription()) &&
                condition.apply(criteria.getUnitLabel()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue()) &&
                condition.apply(criteria.getDiscountAmount()) &&
                condition.apply(criteria.getTaxableAmount()) &&
                condition.apply(criteria.getTaxRate()) &&
                condition.apply(criteria.getTaxAmount()) &&
                condition.apply(criteria.getLineTotal()) &&
                condition.apply(criteria.getSortOrder()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getQuotationId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuotationItemCriteria> copyFiltersAre(
        QuotationItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProductName(), copy.getProductName()) &&
                condition.apply(criteria.getProductSku(), copy.getProductSku()) &&
                condition.apply(criteria.getProductDescription(), copy.getProductDescription()) &&
                condition.apply(criteria.getUnitLabel(), copy.getUnitLabel()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue(), copy.getDiscountValue()) &&
                condition.apply(criteria.getDiscountAmount(), copy.getDiscountAmount()) &&
                condition.apply(criteria.getTaxableAmount(), copy.getTaxableAmount()) &&
                condition.apply(criteria.getTaxRate(), copy.getTaxRate()) &&
                condition.apply(criteria.getTaxAmount(), copy.getTaxAmount()) &&
                condition.apply(criteria.getLineTotal(), copy.getLineTotal()) &&
                condition.apply(criteria.getSortOrder(), copy.getSortOrder()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getQuotationId(), copy.getQuotationId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
