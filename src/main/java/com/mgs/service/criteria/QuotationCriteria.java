package com.mgs.service.criteria;

import com.mgs.domain.enumeration.DiscountType;
import com.mgs.domain.enumeration.QuotationStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Quotation} entity. This class is used
 * in {@link com.mgs.web.rest.QuotationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuotationStatus
     */
    public static class QuotationStatusFilter extends Filter<QuotationStatus> {

        public QuotationStatusFilter() {}

        public QuotationStatusFilter(QuotationStatusFilter filter) {
            super(filter);
        }

        @Override
        public QuotationStatusFilter copy() {
            return new QuotationStatusFilter(this);
        }
    }

    /**
     * Class for filtering DiscountType
     */
    public static class DiscountTypeFilter extends Filter<DiscountType> {

        public DiscountTypeFilter() {}

        public DiscountTypeFilter(DiscountTypeFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeFilter copy() {
            return new DiscountTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter quoteNumber;

    private LocalDateFilter estimateDate;

    private LocalDateFilter validUntil;

    private QuotationStatusFilter status;

    private IntegerFilter revisionNumber;

    private StringFilter currency;

    private BigDecimalFilter subtotal;

    private BigDecimalFilter itemDiscountTotal;

    private DiscountTypeFilter globalDiscountType;

    private BigDecimalFilter globalDiscountValue;

    private BigDecimalFilter globalDiscountAmount;

    private BigDecimalFilter taxableAmount;

    private BigDecimalFilter totalTax;

    private BigDecimalFilter shippingAmount;

    private BigDecimalFilter otherChargesAmount;

    private BigDecimalFilter roundOffAmount;

    private BigDecimalFilter totalAmount;

    private StringFilter title;

    private StringFilter headerNotes;

    private StringFilter footerNotes;

    private StringFilter termsAndConditions;

    private StringFilter referenceNumber;

    private InstantFilter lastSentAt;

    private StringFilter pdfTemplateCode;

    private LongFilter tenantId;

    private LongFilter customerId;

    private LongFilter contactId;

    private LongFilter leadId;

    private LongFilter createdByUserId;

    private Boolean distinct;

    public QuotationCriteria() {}

    public QuotationCriteria(QuotationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quoteNumber = other.optionalQuoteNumber().map(StringFilter::copy).orElse(null);
        this.estimateDate = other.optionalEstimateDate().map(LocalDateFilter::copy).orElse(null);
        this.validUntil = other.optionalValidUntil().map(LocalDateFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(QuotationStatusFilter::copy).orElse(null);
        this.revisionNumber = other.optionalRevisionNumber().map(IntegerFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(BigDecimalFilter::copy).orElse(null);
        this.itemDiscountTotal = other.optionalItemDiscountTotal().map(BigDecimalFilter::copy).orElse(null);
        this.globalDiscountType = other.optionalGlobalDiscountType().map(DiscountTypeFilter::copy).orElse(null);
        this.globalDiscountValue = other.optionalGlobalDiscountValue().map(BigDecimalFilter::copy).orElse(null);
        this.globalDiscountAmount = other.optionalGlobalDiscountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.taxableAmount = other.optionalTaxableAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalTax = other.optionalTotalTax().map(BigDecimalFilter::copy).orElse(null);
        this.shippingAmount = other.optionalShippingAmount().map(BigDecimalFilter::copy).orElse(null);
        this.otherChargesAmount = other.optionalOtherChargesAmount().map(BigDecimalFilter::copy).orElse(null);
        this.roundOffAmount = other.optionalRoundOffAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.headerNotes = other.optionalHeaderNotes().map(StringFilter::copy).orElse(null);
        this.footerNotes = other.optionalFooterNotes().map(StringFilter::copy).orElse(null);
        this.termsAndConditions = other.optionalTermsAndConditions().map(StringFilter::copy).orElse(null);
        this.referenceNumber = other.optionalReferenceNumber().map(StringFilter::copy).orElse(null);
        this.lastSentAt = other.optionalLastSentAt().map(InstantFilter::copy).orElse(null);
        this.pdfTemplateCode = other.optionalPdfTemplateCode().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(LongFilter::copy).orElse(null);
        this.leadId = other.optionalLeadId().map(LongFilter::copy).orElse(null);
        this.createdByUserId = other.optionalCreatedByUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuotationCriteria copy() {
        return new QuotationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getQuoteNumber() {
        return quoteNumber;
    }

    public Optional<StringFilter> optionalQuoteNumber() {
        return Optional.ofNullable(quoteNumber);
    }

    public StringFilter quoteNumber() {
        if (quoteNumber == null) {
            setQuoteNumber(new StringFilter());
        }
        return quoteNumber;
    }

    public void setQuoteNumber(StringFilter quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public LocalDateFilter getEstimateDate() {
        return estimateDate;
    }

    public Optional<LocalDateFilter> optionalEstimateDate() {
        return Optional.ofNullable(estimateDate);
    }

    public LocalDateFilter estimateDate() {
        if (estimateDate == null) {
            setEstimateDate(new LocalDateFilter());
        }
        return estimateDate;
    }

    public void setEstimateDate(LocalDateFilter estimateDate) {
        this.estimateDate = estimateDate;
    }

    public LocalDateFilter getValidUntil() {
        return validUntil;
    }

    public Optional<LocalDateFilter> optionalValidUntil() {
        return Optional.ofNullable(validUntil);
    }

    public LocalDateFilter validUntil() {
        if (validUntil == null) {
            setValidUntil(new LocalDateFilter());
        }
        return validUntil;
    }

    public void setValidUntil(LocalDateFilter validUntil) {
        this.validUntil = validUntil;
    }

    public QuotationStatusFilter getStatus() {
        return status;
    }

    public Optional<QuotationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public QuotationStatusFilter status() {
        if (status == null) {
            setStatus(new QuotationStatusFilter());
        }
        return status;
    }

    public void setStatus(QuotationStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getRevisionNumber() {
        return revisionNumber;
    }

    public Optional<IntegerFilter> optionalRevisionNumber() {
        return Optional.ofNullable(revisionNumber);
    }

    public IntegerFilter revisionNumber() {
        if (revisionNumber == null) {
            setRevisionNumber(new IntegerFilter());
        }
        return revisionNumber;
    }

    public void setRevisionNumber(IntegerFilter revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public BigDecimalFilter getSubtotal() {
        return subtotal;
    }

    public Optional<BigDecimalFilter> optionalSubtotal() {
        return Optional.ofNullable(subtotal);
    }

    public BigDecimalFilter subtotal() {
        if (subtotal == null) {
            setSubtotal(new BigDecimalFilter());
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimalFilter subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimalFilter getItemDiscountTotal() {
        return itemDiscountTotal;
    }

    public Optional<BigDecimalFilter> optionalItemDiscountTotal() {
        return Optional.ofNullable(itemDiscountTotal);
    }

    public BigDecimalFilter itemDiscountTotal() {
        if (itemDiscountTotal == null) {
            setItemDiscountTotal(new BigDecimalFilter());
        }
        return itemDiscountTotal;
    }

    public void setItemDiscountTotal(BigDecimalFilter itemDiscountTotal) {
        this.itemDiscountTotal = itemDiscountTotal;
    }

    public DiscountTypeFilter getGlobalDiscountType() {
        return globalDiscountType;
    }

    public Optional<DiscountTypeFilter> optionalGlobalDiscountType() {
        return Optional.ofNullable(globalDiscountType);
    }

    public DiscountTypeFilter globalDiscountType() {
        if (globalDiscountType == null) {
            setGlobalDiscountType(new DiscountTypeFilter());
        }
        return globalDiscountType;
    }

    public void setGlobalDiscountType(DiscountTypeFilter globalDiscountType) {
        this.globalDiscountType = globalDiscountType;
    }

    public BigDecimalFilter getGlobalDiscountValue() {
        return globalDiscountValue;
    }

    public Optional<BigDecimalFilter> optionalGlobalDiscountValue() {
        return Optional.ofNullable(globalDiscountValue);
    }

    public BigDecimalFilter globalDiscountValue() {
        if (globalDiscountValue == null) {
            setGlobalDiscountValue(new BigDecimalFilter());
        }
        return globalDiscountValue;
    }

    public void setGlobalDiscountValue(BigDecimalFilter globalDiscountValue) {
        this.globalDiscountValue = globalDiscountValue;
    }

    public BigDecimalFilter getGlobalDiscountAmount() {
        return globalDiscountAmount;
    }

    public Optional<BigDecimalFilter> optionalGlobalDiscountAmount() {
        return Optional.ofNullable(globalDiscountAmount);
    }

    public BigDecimalFilter globalDiscountAmount() {
        if (globalDiscountAmount == null) {
            setGlobalDiscountAmount(new BigDecimalFilter());
        }
        return globalDiscountAmount;
    }

    public void setGlobalDiscountAmount(BigDecimalFilter globalDiscountAmount) {
        this.globalDiscountAmount = globalDiscountAmount;
    }

    public BigDecimalFilter getTaxableAmount() {
        return taxableAmount;
    }

    public Optional<BigDecimalFilter> optionalTaxableAmount() {
        return Optional.ofNullable(taxableAmount);
    }

    public BigDecimalFilter taxableAmount() {
        if (taxableAmount == null) {
            setTaxableAmount(new BigDecimalFilter());
        }
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimalFilter taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimalFilter getTotalTax() {
        return totalTax;
    }

    public Optional<BigDecimalFilter> optionalTotalTax() {
        return Optional.ofNullable(totalTax);
    }

    public BigDecimalFilter totalTax() {
        if (totalTax == null) {
            setTotalTax(new BigDecimalFilter());
        }
        return totalTax;
    }

    public void setTotalTax(BigDecimalFilter totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimalFilter getShippingAmount() {
        return shippingAmount;
    }

    public Optional<BigDecimalFilter> optionalShippingAmount() {
        return Optional.ofNullable(shippingAmount);
    }

    public BigDecimalFilter shippingAmount() {
        if (shippingAmount == null) {
            setShippingAmount(new BigDecimalFilter());
        }
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimalFilter shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimalFilter getOtherChargesAmount() {
        return otherChargesAmount;
    }

    public Optional<BigDecimalFilter> optionalOtherChargesAmount() {
        return Optional.ofNullable(otherChargesAmount);
    }

    public BigDecimalFilter otherChargesAmount() {
        if (otherChargesAmount == null) {
            setOtherChargesAmount(new BigDecimalFilter());
        }
        return otherChargesAmount;
    }

    public void setOtherChargesAmount(BigDecimalFilter otherChargesAmount) {
        this.otherChargesAmount = otherChargesAmount;
    }

    public BigDecimalFilter getRoundOffAmount() {
        return roundOffAmount;
    }

    public Optional<BigDecimalFilter> optionalRoundOffAmount() {
        return Optional.ofNullable(roundOffAmount);
    }

    public BigDecimalFilter roundOffAmount() {
        if (roundOffAmount == null) {
            setRoundOffAmount(new BigDecimalFilter());
        }
        return roundOffAmount;
    }

    public void setRoundOffAmount(BigDecimalFilter roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getHeaderNotes() {
        return headerNotes;
    }

    public Optional<StringFilter> optionalHeaderNotes() {
        return Optional.ofNullable(headerNotes);
    }

    public StringFilter headerNotes() {
        if (headerNotes == null) {
            setHeaderNotes(new StringFilter());
        }
        return headerNotes;
    }

    public void setHeaderNotes(StringFilter headerNotes) {
        this.headerNotes = headerNotes;
    }

    public StringFilter getFooterNotes() {
        return footerNotes;
    }

    public Optional<StringFilter> optionalFooterNotes() {
        return Optional.ofNullable(footerNotes);
    }

    public StringFilter footerNotes() {
        if (footerNotes == null) {
            setFooterNotes(new StringFilter());
        }
        return footerNotes;
    }

    public void setFooterNotes(StringFilter footerNotes) {
        this.footerNotes = footerNotes;
    }

    public StringFilter getTermsAndConditions() {
        return termsAndConditions;
    }

    public Optional<StringFilter> optionalTermsAndConditions() {
        return Optional.ofNullable(termsAndConditions);
    }

    public StringFilter termsAndConditions() {
        if (termsAndConditions == null) {
            setTermsAndConditions(new StringFilter());
        }
        return termsAndConditions;
    }

    public void setTermsAndConditions(StringFilter termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public Optional<StringFilter> optionalReferenceNumber() {
        return Optional.ofNullable(referenceNumber);
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            setReferenceNumber(new StringFilter());
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public InstantFilter getLastSentAt() {
        return lastSentAt;
    }

    public Optional<InstantFilter> optionalLastSentAt() {
        return Optional.ofNullable(lastSentAt);
    }

    public InstantFilter lastSentAt() {
        if (lastSentAt == null) {
            setLastSentAt(new InstantFilter());
        }
        return lastSentAt;
    }

    public void setLastSentAt(InstantFilter lastSentAt) {
        this.lastSentAt = lastSentAt;
    }

    public StringFilter getPdfTemplateCode() {
        return pdfTemplateCode;
    }

    public Optional<StringFilter> optionalPdfTemplateCode() {
        return Optional.ofNullable(pdfTemplateCode);
    }

    public StringFilter pdfTemplateCode() {
        if (pdfTemplateCode == null) {
            setPdfTemplateCode(new StringFilter());
        }
        return pdfTemplateCode;
    }

    public void setPdfTemplateCode(StringFilter pdfTemplateCode) {
        this.pdfTemplateCode = pdfTemplateCode;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public Optional<LongFilter> optionalContactId() {
        return Optional.ofNullable(contactId);
    }

    public LongFilter contactId() {
        if (contactId == null) {
            setContactId(new LongFilter());
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getLeadId() {
        return leadId;
    }

    public Optional<LongFilter> optionalLeadId() {
        return Optional.ofNullable(leadId);
    }

    public LongFilter leadId() {
        if (leadId == null) {
            setLeadId(new LongFilter());
        }
        return leadId;
    }

    public void setLeadId(LongFilter leadId) {
        this.leadId = leadId;
    }

    public LongFilter getCreatedByUserId() {
        return createdByUserId;
    }

    public Optional<LongFilter> optionalCreatedByUserId() {
        return Optional.ofNullable(createdByUserId);
    }

    public LongFilter createdByUserId() {
        if (createdByUserId == null) {
            setCreatedByUserId(new LongFilter());
        }
        return createdByUserId;
    }

    public void setCreatedByUserId(LongFilter createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuotationCriteria that = (QuotationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quoteNumber, that.quoteNumber) &&
            Objects.equals(estimateDate, that.estimateDate) &&
            Objects.equals(validUntil, that.validUntil) &&
            Objects.equals(status, that.status) &&
            Objects.equals(revisionNumber, that.revisionNumber) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(itemDiscountTotal, that.itemDiscountTotal) &&
            Objects.equals(globalDiscountType, that.globalDiscountType) &&
            Objects.equals(globalDiscountValue, that.globalDiscountValue) &&
            Objects.equals(globalDiscountAmount, that.globalDiscountAmount) &&
            Objects.equals(taxableAmount, that.taxableAmount) &&
            Objects.equals(totalTax, that.totalTax) &&
            Objects.equals(shippingAmount, that.shippingAmount) &&
            Objects.equals(otherChargesAmount, that.otherChargesAmount) &&
            Objects.equals(roundOffAmount, that.roundOffAmount) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(title, that.title) &&
            Objects.equals(headerNotes, that.headerNotes) &&
            Objects.equals(footerNotes, that.footerNotes) &&
            Objects.equals(termsAndConditions, that.termsAndConditions) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(lastSentAt, that.lastSentAt) &&
            Objects.equals(pdfTemplateCode, that.pdfTemplateCode) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(leadId, that.leadId) &&
            Objects.equals(createdByUserId, that.createdByUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            quoteNumber,
            estimateDate,
            validUntil,
            status,
            revisionNumber,
            currency,
            subtotal,
            itemDiscountTotal,
            globalDiscountType,
            globalDiscountValue,
            globalDiscountAmount,
            taxableAmount,
            totalTax,
            shippingAmount,
            otherChargesAmount,
            roundOffAmount,
            totalAmount,
            title,
            headerNotes,
            footerNotes,
            termsAndConditions,
            referenceNumber,
            lastSentAt,
            pdfTemplateCode,
            tenantId,
            customerId,
            contactId,
            leadId,
            createdByUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuoteNumber().map(f -> "quoteNumber=" + f + ", ").orElse("") +
            optionalEstimateDate().map(f -> "estimateDate=" + f + ", ").orElse("") +
            optionalValidUntil().map(f -> "validUntil=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalRevisionNumber().map(f -> "revisionNumber=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalItemDiscountTotal().map(f -> "itemDiscountTotal=" + f + ", ").orElse("") +
            optionalGlobalDiscountType().map(f -> "globalDiscountType=" + f + ", ").orElse("") +
            optionalGlobalDiscountValue().map(f -> "globalDiscountValue=" + f + ", ").orElse("") +
            optionalGlobalDiscountAmount().map(f -> "globalDiscountAmount=" + f + ", ").orElse("") +
            optionalTaxableAmount().map(f -> "taxableAmount=" + f + ", ").orElse("") +
            optionalTotalTax().map(f -> "totalTax=" + f + ", ").orElse("") +
            optionalShippingAmount().map(f -> "shippingAmount=" + f + ", ").orElse("") +
            optionalOtherChargesAmount().map(f -> "otherChargesAmount=" + f + ", ").orElse("") +
            optionalRoundOffAmount().map(f -> "roundOffAmount=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalHeaderNotes().map(f -> "headerNotes=" + f + ", ").orElse("") +
            optionalFooterNotes().map(f -> "footerNotes=" + f + ", ").orElse("") +
            optionalTermsAndConditions().map(f -> "termsAndConditions=" + f + ", ").orElse("") +
            optionalReferenceNumber().map(f -> "referenceNumber=" + f + ", ").orElse("") +
            optionalLastSentAt().map(f -> "lastSentAt=" + f + ", ").orElse("") +
            optionalPdfTemplateCode().map(f -> "pdfTemplateCode=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalLeadId().map(f -> "leadId=" + f + ", ").orElse("") +
            optionalCreatedByUserId().map(f -> "createdByUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
