package com.crm.service.dto;

import com.crm.domain.enumeration.DiscountLevelTypeEnum;
import com.crm.domain.enumeration.DiscountTypeEnum;
import com.crm.domain.enumeration.PDFGenerationStatus;
import com.crm.domain.enumeration.PriceDataSourceEnum;
import com.crm.domain.enumeration.TestReportEmailStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.crm.domain.Quotation} entity.
 */
@Schema(description = "Quotation - Sales quotations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDTO implements Serializable {

    private Long id;

    private String quotationNumber;

    private Instant quotationDate;

    private String referenceNumber;

    private Instant referenceDate;

    private Instant estimateDate;

    private String subject;

    private Instant validity;

    @Lob
    private String additionalNote;

    private DiscountLevelTypeEnum discountLevelType;

    private DiscountTypeEnum discountType;

    private Double discountTypeValue;

    private String currency;

    private Double subTotal;

    private Double grandTotal;

    private Double totalTaxAmount;

    private Double adjustmentAmount;

    private String statusReason;

    private PDFGenerationStatus pdfGenerationStatus;

    private TestReportEmailStatus emailStatus;

    private String emailFailureReason;

    @Lob
    private String customParagraph;

    private UUID correlationId;

    private Instant approvedAt;

    private PriceDataSourceEnum priceDataSource;

    private UserDTO user;

    private CustomerDTO customer;

    private MasterStaticTypeDTO paymentTerm;

    private MasterStaticTypeDTO quotationStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public Instant getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Instant quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Instant getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Instant referenceDate) {
        this.referenceDate = referenceDate;
    }

    public Instant getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(Instant estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getValidity() {
        return validity;
    }

    public void setValidity(Instant validity) {
        this.validity = validity;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public DiscountLevelTypeEnum getDiscountLevelType() {
        return discountLevelType;
    }

    public void setDiscountLevelType(DiscountLevelTypeEnum discountLevelType) {
        this.discountLevelType = discountLevelType;
    }

    public DiscountTypeEnum getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeEnum discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountTypeValue() {
        return discountTypeValue;
    }

    public void setDiscountTypeValue(Double discountTypeValue) {
        this.discountTypeValue = discountTypeValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public PDFGenerationStatus getPdfGenerationStatus() {
        return pdfGenerationStatus;
    }

    public void setPdfGenerationStatus(PDFGenerationStatus pdfGenerationStatus) {
        this.pdfGenerationStatus = pdfGenerationStatus;
    }

    public TestReportEmailStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(TestReportEmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getEmailFailureReason() {
        return emailFailureReason;
    }

    public void setEmailFailureReason(String emailFailureReason) {
        this.emailFailureReason = emailFailureReason;
    }

    public String getCustomParagraph() {
        return customParagraph;
    }

    public void setCustomParagraph(String customParagraph) {
        this.customParagraph = customParagraph;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public PriceDataSourceEnum getPriceDataSource() {
        return priceDataSource;
    }

    public void setPriceDataSource(PriceDataSourceEnum priceDataSource) {
        this.priceDataSource = priceDataSource;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public MasterStaticTypeDTO getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(MasterStaticTypeDTO paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public MasterStaticTypeDTO getQuotationStatus() {
        return quotationStatus;
    }

    public void setQuotationStatus(MasterStaticTypeDTO quotationStatus) {
        this.quotationStatus = quotationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationDTO)) {
            return false;
        }

        QuotationDTO quotationDTO = (QuotationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationDTO{" +
            "id=" + getId() +
            ", quotationNumber='" + getQuotationNumber() + "'" +
            ", quotationDate='" + getQuotationDate() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", referenceDate='" + getReferenceDate() + "'" +
            ", estimateDate='" + getEstimateDate() + "'" +
            ", subject='" + getSubject() + "'" +
            ", validity='" + getValidity() + "'" +
            ", additionalNote='" + getAdditionalNote() + "'" +
            ", discountLevelType='" + getDiscountLevelType() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountTypeValue=" + getDiscountTypeValue() +
            ", currency='" + getCurrency() + "'" +
            ", subTotal=" + getSubTotal() +
            ", grandTotal=" + getGrandTotal() +
            ", totalTaxAmount=" + getTotalTaxAmount() +
            ", adjustmentAmount=" + getAdjustmentAmount() +
            ", statusReason='" + getStatusReason() + "'" +
            ", pdfGenerationStatus='" + getPdfGenerationStatus() + "'" +
            ", emailStatus='" + getEmailStatus() + "'" +
            ", emailFailureReason='" + getEmailFailureReason() + "'" +
            ", customParagraph='" + getCustomParagraph() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", priceDataSource='" + getPriceDataSource() + "'" +
            ", user=" + getUser() +
            ", customer=" + getCustomer() +
            ", paymentTerm=" + getPaymentTerm() +
            ", quotationStatus=" + getQuotationStatus() +
            "}";
    }
}
