package com.mgs.service.criteria;

import com.mgs.domain.enumeration.TenantStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Tenant} entity. This class is used
 * in {@link com.mgs.web.rest.TenantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TenantStatus
     */
    public static class TenantStatusFilter extends Filter<TenantStatus> {

        public TenantStatusFilter() {}

        public TenantStatusFilter(TenantStatusFilter filter) {
            super(filter);
        }

        @Override
        public TenantStatusFilter copy() {
            return new TenantStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private TenantStatusFilter status;

    private LongFilter tenantProfileId;

    private LongFilter tenantBrandingId;

    private Boolean distinct;

    public TenantCriteria() {}

    public TenantCriteria(TenantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TenantStatusFilter::copy).orElse(null);
        this.tenantProfileId = other.optionalTenantProfileId().map(LongFilter::copy).orElse(null);
        this.tenantBrandingId = other.optionalTenantBrandingId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantCriteria copy() {
        return new TenantCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public TenantStatusFilter getStatus() {
        return status;
    }

    public Optional<TenantStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TenantStatusFilter status() {
        if (status == null) {
            setStatus(new TenantStatusFilter());
        }
        return status;
    }

    public void setStatus(TenantStatusFilter status) {
        this.status = status;
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

    public LongFilter getTenantBrandingId() {
        return tenantBrandingId;
    }

    public Optional<LongFilter> optionalTenantBrandingId() {
        return Optional.ofNullable(tenantBrandingId);
    }

    public LongFilter tenantBrandingId() {
        if (tenantBrandingId == null) {
            setTenantBrandingId(new LongFilter());
        }
        return tenantBrandingId;
    }

    public void setTenantBrandingId(LongFilter tenantBrandingId) {
        this.tenantBrandingId = tenantBrandingId;
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
        final TenantCriteria that = (TenantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tenantProfileId, that.tenantProfileId) &&
            Objects.equals(tenantBrandingId, that.tenantBrandingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, status, tenantProfileId, tenantBrandingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTenantProfileId().map(f -> "tenantProfileId=" + f + ", ").orElse("") +
            optionalTenantBrandingId().map(f -> "tenantBrandingId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
