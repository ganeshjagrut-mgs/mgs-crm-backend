package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Tenant} entity. This class is used
 * in {@link com.crm.web.rest.TenantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter contactPerson;

    private StringFilter logo;

    private StringFilter website;

    private StringFilter registrationNumber;

    private IntegerFilter subId;

    private LongFilter addressesId;

    private LongFilter usersId;

    private LongFilter encryptionId;

    private Boolean distinct;

    public TenantCriteria() {}

    public TenantCriteria(TenantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.contactPerson = other.contactPerson == null ? null : other.contactPerson.copy();
        this.logo = other.logo == null ? null : other.logo.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.registrationNumber = other.registrationNumber == null ? null : other.registrationNumber.copy();
        this.subId = other.subId == null ? null : other.subId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.encryptionId = other.encryptionId == null ? null : other.encryptionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TenantCriteria copy() {
        return new TenantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getContactPerson() {
        return contactPerson;
    }

    public StringFilter contactPerson() {
        if (contactPerson == null) {
            contactPerson = new StringFilter();
        }
        return contactPerson;
    }

    public void setContactPerson(StringFilter contactPerson) {
        this.contactPerson = contactPerson;
    }

    public StringFilter getLogo() {
        return logo;
    }

    public StringFilter logo() {
        if (logo == null) {
            logo = new StringFilter();
        }
        return logo;
    }

    public void setLogo(StringFilter logo) {
        this.logo = logo;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            registrationNumber = new StringFilter();
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public IntegerFilter getSubId() {
        return subId;
    }

    public IntegerFilter subId() {
        if (subId == null) {
            subId = new IntegerFilter();
        }
        return subId;
    }

    public void setSubId(IntegerFilter subId) {
        this.subId = subId;
    }

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public LongFilter addressesId() {
        if (addressesId == null) {
            addressesId = new LongFilter();
        }
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public LongFilter usersId() {
        if (usersId == null) {
            usersId = new LongFilter();
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getEncryptionId() {
        return encryptionId;
    }

    public LongFilter encryptionId() {
        if (encryptionId == null) {
            encryptionId = new LongFilter();
        }
        return encryptionId;
    }

    public void setEncryptionId(LongFilter encryptionId) {
        this.encryptionId = encryptionId;
    }

    public Boolean getDistinct() {
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
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(contactPerson, that.contactPerson) &&
            Objects.equals(logo, that.logo) &&
            Objects.equals(website, that.website) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(subId, that.subId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(encryptionId, that.encryptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            companyName,
            contactPerson,
            logo,
            website,
            registrationNumber,
            subId,
            addressesId,
            usersId,
            encryptionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (contactPerson != null ? "contactPerson=" + contactPerson + ", " : "") +
            (logo != null ? "logo=" + logo + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (registrationNumber != null ? "registrationNumber=" + registrationNumber + ", " : "") +
            (subId != null ? "subId=" + subId + ", " : "") +
            (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
            (usersId != null ? "usersId=" + usersId + ", " : "") +
            (encryptionId != null ? "encryptionId=" + encryptionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
