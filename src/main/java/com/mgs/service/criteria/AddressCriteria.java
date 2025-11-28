package com.mgs.service.criteria;

import com.mgs.domain.enumeration.AddressType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Address} entity. This class is used
 * in {@link com.mgs.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AddressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AddressType
     */
    public static class AddressTypeFilter extends Filter<AddressType> {

        public AddressTypeFilter() {}

        public AddressTypeFilter(AddressTypeFilter filter) {
            super(filter);
        }

        @Override
        public AddressTypeFilter copy() {
            return new AddressTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AddressTypeFilter addressType;

    private LongFilter tenantId;

    private LongFilter tenantProfileId;

    private Boolean distinct;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.addressType = other.optionalAddressType().map(AddressTypeFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.tenantProfileId = other.optionalTenantProfileId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
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

    public AddressTypeFilter getAddressType() {
        return addressType;
    }

    public Optional<AddressTypeFilter> optionalAddressType() {
        return Optional.ofNullable(addressType);
    }

    public AddressTypeFilter addressType() {
        if (addressType == null) {
            setAddressType(new AddressTypeFilter());
        }
        return addressType;
    }

    public void setAddressType(AddressTypeFilter addressType) {
        this.addressType = addressType;
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

    public LongFilter getTenantProfileId() {
        return tenantProfileId;
    }

    public Optional<LongFilter> optionalTenantProfileId() {
        return Optional.ofNullable(tenantProfileId);
    }

    public LongFilter tenantProfileId() {
        if (tenantProfileId == null) {
            setTenantProfileId(new LongFilter());
        }
        return tenantProfileId;
    }

    public void setTenantProfileId(LongFilter tenantProfileId) {
        this.tenantProfileId = tenantProfileId;
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
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(addressType, that.addressType) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(tenantProfileId, that.tenantProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addressType, tenantId, tenantProfileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAddressType().map(f -> "addressType=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTenantProfileId().map(f -> "tenantProfileId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
