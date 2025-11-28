package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuotationItem.
 */
@Entity
@Table(name = "quotation_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationItem extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @Size(max = 100)
    @Column(name = "product_sku", length = 100)
    private String productSku;

    @Column(name = "product_description")
    private String productDescription;

    @Size(max = 50)
    @Column(name = "unit_label", length = 50)
    private String unitLabel;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount_value", precision = 21, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "discount_amount", precision = 21, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "taxable_amount", precision = 21, scale = 2)
    private BigDecimal taxableAmount;

    @Column(name = "tax_rate", precision = 21, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 21, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "line_total", precision = 21, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant", "customer", "contact", "lead", "createdByUser" }, allowSetters = true)
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuotationItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public QuotationItem productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return this.productSku;
    }

    public QuotationItem productSku(String productSku) {
        this.setProductSku(productSku);
        return this;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public QuotationItem productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getUnitLabel() {
        return this.unitLabel;
    }

    public QuotationItem unitLabel(String unitLabel) {
        this.setUnitLabel(unitLabel);
        return this;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public QuotationItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public QuotationItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public QuotationItem discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public QuotationItem discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public QuotationItem discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxableAmount() {
        return this.taxableAmount;
    }

    public QuotationItem taxableAmount(BigDecimal taxableAmount) {
        this.setTaxableAmount(taxableAmount);
        return this;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public QuotationItem taxRate(BigDecimal taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public QuotationItem taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getLineTotal() {
        return this.lineTotal;
    }

    public QuotationItem lineTotal(BigDecimal lineTotal) {
        this.setLineTotal(lineTotal);
        return this;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public QuotationItem sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public QuotationItem tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Quotation getQuotation() {
        return this.quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public QuotationItem quotation(Quotation quotation) {
        this.setQuotation(quotation);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public QuotationItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationItem)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotationItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationItem{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productSku='" + getProductSku() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", unitLabel='" + getUnitLabel() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", discountAmount=" + getDiscountAmount() +
            ", taxableAmount=" + getTaxableAmount() +
            ", taxRate=" + getTaxRate() +
            ", taxAmount=" + getTaxAmount() +
            ", lineTotal=" + getLineTotal() +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
