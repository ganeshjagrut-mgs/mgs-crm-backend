package com.crm.service.criteria;

import com.crm.domain.enumeration.DiscountLevelTypeEnum;
import com.crm.domain.enumeration.DiscountTypeEnum;
import com.crm.domain.enumeration.PDFGenerationStatus;
import com.crm.domain.enumeration.PriceDataSourceEnum;
import com.crm.domain.enumeration.TestReportEmailStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Quotation} entity. This class is used
 * in {@link com.crm.web.rest.QuotationResource} to receive all the possible filtering options from
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
     * Class for filtering DiscountLevelTypeEnum
     */
    public static class DiscountLevelTypeEnumFilter extends Filter<DiscountLevelTypeEnum> {

        public DiscountLevelTypeEnumFilter() {}

        public DiscountLevelTypeEnumFilter(DiscountLevelTypeEnumFilter filter) {
            super(filter);
        }

        @Override
        public DiscountLevelTypeEnumFilter copy() {
            return new DiscountLevelTypeEnumFilter(this);
        }
    }

    /**
     * Class for filtering DiscountTypeEnum
     */
    public static class DiscountTypeEnumFilter extends Filter<DiscountTypeEnum> {

        public DiscountTypeEnumFilter() {}

        public DiscountTypeEnumFilter(DiscountTypeEnumFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeEnumFilter copy() {
            return new DiscountTypeEnumFilter(this);
        }
    }

    /**
     * Class for filtering PDFGenerationStatus
     */
    public static class PDFGenerationStatusFilter extends Filter<PDFGenerationStatus> {

        public PDFGenerationStatusFilter() {}

        public PDFGenerationStatusFilter(PDFGenerationStatusFilter filter) {
            super(filter);
        }

        @Override
        public PDFGenerationStatusFilter copy() {
            return new PDFGenerationStatusFilter(this);
        }
    }

    /**
     * Class for filtering TestReportEmailStatus
     */
    public static class TestReportEmailStatusFilter extends Filter<TestReportEmailStatus> {

        public TestReportEmailStatusFilter() {}

        public TestReportEmailStatusFilter(TestReportEmailStatusFilter filter) {
            super(filter);
        }

        @Override
        public TestReportEmailStatusFilter copy() {
            return new TestReportEmailStatusFilter(this);
        }
    }

    /**
     * Class for filtering PriceDataSourceEnum
     */
    public static class PriceDataSourceEnumFilter extends Filter<PriceDataSourceEnum> {

        public PriceDataSourceEnumFilter() {}

        public PriceDataSourceEnumFilter(PriceDataSourceEnumFilter filter) {
            super(filter);
        }

        @Override
        public PriceDataSourceEnumFilter copy() {
            return new PriceDataSourceEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter quotationNumber;

    private InstantFilter quotationDate;

    private StringFilter referenceNumber;

    private InstantFilter referenceDate;

    private InstantFilter estimateDate;

    private StringFilter subject;

    private InstantFilter validity;

    private DiscountLevelTypeEnumFilter discountLevelType;

    private DiscountTypeEnumFilter discountType;

    private DoubleFilter discountTypeValue;

    private StringFilter currency;

    private DoubleFilter subTotal;

    private DoubleFilter grandTotal;

    private DoubleFilter totalTaxAmount;

    private DoubleFilter adjustmentAmount;

    private StringFilter statusReason;

    private PDFGenerationStatusFilter pdfGenerationStatus;

    private TestReportEmailStatusFilter emailStatus;

    private StringFilter emailFailureReason;

    private UUIDFilter correlationId;

    private InstantFilter approvedAt;

    private PriceDataSourceEnumFilter priceDataSource;

    private LongFilter userId;

    private LongFilter customerId;

    private LongFilter paymentTermId;

    private LongFilter quotationStatusId;

    private Boolean distinct;

    public QuotationCriteria() {}

    public QuotationCriteria(QuotationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quotationNumber = other.quotationNumber == null ? null : other.quotationNumber.copy();
        this.quotationDate = other.quotationDate == null ? null : other.quotationDate.copy();
        this.referenceNumber = other.referenceNumber == null ? null : other.referenceNumber.copy();
        this.referenceDate = other.referenceDate == null ? null : other.referenceDate.copy();
        this.estimateDate = other.estimateDate == null ? null : other.estimateDate.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.validity = other.validity == null ? null : other.validity.copy();
        this.discountLevelType = other.discountLevelType == null ? null : other.discountLevelType.copy();
        this.discountType = other.discountType == null ? null : other.discountType.copy();
        this.discountTypeValue = other.discountTypeValue == null ? null : other.discountTypeValue.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.subTotal = other.subTotal == null ? null : other.subTotal.copy();
        this.grandTotal = other.grandTotal == null ? null : other.grandTotal.copy();
        this.totalTaxAmount = other.totalTaxAmount == null ? null : other.totalTaxAmount.copy();
        this.adjustmentAmount = other.adjustmentAmount == null ? null : other.adjustmentAmount.copy();
        this.statusReason = other.statusReason == null ? null : other.statusReason.copy();
        this.pdfGenerationStatus = other.pdfGenerationStatus == null ? null : other.pdfGenerationStatus.copy();
        this.emailStatus = other.emailStatus == null ? null : other.emailStatus.copy();
        this.emailFailureReason = other.emailFailureReason == null ? null : other.emailFailureReason.copy();
        this.correlationId = other.correlationId == null ? null : other.correlationId.copy();
        this.approvedAt = other.approvedAt == null ? null : other.approvedAt.copy();
        this.priceDataSource = other.priceDataSource == null ? null : other.priceDataSource.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.paymentTermId = other.paymentTermId == null ? null : other.paymentTermId.copy();
        this.quotationStatusId = other.quotationStatusId == null ? null : other.quotationStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuotationCriteria copy() {
        return new QuotationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getQuotationNumber() {
        return quotationNumber;
    }

    public StringFilter quotationNumber() {
        if (quotationNumber == null) {
            quotationNumber = new StringFilter();
        }
        return quotationNumber;
    }

    public void setQuotationNumber(StringFilter quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public InstantFilter getQuotationDate() {
        return quotationDate;
    }

    public InstantFilter quotationDate() {
        if (quotationDate == null) {
            quotationDate = new InstantFilter();
        }
        return quotationDate;
    }

    public void setQuotationDate(InstantFilter quotationDate) {
        this.quotationDate = quotationDate;
    }

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            referenceNumber = new StringFilter();
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public InstantFilter getReferenceDate() {
        return referenceDate;
    }

    public InstantFilter referenceDate() {
        if (referenceDate == null) {
            referenceDate = new InstantFilter();
        }
        return referenceDate;
    }

    public void setReferenceDate(InstantFilter referenceDate) {
        this.referenceDate = referenceDate;
    }

    public InstantFilter getEstimateDate() {
        return estimateDate;
    }

    public InstantFilter estimateDate() {
        if (estimateDate == null) {
            estimateDate = new InstantFilter();
        }
        return estimateDate;
    }

    public void setEstimateDate(InstantFilter estimateDate) {
        this.estimateDate = estimateDate;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public StringFilter subject() {
        if (subject == null) {
            subject = new StringFilter();
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public InstantFilter getValidity() {
        return validity;
    }

    public InstantFilter validity() {
        if (validity == null) {
            validity = new InstantFilter();
        }
        return validity;
    }

    public void setValidity(InstantFilter validity) {
        this.validity = validity;
    }

    public DiscountLevelTypeEnumFilter getDiscountLevelType() {
        return discountLevelType;
    }

    public DiscountLevelTypeEnumFilter discountLevelType() {
        if (discountLevelType == null) {
            discountLevelType = new DiscountLevelTypeEnumFilter();
        }
        return discountLevelType;
    }

    public void setDiscountLevelType(DiscountLevelTypeEnumFilter discountLevelType) {
        this.discountLevelType = discountLevelType;
    }

    public DiscountTypeEnumFilter getDiscountType() {
        return discountType;
    }

    public DiscountTypeEnumFilter discountType() {
        if (discountType == null) {
            discountType = new DiscountTypeEnumFilter();
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeEnumFilter discountType) {
        this.discountType = discountType;
    }

    public DoubleFilter getDiscountTypeValue() {
        return discountTypeValue;
    }

    public DoubleFilter discountTypeValue() {
        if (discountTypeValue == null) {
            discountTypeValue = new DoubleFilter();
        }
        return discountTypeValue;
    }

    public void setDiscountTypeValue(DoubleFilter discountTypeValue) {
        this.discountTypeValue = discountTypeValue;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public DoubleFilter getSubTotal() {
        return subTotal;
    }

    public DoubleFilter subTotal() {
        if (subTotal == null) {
            subTotal = new DoubleFilter();
        }
        return subTotal;
    }

    public void setSubTotal(DoubleFilter subTotal) {
        this.subTotal = subTotal;
    }

    public DoubleFilter getGrandTotal() {
        return grandTotal;
    }

    public DoubleFilter grandTotal() {
        if (grandTotal == null) {
            grandTotal = new DoubleFilter();
        }
        return grandTotal;
    }

    public void setGrandTotal(DoubleFilter grandTotal) {
        this.grandTotal = grandTotal;
    }

    public DoubleFilter getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public DoubleFilter totalTaxAmount() {
        if (totalTaxAmount == null) {
            totalTaxAmount = new DoubleFilter();
        }
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(DoubleFilter totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public DoubleFilter getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public DoubleFilter adjustmentAmount() {
        if (adjustmentAmount == null) {
            adjustmentAmount = new DoubleFilter();
        }
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(DoubleFilter adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public StringFilter getStatusReason() {
        return statusReason;
    }

    public StringFilter statusReason() {
        if (statusReason == null) {
            statusReason = new StringFilter();
        }
        return statusReason;
    }

    public void setStatusReason(StringFilter statusReason) {
        this.statusReason = statusReason;
    }

    public PDFGenerationStatusFilter getPdfGenerationStatus() {
        return pdfGenerationStatus;
    }

    public PDFGenerationStatusFilter pdfGenerationStatus() {
        if (pdfGenerationStatus == null) {
            pdfGenerationStatus = new PDFGenerationStatusFilter();
        }
        return pdfGenerationStatus;
    }

    public void setPdfGenerationStatus(PDFGenerationStatusFilter pdfGenerationStatus) {
        this.pdfGenerationStatus = pdfGenerationStatus;
    }

    public TestReportEmailStatusFilter getEmailStatus() {
        return emailStatus;
    }

    public TestReportEmailStatusFilter emailStatus() {
        if (emailStatus == null) {
            emailStatus = new TestReportEmailStatusFilter();
        }
        return emailStatus;
    }

    public void setEmailStatus(TestReportEmailStatusFilter emailStatus) {
        this.emailStatus = emailStatus;
    }

    public StringFilter getEmailFailureReason() {
        return emailFailureReason;
    }

    public StringFilter emailFailureReason() {
        if (emailFailureReason == null) {
            emailFailureReason = new StringFilter();
        }
        return emailFailureReason;
    }

    public void setEmailFailureReason(StringFilter emailFailureReason) {
        this.emailFailureReason = emailFailureReason;
    }

    public UUIDFilter getCorrelationId() {
        return correlationId;
    }

    public UUIDFilter correlationId() {
        if (correlationId == null) {
            correlationId = new UUIDFilter();
        }
        return correlationId;
    }

    public void setCorrelationId(UUIDFilter correlationId) {
        this.correlationId = correlationId;
    }

    public InstantFilter getApprovedAt() {
        return approvedAt;
    }

    public InstantFilter approvedAt() {
        if (approvedAt == null) {
            approvedAt = new InstantFilter();
        }
        return approvedAt;
    }

    public void setApprovedAt(InstantFilter approvedAt) {
        this.approvedAt = approvedAt;
    }

    public PriceDataSourceEnumFilter getPriceDataSource() {
        return priceDataSource;
    }

    public PriceDataSourceEnumFilter priceDataSource() {
        if (priceDataSource == null) {
            priceDataSource = new PriceDataSourceEnumFilter();
        }
        return priceDataSource;
    }

    public void setPriceDataSource(PriceDataSourceEnumFilter priceDataSource) {
        this.priceDataSource = priceDataSource;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getPaymentTermId() {
        return paymentTermId;
    }

    public LongFilter paymentTermId() {
        if (paymentTermId == null) {
            paymentTermId = new LongFilter();
        }
        return paymentTermId;
    }

    public void setPaymentTermId(LongFilter paymentTermId) {
        this.paymentTermId = paymentTermId;
    }

    public LongFilter getQuotationStatusId() {
        return quotationStatusId;
    }

    public LongFilter quotationStatusId() {
        if (quotationStatusId == null) {
            quotationStatusId = new LongFilter();
        }
        return quotationStatusId;
    }

    public void setQuotationStatusId(LongFilter quotationStatusId) {
        this.quotationStatusId = quotationStatusId;
    }

    public Boolean getDistinct() {
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
            Objects.equals(quotationNumber, that.quotationNumber) &&
            Objects.equals(quotationDate, that.quotationDate) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(referenceDate, that.referenceDate) &&
            Objects.equals(estimateDate, that.estimateDate) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(validity, that.validity) &&
            Objects.equals(discountLevelType, that.discountLevelType) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountTypeValue, that.discountTypeValue) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(subTotal, that.subTotal) &&
            Objects.equals(grandTotal, that.grandTotal) &&
            Objects.equals(totalTaxAmount, that.totalTaxAmount) &&
            Objects.equals(adjustmentAmount, that.adjustmentAmount) &&
            Objects.equals(statusReason, that.statusReason) &&
            Objects.equals(pdfGenerationStatus, that.pdfGenerationStatus) &&
            Objects.equals(emailStatus, that.emailStatus) &&
            Objects.equals(emailFailureReason, that.emailFailureReason) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(approvedAt, that.approvedAt) &&
            Objects.equals(priceDataSource, that.priceDataSource) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(paymentTermId, that.paymentTermId) &&
            Objects.equals(quotationStatusId, that.quotationStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            quotationNumber,
            quotationDate,
            referenceNumber,
            referenceDate,
            estimateDate,
            subject,
            validity,
            discountLevelType,
            discountType,
            discountTypeValue,
            currency,
            subTotal,
            grandTotal,
            totalTaxAmount,
            adjustmentAmount,
            statusReason,
            pdfGenerationStatus,
            emailStatus,
            emailFailureReason,
            correlationId,
            approvedAt,
            priceDataSource,
            userId,
            customerId,
            paymentTermId,
            quotationStatusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (quotationNumber != null ? "quotationNumber=" + quotationNumber + ", " : "") +
            (quotationDate != null ? "quotationDate=" + quotationDate + ", " : "") +
            (referenceNumber != null ? "referenceNumber=" + referenceNumber + ", " : "") +
            (referenceDate != null ? "referenceDate=" + referenceDate + ", " : "") +
            (estimateDate != null ? "estimateDate=" + estimateDate + ", " : "") +
            (subject != null ? "subject=" + subject + ", " : "") +
            (validity != null ? "validity=" + validity + ", " : "") +
            (discountLevelType != null ? "discountLevelType=" + discountLevelType + ", " : "") +
            (discountType != null ? "discountType=" + discountType + ", " : "") +
            (discountTypeValue != null ? "discountTypeValue=" + discountTypeValue + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
            (grandTotal != null ? "grandTotal=" + grandTotal + ", " : "") +
            (totalTaxAmount != null ? "totalTaxAmount=" + totalTaxAmount + ", " : "") +
            (adjustmentAmount != null ? "adjustmentAmount=" + adjustmentAmount + ", " : "") +
            (statusReason != null ? "statusReason=" + statusReason + ", " : "") +
            (pdfGenerationStatus != null ? "pdfGenerationStatus=" + pdfGenerationStatus + ", " : "") +
            (emailStatus != null ? "emailStatus=" + emailStatus + ", " : "") +
            (emailFailureReason != null ? "emailFailureReason=" + emailFailureReason + ", " : "") +
            (correlationId != null ? "correlationId=" + correlationId + ", " : "") +
            (approvedAt != null ? "approvedAt=" + approvedAt + ", " : "") +
            (priceDataSource != null ? "priceDataSource=" + priceDataSource + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (paymentTermId != null ? "paymentTermId=" + paymentTermId + ", " : "") +
            (quotationStatusId != null ? "quotationStatusId=" + quotationStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
