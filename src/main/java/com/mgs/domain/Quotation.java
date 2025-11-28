package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.DiscountType;
import com.mgs.domain.enumeration.QuotationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quotation.
 */
@Entity
@Table(name = "quotation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quotation extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "quote_number", length = 50, nullable = false)
    private String quoteNumber;

    @NotNull
    @Column(name = "estimate_date", nullable = false)
    private LocalDate estimateDate;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuotationStatus status;

    @Column(name = "revision_number")
    private Integer revisionNumber;

    @NotNull
    @Size(max = 10)
    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @NotNull
    @Column(name = "subtotal", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "item_discount_total", precision = 21, scale = 2)
    private BigDecimal itemDiscountTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "global_discount_type")
    private DiscountType globalDiscountType;

    @Column(name = "global_discount_value", precision = 21, scale = 2)
    private BigDecimal globalDiscountValue;

    @Column(name = "global_discount_amount", precision = 21, scale = 2)
    private BigDecimal globalDiscountAmount;

    @Column(name = "taxable_amount", precision = 21, scale = 2)
    private BigDecimal taxableAmount;

    @Column(name = "total_tax", precision = 21, scale = 2)
    private BigDecimal totalTax;

    @Column(name = "shipping_amount", precision = 21, scale = 2)
    private BigDecimal shippingAmount;

    @Column(name = "other_charges_amount", precision = 21, scale = 2)
    private BigDecimal otherChargesAmount;

    @Column(name = "round_off_amount", precision = 21, scale = 2)
    private BigDecimal roundOffAmount;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "title")
    private String title;

    @Column(name = "header_notes")
    private String headerNotes;

    @Column(name = "footer_notes")
    private String footerNotes;

    @Column(name = "terms_and_conditions")
    private String termsAndConditions;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "last_sent_at")
    private Instant lastSentAt;

    @Size(max = 100)
    @Column(name = "pdf_template_code", length = 100)
    private String pdfTemplateCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant", "department", "billingAddress", "shippingAddress", "primaryContact" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "address", "ownerUser" }, allowSetters = true)
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "contact", "source", "pipeline", "stage", "ownerUser" }, allowSetters = true)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User createdByUser;

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

    public String getQuoteNumber() {
        return this.quoteNumber;
    }

    public Quotation quoteNumber(String quoteNumber) {
        this.setQuoteNumber(quoteNumber);
        return this;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public LocalDate getEstimateDate() {
        return this.estimateDate;
    }

    public Quotation estimateDate(LocalDate estimateDate) {
        this.setEstimateDate(estimateDate);
        return this;
    }

    public void setEstimateDate(LocalDate estimateDate) {
        this.estimateDate = estimateDate;
    }

    public LocalDate getValidUntil() {
        return this.validUntil;
    }

    public Quotation validUntil(LocalDate validUntil) {
        this.setValidUntil(validUntil);
        return this;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public QuotationStatus getStatus() {
        return this.status;
    }

    public Quotation status(QuotationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public Integer getRevisionNumber() {
        return this.revisionNumber;
    }

    public Quotation revisionNumber(Integer revisionNumber) {
        this.setRevisionNumber(revisionNumber);
        return this;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
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

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public Quotation subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getItemDiscountTotal() {
        return this.itemDiscountTotal;
    }

    public Quotation itemDiscountTotal(BigDecimal itemDiscountTotal) {
        this.setItemDiscountTotal(itemDiscountTotal);
        return this;
    }

    public void setItemDiscountTotal(BigDecimal itemDiscountTotal) {
        this.itemDiscountTotal = itemDiscountTotal;
    }

    public DiscountType getGlobalDiscountType() {
        return this.globalDiscountType;
    }

    public Quotation globalDiscountType(DiscountType globalDiscountType) {
        this.setGlobalDiscountType(globalDiscountType);
        return this;
    }

    public void setGlobalDiscountType(DiscountType globalDiscountType) {
        this.globalDiscountType = globalDiscountType;
    }

    public BigDecimal getGlobalDiscountValue() {
        return this.globalDiscountValue;
    }

    public Quotation globalDiscountValue(BigDecimal globalDiscountValue) {
        this.setGlobalDiscountValue(globalDiscountValue);
        return this;
    }

    public void setGlobalDiscountValue(BigDecimal globalDiscountValue) {
        this.globalDiscountValue = globalDiscountValue;
    }

    public BigDecimal getGlobalDiscountAmount() {
        return this.globalDiscountAmount;
    }

    public Quotation globalDiscountAmount(BigDecimal globalDiscountAmount) {
        this.setGlobalDiscountAmount(globalDiscountAmount);
        return this;
    }

    public void setGlobalDiscountAmount(BigDecimal globalDiscountAmount) {
        this.globalDiscountAmount = globalDiscountAmount;
    }

    public BigDecimal getTaxableAmount() {
        return this.taxableAmount;
    }

    public Quotation taxableAmount(BigDecimal taxableAmount) {
        this.setTaxableAmount(taxableAmount);
        return this;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTotalTax() {
        return this.totalTax;
    }

    public Quotation totalTax(BigDecimal totalTax) {
        this.setTotalTax(totalTax);
        return this;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getShippingAmount() {
        return this.shippingAmount;
    }

    public Quotation shippingAmount(BigDecimal shippingAmount) {
        this.setShippingAmount(shippingAmount);
        return this;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public BigDecimal getOtherChargesAmount() {
        return this.otherChargesAmount;
    }

    public Quotation otherChargesAmount(BigDecimal otherChargesAmount) {
        this.setOtherChargesAmount(otherChargesAmount);
        return this;
    }

    public void setOtherChargesAmount(BigDecimal otherChargesAmount) {
        this.otherChargesAmount = otherChargesAmount;
    }

    public BigDecimal getRoundOffAmount() {
        return this.roundOffAmount;
    }

    public Quotation roundOffAmount(BigDecimal roundOffAmount) {
        this.setRoundOffAmount(roundOffAmount);
        return this;
    }

    public void setRoundOffAmount(BigDecimal roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Quotation totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTitle() {
        return this.title;
    }

    public Quotation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderNotes() {
        return this.headerNotes;
    }

    public Quotation headerNotes(String headerNotes) {
        this.setHeaderNotes(headerNotes);
        return this;
    }

    public void setHeaderNotes(String headerNotes) {
        this.headerNotes = headerNotes;
    }

    public String getFooterNotes() {
        return this.footerNotes;
    }

    public Quotation footerNotes(String footerNotes) {
        this.setFooterNotes(footerNotes);
        return this;
    }

    public void setFooterNotes(String footerNotes) {
        this.footerNotes = footerNotes;
    }

    public String getTermsAndConditions() {
        return this.termsAndConditions;
    }

    public Quotation termsAndConditions(String termsAndConditions) {
        this.setTermsAndConditions(termsAndConditions);
        return this;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
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

    public Instant getLastSentAt() {
        return this.lastSentAt;
    }

    public Quotation lastSentAt(Instant lastSentAt) {
        this.setLastSentAt(lastSentAt);
        return this;
    }

    public void setLastSentAt(Instant lastSentAt) {
        this.lastSentAt = lastSentAt;
    }

    public String getPdfTemplateCode() {
        return this.pdfTemplateCode;
    }

    public Quotation pdfTemplateCode(String pdfTemplateCode) {
        this.setPdfTemplateCode(pdfTemplateCode);
        return this;
    }

    public void setPdfTemplateCode(String pdfTemplateCode) {
        this.pdfTemplateCode = pdfTemplateCode;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Quotation tenant(Tenant tenant) {
        this.setTenant(tenant);
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

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Quotation contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public Lead getLead() {
        return this.lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Quotation lead(Lead lead) {
        this.setLead(lead);
        return this;
    }

    public User getCreatedByUser() {
        return this.createdByUser;
    }

    public void setCreatedByUser(User user) {
        this.createdByUser = user;
    }

    public Quotation createdByUser(User user) {
        this.setCreatedByUser(user);
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
            "}";
    }
}
