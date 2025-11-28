package com.mgs.service.criteria;

import com.mgs.domain.enumeration.DiscountType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.QuotationItem} entity. This class is used
 * in {@link com.mgs.web.rest.QuotationItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotation-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationItemCriteria implements Serializable, Criteria {

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

    private StringFilter productName;

    private StringFilter productSku;

    private StringFilter productDescription;

    private StringFilter unitLabel;

    private BigDecimalFilter quantity;

    private BigDecimalFilter unitPrice;

    private DiscountTypeFilter discountType;

    private BigDecimalFilter discountValue;

    private BigDecimalFilter discountAmount;

    private BigDecimalFilter taxableAmount;

    private BigDecimalFilter taxRate;

    private BigDecimalFilter taxAmount;

    private BigDecimalFilter lineTotal;

    private IntegerFilter sortOrder;

    private LongFilter tenantId;

    private LongFilter quotationId;

    private LongFilter productId;

    private Boolean distinct;

    public QuotationItemCriteria() {}

    public QuotationItemCriteria(QuotationItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.productName = other.optionalProductName().map(StringFilter::copy).orElse(null);
        this.productSku = other.optionalProductSku().map(StringFilter::copy).orElse(null);
        this.productDescription = other.optionalProductDescription().map(StringFilter::copy).orElse(null);
        this.unitLabel = other.optionalUnitLabel().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.unitPrice = other.optionalUnitPrice().map(BigDecimalFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(DiscountTypeFilter::copy).orElse(null);
        this.discountValue = other.optionalDiscountValue().map(BigDecimalFilter::copy).orElse(null);
        this.discountAmount = other.optionalDiscountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.taxableAmount = other.optionalTaxableAmount().map(BigDecimalFilter::copy).orElse(null);
        this.taxRate = other.optionalTaxRate().map(BigDecimalFilter::copy).orElse(null);
        this.taxAmount = other.optionalTaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.lineTotal = other.optionalLineTotal().map(BigDecimalFilter::copy).orElse(null);
        this.sortOrder = other.optionalSortOrder().map(IntegerFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.quotationId = other.optionalQuotationId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuotationItemCriteria copy() {
        return new QuotationItemCriteria(this);
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

    public StringFilter getProductName() {
        return productName;
    }

    public Optional<StringFilter> optionalProductName() {
        return Optional.ofNullable(productName);
    }

    public StringFilter productName() {
        if (productName == null) {
            setProductName(new StringFilter());
        }
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public StringFilter getProductSku() {
        return productSku;
    }

    public Optional<StringFilter> optionalProductSku() {
        return Optional.ofNullable(productSku);
    }

    public StringFilter productSku() {
        if (productSku == null) {
            setProductSku(new StringFilter());
        }
        return productSku;
    }

    public void setProductSku(StringFilter productSku) {
        this.productSku = productSku;
    }

    public StringFilter getProductDescription() {
        return productDescription;
    }

    public Optional<StringFilter> optionalProductDescription() {
        return Optional.ofNullable(productDescription);
    }

    public StringFilter productDescription() {
        if (productDescription == null) {
            setProductDescription(new StringFilter());
        }
        return productDescription;
    }

    public void setProductDescription(StringFilter productDescription) {
        this.productDescription = productDescription;
    }

    public StringFilter getUnitLabel() {
        return unitLabel;
    }

    public Optional<StringFilter> optionalUnitLabel() {
        return Optional.ofNullable(unitLabel);
    }

    public StringFilter unitLabel() {
        if (unitLabel == null) {
            setUnitLabel(new StringFilter());
        }
        return unitLabel;
    }

    public void setUnitLabel(StringFilter unitLabel) {
        this.unitLabel = unitLabel;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public Optional<BigDecimalFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public BigDecimalFilter quantity() {
        if (quantity == null) {
            setQuantity(new BigDecimalFilter());
        }
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public Optional<BigDecimalFilter> optionalUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    public BigDecimalFilter unitPrice() {
        if (unitPrice == null) {
            setUnitPrice(new BigDecimalFilter());
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DiscountTypeFilter getDiscountType() {
        return discountType;
    }

    public Optional<DiscountTypeFilter> optionalDiscountType() {
        return Optional.ofNullable(discountType);
    }

    public DiscountTypeFilter discountType() {
        if (discountType == null) {
            setDiscountType(new DiscountTypeFilter());
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountValue() {
        return discountValue;
    }

    public Optional<BigDecimalFilter> optionalDiscountValue() {
        return Optional.ofNullable(discountValue);
    }

    public BigDecimalFilter discountValue() {
        if (discountValue == null) {
            setDiscountValue(new BigDecimalFilter());
        }
        return discountValue;
    }

    public void setDiscountValue(BigDecimalFilter discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public Optional<BigDecimalFilter> optionalDiscountAmount() {
        return Optional.ofNullable(discountAmount);
    }

    public BigDecimalFilter discountAmount() {
        if (discountAmount == null) {
            setDiscountAmount(new BigDecimalFilter());
        }
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
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

    public BigDecimalFilter getTaxRate() {
        return taxRate;
    }

    public Optional<BigDecimalFilter> optionalTaxRate() {
        return Optional.ofNullable(taxRate);
    }

    public BigDecimalFilter taxRate() {
        if (taxRate == null) {
            setTaxRate(new BigDecimalFilter());
        }
        return taxRate;
    }

    public void setTaxRate(BigDecimalFilter taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public Optional<BigDecimalFilter> optionalTaxAmount() {
        return Optional.ofNullable(taxAmount);
    }

    public BigDecimalFilter taxAmount() {
        if (taxAmount == null) {
            setTaxAmount(new BigDecimalFilter());
        }
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimalFilter getLineTotal() {
        return lineTotal;
    }

    public Optional<BigDecimalFilter> optionalLineTotal() {
        return Optional.ofNullable(lineTotal);
    }

    public BigDecimalFilter lineTotal() {
        if (lineTotal == null) {
            setLineTotal(new BigDecimalFilter());
        }
        return lineTotal;
    }

    public void setLineTotal(BigDecimalFilter lineTotal) {
        this.lineTotal = lineTotal;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public Optional<IntegerFilter> optionalSortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            setSortOrder(new IntegerFilter());
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
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

    public LongFilter getQuotationId() {
        return quotationId;
    }

    public Optional<LongFilter> optionalQuotationId() {
        return Optional.ofNullable(quotationId);
    }

    public LongFilter quotationId() {
        if (quotationId == null) {
            setQuotationId(new LongFilter());
        }
        return quotationId;
    }

    public void setQuotationId(LongFilter quotationId) {
        this.quotationId = quotationId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final QuotationItemCriteria that = (QuotationItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productSku, that.productSku) &&
            Objects.equals(productDescription, that.productDescription) &&
            Objects.equals(unitLabel, that.unitLabel) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountValue, that.discountValue) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(taxableAmount, that.taxableAmount) &&
            Objects.equals(taxRate, that.taxRate) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(lineTotal, that.lineTotal) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(quotationId, that.quotationId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            productName,
            productSku,
            productDescription,
            unitLabel,
            quantity,
            unitPrice,
            discountType,
            discountValue,
            discountAmount,
            taxableAmount,
            taxRate,
            taxAmount,
            lineTotal,
            sortOrder,
            tenantId,
            quotationId,
            productId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProductName().map(f -> "productName=" + f + ", ").orElse("") +
            optionalProductSku().map(f -> "productSku=" + f + ", ").orElse("") +
            optionalProductDescription().map(f -> "productDescription=" + f + ", ").orElse("") +
            optionalUnitLabel().map(f -> "unitLabel=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalUnitPrice().map(f -> "unitPrice=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountValue().map(f -> "discountValue=" + f + ", ").orElse("") +
            optionalDiscountAmount().map(f -> "discountAmount=" + f + ", ").orElse("") +
            optionalTaxableAmount().map(f -> "taxableAmount=" + f + ", ").orElse("") +
            optionalTaxRate().map(f -> "taxRate=" + f + ", ").orElse("") +
            optionalTaxAmount().map(f -> "taxAmount=" + f + ", ").orElse("") +
            optionalLineTotal().map(f -> "lineTotal=" + f + ", ").orElse("") +
            optionalSortOrder().map(f -> "sortOrder=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalQuotationId().map(f -> "quotationId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
