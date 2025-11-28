package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.TenantProfile} entity. This class is used
 * in {@link com.mgs.web.rest.TenantProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subdomain;

    private StringFilter customDomain;

    private BooleanFilter domainVerified;

    private StringFilter defaultLocale;

    private StringFilter timezone;

    private LongFilter tenantId;

    private LongFilter addressId;

    private Boolean distinct;

    public TenantProfileCriteria() {}

    public TenantProfileCriteria(TenantProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.subdomain = other.optionalSubdomain().map(StringFilter::copy).orElse(null);
        this.customDomain = other.optionalCustomDomain().map(StringFilter::copy).orElse(null);
        this.domainVerified = other.optionalDomainVerified().map(BooleanFilter::copy).orElse(null);
        this.defaultLocale = other.optionalDefaultLocale().map(StringFilter::copy).orElse(null);
        this.timezone = other.optionalTimezone().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantProfileCriteria copy() {
        return new TenantProfileCriteria(this);
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

    public StringFilter getSubdomain() {
        return subdomain;
    }

    public Optional<StringFilter> optionalSubdomain() {
        return Optional.ofNullable(subdomain);
    }

    public StringFilter subdomain() {
        if (subdomain == null) {
            setSubdomain(new StringFilter());
        }
        return subdomain;
    }

    public void setSubdomain(StringFilter subdomain) {
        this.subdomain = subdomain;
    }

    public StringFilter getCustomDomain() {
        return customDomain;
    }

    public Optional<StringFilter> optionalCustomDomain() {
        return Optional.ofNullable(customDomain);
    }

    public StringFilter customDomain() {
        if (customDomain == null) {
            setCustomDomain(new StringFilter());
        }
        return customDomain;
    }

    public void setCustomDomain(StringFilter customDomain) {
        this.customDomain = customDomain;
    }

    public BooleanFilter getDomainVerified() {
        return domainVerified;
    }

    public Optional<BooleanFilter> optionalDomainVerified() {
        return Optional.ofNullable(domainVerified);
    }

    public BooleanFilter domainVerified() {
        if (domainVerified == null) {
            setDomainVerified(new BooleanFilter());
        }
        return domainVerified;
    }

    public void setDomainVerified(BooleanFilter domainVerified) {
        this.domainVerified = domainVerified;
    }

    public StringFilter getDefaultLocale() {
        return defaultLocale;
    }

    public Optional<StringFilter> optionalDefaultLocale() {
        return Optional.ofNullable(defaultLocale);
    }

    public StringFilter defaultLocale() {
        if (defaultLocale == null) {
            setDefaultLocale(new StringFilter());
        }
        return defaultLocale;
    }

    public void setDefaultLocale(StringFilter defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public StringFilter getTimezone() {
        return timezone;
    }

    public Optional<StringFilter> optionalTimezone() {
        return Optional.ofNullable(timezone);
    }

    public StringFilter timezone() {
        if (timezone == null) {
            setTimezone(new StringFilter());
        }
        return timezone;
    }

    public void setTimezone(StringFilter timezone) {
        this.timezone = timezone;
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

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
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
        final TenantProfileCriteria that = (TenantProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subdomain, that.subdomain) &&
            Objects.equals(customDomain, that.customDomain) &&
            Objects.equals(domainVerified, that.domainVerified) &&
            Objects.equals(defaultLocale, that.defaultLocale) &&
            Objects.equals(timezone, that.timezone) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subdomain, customDomain, domainVerified, defaultLocale, timezone, tenantId, addressId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSubdomain().map(f -> "subdomain=" + f + ", ").orElse("") +
            optionalCustomDomain().map(f -> "customDomain=" + f + ", ").orElse("") +
            optionalDomainVerified().map(f -> "domainVerified=" + f + ", ").orElse("") +
            optionalDefaultLocale().map(f -> "defaultLocale=" + f + ", ").orElse("") +
            optionalTimezone().map(f -> "timezone=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
