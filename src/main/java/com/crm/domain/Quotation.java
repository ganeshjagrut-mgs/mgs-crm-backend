package com.crm.domain;

import com.crm.domain.enumeration.DiscountLevelTypeEnum;
import com.crm.domain.enumeration.DiscountTypeEnum;
import com.crm.domain.enumeration.PDFGenerationStatus;
import com.crm.domain.enumeration.PriceDataSourceEnum;
import com.crm.domain.enumeration.TestReportEmailStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Quotation - Sales quotations
 */
@Entity
@Table(name = "quotation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quotation_number")
    private String quotationNumber;

    @Column(name = "quotation_date")
    private Instant quotationDate;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "reference_date")
    private Instant referenceDate;

    @Column(name = "estimate_date")
    private Instant estimateDate;

    @Column(name = "subject")
    private String subject;

    @Column(name = "validity")
    private Instant validity;

    @Lob
    @Column(name = "additional_note")
    private String additionalNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_level_type")
    private DiscountLevelTypeEnum discountLevelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountTypeEnum discountType;

    @Column(name = "discount_type_value")
    private Double discountTypeValue;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sub_total")
    private Double subTotal;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "total_tax_amount")
    private Double totalTaxAmount;

    @Column(name = "adjustment_amount")
    private Double adjustmentAmount;

    @Column(name = "status_reason")
    private String statusReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "pdf_generation_status")
    private PDFGenerationStatus pdfGenerationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status")
    private TestReportEmailStatus emailStatus;

    @Column(name = "email_failure_reason")
    private String emailFailureReason;

    @Lob
    @Column(name = "custom_paragraph")
    private String customParagraph;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_data_source")
    private PriceDataSourceEnum priceDataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "addresses",
            "company",
            "user",
            "customerType",
            "customerStatus",
            "ownershipType",
            "industryType",
            "customerCategory",
            "paymentTerms",
            "invoiceFrequency",
            "gstTreatment",
            "outstandingPerson",
            "department",
            "tenat",
            "masterCategories",
        },
        allowSetters = true
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType paymentTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType quotationStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quotation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuotationNumber() {
        return this.quotationNumber;
    }

    public Quotation quotationNumber(String quotationNumber) {
        this.setQuotationNumber(quotationNumber);
        return this;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public Instant getQuotationDate() {
        return this.quotationDate;
    }

    public Quotation quotationDate(Instant quotationDate) {
        this.setQuotationDate(quotationDate);
        return this;
    }

    public void setQuotationDate(Instant quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public Quotation referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Instant getReferenceDate() {
        return this.referenceDate;
    }

    public Quotation referenceDate(Instant referenceDate) {
        this.setReferenceDate(referenceDate);
        return this;
    }

    public void setReferenceDate(Instant referenceDate) {
        this.referenceDate = referenceDate;
    }

    public Instant getEstimateDate() {
        return this.estimateDate;
    }

    public Quotation estimateDate(Instant estimateDate) {
        this.setEstimateDate(estimateDate);
        return this;
    }

    public void setEstimateDate(Instant estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getSubject() {
        return this.subject;
    }

    public Quotation subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getValidity() {
        return this.validity;
    }

    public Quotation validity(Instant validity) {
        this.setValidity(validity);
        return this;
    }

    public void setValidity(Instant validity) {
        this.validity = validity;
    }

    public String getAdditionalNote() {
        return this.additionalNote;
    }

    public Quotation additionalNote(String additionalNote) {
        this.setAdditionalNote(additionalNote);
        return this;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public DiscountLevelTypeEnum getDiscountLevelType() {
        return this.discountLevelType;
    }

    public Quotation discountLevelType(DiscountLevelTypeEnum discountLevelType) {
        this.setDiscountLevelType(discountLevelType);
        return this;
    }

    public void setDiscountLevelType(DiscountLevelTypeEnum discountLevelType) {
        this.discountLevelType = discountLevelType;
    }

    public DiscountTypeEnum getDiscountType() {
        return this.discountType;
    }

    public Quotation discountType(DiscountTypeEnum discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountTypeEnum discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountTypeValue() {
        return this.discountTypeValue;
    }

    public Quotation discountTypeValue(Double discountTypeValue) {
        this.setDiscountTypeValue(discountTypeValue);
        return this;
    }

    public void setDiscountTypeValue(Double discountTypeValue) {
        this.discountTypeValue = discountTypeValue;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Quotation currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSubTotal() {
        return this.subTotal;
    }

    public Quotation subTotal(Double subTotal) {
        this.setSubTotal(subTotal);
        return this;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getGrandTotal() {
        return this.grandTotal;
    }

    public Quotation grandTotal(Double grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getTotalTaxAmount() {
        return this.totalTaxAmount;
    }

    public Quotation totalTaxAmount(Double totalTaxAmount) {
        this.setTotalTaxAmount(totalTaxAmount);
        return this;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getAdjustmentAmount() {
        return this.adjustmentAmount;
    }

    public Quotation adjustmentAmount(Double adjustmentAmount) {
        this.setAdjustmentAmount(adjustmentAmount);
        return this;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getStatusReason() {
        return this.statusReason;
    }

    public Quotation statusReason(String statusReason) {
        this.setStatusReason(statusReason);
        return this;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public PDFGenerationStatus getPdfGenerationStatus() {
        return this.pdfGenerationStatus;
    }

    public Quotation pdfGenerationStatus(PDFGenerationStatus pdfGenerationStatus) {
        this.setPdfGenerationStatus(pdfGenerationStatus);
        return this;
    }

    public void setPdfGenerationStatus(PDFGenerationStatus pdfGenerationStatus) {
        this.pdfGenerationStatus = pdfGenerationStatus;
    }

    public TestReportEmailStatus getEmailStatus() {
        return this.emailStatus;
    }

    public Quotation emailStatus(TestReportEmailStatus emailStatus) {
        this.setEmailStatus(emailStatus);
        return this;
    }

    public void setEmailStatus(TestReportEmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getEmailFailureReason() {
        return this.emailFailureReason;
    }

    public Quotation emailFailureReason(String emailFailureReason) {
        this.setEmailFailureReason(emailFailureReason);
        return this;
    }

    public void setEmailFailureReason(String emailFailureReason) {
        this.emailFailureReason = emailFailureReason;
    }

    public String getCustomParagraph() {
        return this.customParagraph;
    }

    public Quotation customParagraph(String customParagraph) {
        this.setCustomParagraph(customParagraph);
        return this;
    }

    public void setCustomParagraph(String customParagraph) {
        this.customParagraph = customParagraph;
    }

    public UUID getCorrelationId() {
        return this.correlationId;
    }

    public Quotation correlationId(UUID correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getApprovedAt() {
        return this.approvedAt;
    }

    public Quotation approvedAt(Instant approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public PriceDataSourceEnum getPriceDataSource() {
        return this.priceDataSource;
    }

    public Quotation priceDataSource(PriceDataSourceEnum priceDataSource) {
        this.setPriceDataSource(priceDataSource);
        return this;
    }

    public void setPriceDataSource(PriceDataSourceEnum priceDataSource) {
        this.priceDataSource = priceDataSource;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quotation user(User user) {
        this.setUser(user);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Quotation customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public MasterStaticType getPaymentTerm() {
        return this.paymentTerm;
    }

    public void setPaymentTerm(MasterStaticType masterStaticType) {
        this.paymentTerm = masterStaticType;
    }

    public Quotation paymentTerm(MasterStaticType masterStaticType) {
        this.setPaymentTerm(masterStaticType);
        return this;
    }

    public MasterStaticType getQuotationStatus() {
        return this.quotationStatus;
    }

    public void setQuotationStatus(MasterStaticType masterStaticType) {
        this.quotationStatus = masterStaticType;
    }

    public Quotation quotationStatus(MasterStaticType masterStaticType) {
        this.setQuotationStatus(masterStaticType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quotation)) {
            return false;
        }
        return getId() != null && getId().equals(((Quotation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quotation{" +
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
            "}";
    }
}
