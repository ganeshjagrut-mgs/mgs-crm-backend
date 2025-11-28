package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuotationCriteriaTest {

    @Test
    void newQuotationCriteriaHasAllFiltersNullTest() {
        var quotationCriteria = new QuotationCriteria();
        assertThat(quotationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quotationCriteriaFluentMethodsCreatesFiltersTest() {
        var quotationCriteria = new QuotationCriteria();

        setAllFilters(quotationCriteria);

        assertThat(quotationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quotationCriteriaCopyCreatesNullFilterTest() {
        var quotationCriteria = new QuotationCriteria();
        var copy = quotationCriteria.copy();

        assertThat(quotationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quotationCriteria)
        );
    }

    @Test
    void quotationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quotationCriteria = new QuotationCriteria();
        setAllFilters(quotationCriteria);

        var copy = quotationCriteria.copy();

        assertThat(quotationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quotationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quotationCriteria = new QuotationCriteria();

        assertThat(quotationCriteria).hasToString("QuotationCriteria{}");
    }

    private static void setAllFilters(QuotationCriteria quotationCriteria) {
        quotationCriteria.id();
        quotationCriteria.quoteNumber();
        quotationCriteria.estimateDate();
        quotationCriteria.validUntil();
        quotationCriteria.status();
        quotationCriteria.revisionNumber();
        quotationCriteria.currency();
        quotationCriteria.subtotal();
        quotationCriteria.itemDiscountTotal();
        quotationCriteria.globalDiscountType();
        quotationCriteria.globalDiscountValue();
        quotationCriteria.globalDiscountAmount();
        quotationCriteria.taxableAmount();
        quotationCriteria.totalTax();
        quotationCriteria.shippingAmount();
        quotationCriteria.otherChargesAmount();
        quotationCriteria.roundOffAmount();
        quotationCriteria.totalAmount();
        quotationCriteria.title();
        quotationCriteria.headerNotes();
        quotationCriteria.footerNotes();
        quotationCriteria.termsAndConditions();
        quotationCriteria.referenceNumber();
        quotationCriteria.lastSentAt();
        quotationCriteria.pdfTemplateCode();
        quotationCriteria.tenantId();
        quotationCriteria.customerId();
        quotationCriteria.contactId();
        quotationCriteria.leadId();
        quotationCriteria.createdByUserId();
        quotationCriteria.distinct();
    }

    private static Condition<QuotationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuoteNumber()) &&
                condition.apply(criteria.getEstimateDate()) &&
                condition.apply(criteria.getValidUntil()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getRevisionNumber()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getItemDiscountTotal()) &&
                condition.apply(criteria.getGlobalDiscountType()) &&
                condition.apply(criteria.getGlobalDiscountValue()) &&
                condition.apply(criteria.getGlobalDiscountAmount()) &&
                condition.apply(criteria.getTaxableAmount()) &&
                condition.apply(criteria.getTotalTax()) &&
                condition.apply(criteria.getShippingAmount()) &&
                condition.apply(criteria.getOtherChargesAmount()) &&
                condition.apply(criteria.getRoundOffAmount()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getHeaderNotes()) &&
                condition.apply(criteria.getFooterNotes()) &&
                condition.apply(criteria.getTermsAndConditions()) &&
                condition.apply(criteria.getReferenceNumber()) &&
                condition.apply(criteria.getLastSentAt()) &&
                condition.apply(criteria.getPdfTemplateCode()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getContactId()) &&
                condition.apply(criteria.getLeadId()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuotationCriteria> copyFiltersAre(QuotationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuoteNumber(), copy.getQuoteNumber()) &&
                condition.apply(criteria.getEstimateDate(), copy.getEstimateDate()) &&
                condition.apply(criteria.getValidUntil(), copy.getValidUntil()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getRevisionNumber(), copy.getRevisionNumber()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getItemDiscountTotal(), copy.getItemDiscountTotal()) &&
                condition.apply(criteria.getGlobalDiscountType(), copy.getGlobalDiscountType()) &&
                condition.apply(criteria.getGlobalDiscountValue(), copy.getGlobalDiscountValue()) &&
                condition.apply(criteria.getGlobalDiscountAmount(), copy.getGlobalDiscountAmount()) &&
                condition.apply(criteria.getTaxableAmount(), copy.getTaxableAmount()) &&
                condition.apply(criteria.getTotalTax(), copy.getTotalTax()) &&
                condition.apply(criteria.getShippingAmount(), copy.getShippingAmount()) &&
                condition.apply(criteria.getOtherChargesAmount(), copy.getOtherChargesAmount()) &&
                condition.apply(criteria.getRoundOffAmount(), copy.getRoundOffAmount()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getHeaderNotes(), copy.getHeaderNotes()) &&
                condition.apply(criteria.getFooterNotes(), copy.getFooterNotes()) &&
                condition.apply(criteria.getTermsAndConditions(), copy.getTermsAndConditions()) &&
                condition.apply(criteria.getReferenceNumber(), copy.getReferenceNumber()) &&
                condition.apply(criteria.getLastSentAt(), copy.getLastSentAt()) &&
                condition.apply(criteria.getPdfTemplateCode(), copy.getPdfTemplateCode()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getContactId(), copy.getContactId()) &&
                condition.apply(criteria.getLeadId(), copy.getLeadId()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
