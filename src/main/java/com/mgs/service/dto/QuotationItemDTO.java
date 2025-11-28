package com.mgs.service.dto;

import com.mgs.domain.enumeration.DiscountType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.QuotationItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String productName;

    @Size(max = 100)
    private String productSku;

    private String productDescription;

    @Size(max = 50)
    private String unitLabel;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal unitPrice;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private BigDecimal discountAmount;

    private BigDecimal taxableAmount;

    private BigDecimal taxRate;

    private BigDecimal taxAmount;

    private BigDecimal lineTotal;

    private Integer sortOrder;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private QuotationDTO quotation;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public QuotationDTO getQuotation() {
        return quotation;
    }

    public void setQuotation(QuotationDTO quotation) {
        this.quotation = quotation;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationItemDTO)) {
            return false;
        }

        QuotationItemDTO quotationItemDTO = (QuotationItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationItemDTO{" +
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
            ", tenant=" + getTenant() +
            ", quotation=" + getQuotation() +
            ", product=" + getProduct() +
            "}";
    }
}
