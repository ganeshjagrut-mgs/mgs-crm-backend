package com.mgs.service.criteria;

import com.mgs.domain.enumeration.CustomerStatus;
import com.mgs.domain.enumeration.CustomerType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Customer} entity. This class is used
 * in {@link com.mgs.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CustomerType
     */
    public static class CustomerTypeFilter extends Filter<CustomerType> {

        public CustomerTypeFilter() {}

        public CustomerTypeFilter(CustomerTypeFilter filter) {
            super(filter);
        }

        @Override
        public CustomerTypeFilter copy() {
            return new CustomerTypeFilter(this);
        }
    }

    /**
     * Class for filtering CustomerStatus
     */
    public static class CustomerStatusFilter extends Filter<CustomerStatus> {

        public CustomerStatusFilter() {}

        public CustomerStatusFilter(CustomerStatusFilter filter) {
            super(filter);
        }

        @Override
        public CustomerStatusFilter copy() {
            return new CustomerStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CustomerTypeFilter customerType;

    private CustomerStatusFilter status;

    private StringFilter segment;

    private StringFilter industry;

    private LongFilter tenantId;

    private LongFilter departmentId;

    private LongFilter billingAddressId;

    private LongFilter shippingAddressId;

    private LongFilter primaryContactId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.customerType = other.optionalCustomerType().map(CustomerTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(CustomerStatusFilter::copy).orElse(null);
        this.segment = other.optionalSegment().map(StringFilter::copy).orElse(null);
        this.industry = other.optionalIndustry().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.billingAddressId = other.optionalBillingAddressId().map(LongFilter::copy).orElse(null);
        this.shippingAddressId = other.optionalShippingAddressId().map(LongFilter::copy).orElse(null);
        this.primaryContactId = other.optionalPrimaryContactId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public CustomerTypeFilter getCustomerType() {
        return customerType;
    }

    public Optional<CustomerTypeFilter> optionalCustomerType() {
        return Optional.ofNullable(customerType);
    }

    public CustomerTypeFilter customerType() {
        if (customerType == null) {
            setCustomerType(new CustomerTypeFilter());
        }
        return customerType;
    }

    public void setCustomerType(CustomerTypeFilter customerType) {
        this.customerType = customerType;
    }

    public CustomerStatusFilter getStatus() {
        return status;
    }

    public Optional<CustomerStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public CustomerStatusFilter status() {
        if (status == null) {
            setStatus(new CustomerStatusFilter());
        }
        return status;
    }

    public void setStatus(CustomerStatusFilter status) {
        this.status = status;
    }

    public StringFilter getSegment() {
        return segment;
    }

    public Optional<StringFilter> optionalSegment() {
        return Optional.ofNullable(segment);
    }

    public StringFilter segment() {
        if (segment == null) {
            setSegment(new StringFilter());
        }
        return segment;
    }

    public void setSegment(StringFilter segment) {
        this.segment = segment;
    }

    public StringFilter getIndustry() {
        return industry;
    }

    public Optional<StringFilter> optionalIndustry() {
        return Optional.ofNullable(industry);
    }

    public StringFilter industry() {
        if (industry == null) {
            setIndustry(new StringFilter());
        }
        return industry;
    }

    public void setIndustry(StringFilter industry) {
        this.industry = industry;
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

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getBillingAddressId() {
        return billingAddressId;
    }

    public Optional<LongFilter> optionalBillingAddressId() {
        return Optional.ofNullable(billingAddressId);
    }

    public LongFilter billingAddressId() {
        if (billingAddressId == null) {
            setBillingAddressId(new LongFilter());
        }
        return billingAddressId;
    }

    public void setBillingAddressId(LongFilter billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public LongFilter getShippingAddressId() {
        return shippingAddressId;
    }

    public Optional<LongFilter> optionalShippingAddressId() {
        return Optional.ofNullable(shippingAddressId);
    }

    public LongFilter shippingAddressId() {
        if (shippingAddressId == null) {
            setShippingAddressId(new LongFilter());
        }
        return shippingAddressId;
    }

    public void setShippingAddressId(LongFilter shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public LongFilter getPrimaryContactId() {
        return primaryContactId;
    }

    public Optional<LongFilter> optionalPrimaryContactId() {
        return Optional.ofNullable(primaryContactId);
    }

    public LongFilter primaryContactId() {
        if (primaryContactId == null) {
            setPrimaryContactId(new LongFilter());
        }
        return primaryContactId;
    }

    public void setPrimaryContactId(LongFilter primaryContactId) {
        this.primaryContactId = primaryContactId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerType, that.customerType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(segment, that.segment) &&
            Objects.equals(industry, that.industry) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(billingAddressId, that.billingAddressId) &&
            Objects.equals(shippingAddressId, that.shippingAddressId) &&
            Objects.equals(primaryContactId, that.primaryContactId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerType,
            status,
            segment,
            industry,
            tenantId,
            departmentId,
            billingAddressId,
            shippingAddressId,
            primaryContactId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCustomerType().map(f -> "customerType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSegment().map(f -> "segment=" + f + ", ").orElse("") +
            optionalIndustry().map(f -> "industry=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalBillingAddressId().map(f -> "billingAddressId=" + f + ", ").orElse("") +
            optionalShippingAddressId().map(f -> "shippingAddressId=" + f + ", ").orElse("") +
            optionalPrimaryContactId().map(f -> "primaryContactId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
