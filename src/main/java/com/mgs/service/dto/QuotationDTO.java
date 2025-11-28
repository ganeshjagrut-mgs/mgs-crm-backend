package com.mgs.service.dto;

import com.mgs.domain.enumeration.DiscountType;
import com.mgs.domain.enumeration.QuotationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Quotation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String quoteNumber;

    @NotNull
    private LocalDate estimateDate;

    private LocalDate validUntil;

    @NotNull
    private QuotationStatus status;

    private Integer revisionNumber;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    private BigDecimal subtotal;

    private BigDecimal itemDiscountTotal;

    private DiscountType globalDiscountType;

    private BigDecimal globalDiscountValue;

    private BigDecimal globalDiscountAmount;

    private BigDecimal taxableAmount;

    private BigDecimal totalTax;

    private BigDecimal shippingAmount;

    private BigDecimal otherChargesAmount;

    private BigDecimal roundOffAmount;

    @NotNull
    private BigDecimal totalAmount;

    private String title;

    private String headerNotes;

    private String footerNotes;

    private String termsAndConditions;

    private String referenceNumber;

    private Instant lastSentAt;

    @Size(max = 100)
    private String pdfTemplateCode;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private CustomerDTO customer;

    private ContactDTO contact;

    private LeadDTO lead;

    private UserDTO createdByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public LocalDate getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(LocalDate estimateDate) {
        this.estimateDate = estimateDate;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getItemDiscountTotal() {
        return itemDiscountTotal;
    }

    public void setItemDiscountTotal(BigDecimal itemDiscountTotal) {
        this.itemDiscountTotal = itemDiscountTotal;
    }

    public DiscountType getGlobalDiscountType() {
        return globalDiscountType;
    }

    public void setGlobalDiscountType(DiscountType globalDiscountType) {
        this.globalDiscountType = globalDiscountType;
    }

    public BigDecimal getGlobalDiscountValue() {
        return globalDiscountValue;
    }

    public void setGlobalDiscountValue(BigDecimal globalDiscountValue) {
        this.globalDiscountValue = globalDiscountValue;
    }

    public BigDecimal getGlobalDiscountAmount() {
        return globalDiscountAmount;
    }

    public void setGlobalDiscountAmount(BigDecimal globalDiscountAmount) {
        this.globalDiscountAmount = globalDiscountAmount;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimal getOtherChargesAmount() {
        return otherChargesAmount;
    }

    public void setOtherChargesAmount(BigDecimal otherChargesAmount) {
        this.otherChargesAmount = otherChargesAmount;
    }

    public BigDecimal getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(BigDecimal roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderNotes() {
        return headerNotes;
    }

    public void setHeaderNotes(String headerNotes) {
        this.headerNotes = headerNotes;
    }

    public String getFooterNotes() {
        return footerNotes;
    }

    public void setFooterNotes(String footerNotes) {
        this.footerNotes = footerNotes;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Instant getLastSentAt() {
        return lastSentAt;
    }

    public void setLastSentAt(Instant lastSentAt) {
        this.lastSentAt = lastSentAt;
    }

    public String getPdfTemplateCode() {
        return pdfTemplateCode;
    }

    public void setPdfTemplateCode(String pdfTemplateCode) {
        this.pdfTemplateCode = pdfTemplateCode;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public LeadDTO getLead() {
        return lead;
    }

    public void setLead(LeadDTO lead) {
        this.lead = lead;
    }

    public UserDTO getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserDTO createdByUser) {
        this.createdByUser = createdByUser;
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
            ", quoteNumber='" + getQuoteNumber() + "'" +
            ", estimateDate='" + getEstimateDate() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", status='" + getStatus() + "'" +
            ", revisionNumber=" + getRevisionNumber() +
            ", currency='" + getCurrency() + "'" +
            ", subtotal=" + getSubtotal() +
            ", itemDiscountTotal=" + getItemDiscountTotal() +
            ", globalDiscountType='" + getGlobalDiscountType() + "'" +
            ", globalDiscountValue=" + getGlobalDiscountValue() +
            ", globalDiscountAmount=" + getGlobalDiscountAmount() +
            ", taxableAmount=" + getTaxableAmount() +
            ", totalTax=" + getTotalTax() +
            ", shippingAmount=" + getShippingAmount() +
            ", otherChargesAmount=" + getOtherChargesAmount() +
            ", roundOffAmount=" + getRoundOffAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", title='" + getTitle() + "'" +
            ", headerNotes='" + getHeaderNotes() + "'" +
            ", footerNotes='" + getFooterNotes() + "'" +
            ", termsAndConditions='" + getTermsAndConditions() + "'" +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", lastSentAt='" + getLastSentAt() + "'" +
            ", pdfTemplateCode='" + getPdfTemplateCode() + "'" +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            ", contact=" + getContact() +
            ", lead=" + getLead() +
            ", createdByUser=" + getCreatedByUser() +
            "}";
    }
}
