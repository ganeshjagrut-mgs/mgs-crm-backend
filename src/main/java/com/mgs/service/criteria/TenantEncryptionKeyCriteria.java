package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.TenantEncryptionKey} entity. This class is used
 * in {@link com.mgs.web.rest.TenantEncryptionKeyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-encryption-keys?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEncryptionKeyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter keyVersion;

    private StringFilter encryptedDataKey;

    private StringFilter pinHash;

    private StringFilter pinSalt;

    private BooleanFilter isActive;

    private LongFilter tenantId;

    private Boolean distinct;

    public TenantEncryptionKeyCriteria() {}

    public TenantEncryptionKeyCriteria(TenantEncryptionKeyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.keyVersion = other.optionalKeyVersion().map(IntegerFilter::copy).orElse(null);
        this.encryptedDataKey = other.optionalEncryptedDataKey().map(StringFilter::copy).orElse(null);
        this.pinHash = other.optionalPinHash().map(StringFilter::copy).orElse(null);
        this.pinSalt = other.optionalPinSalt().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantEncryptionKeyCriteria copy() {
        return new TenantEncryptionKeyCriteria(this);
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

    public IntegerFilter getKeyVersion() {
        return keyVersion;
    }

    public Optional<IntegerFilter> optionalKeyVersion() {
        return Optional.ofNullable(keyVersion);
    }

    public IntegerFilter keyVersion() {
        if (keyVersion == null) {
            setKeyVersion(new IntegerFilter());
        }
        return keyVersion;
    }

    public void setKeyVersion(IntegerFilter keyVersion) {
        this.keyVersion = keyVersion;
    }

    public StringFilter getEncryptedDataKey() {
        return encryptedDataKey;
    }

    public Optional<StringFilter> optionalEncryptedDataKey() {
        return Optional.ofNullable(encryptedDataKey);
    }

    public StringFilter encryptedDataKey() {
        if (encryptedDataKey == null) {
            setEncryptedDataKey(new StringFilter());
        }
        return encryptedDataKey;
    }

    public void setEncryptedDataKey(StringFilter encryptedDataKey) {
        this.encryptedDataKey = encryptedDataKey;
    }

    public StringFilter getPinHash() {
        return pinHash;
    }

    public Optional<StringFilter> optionalPinHash() {
        return Optional.ofNullable(pinHash);
    }

    public StringFilter pinHash() {
        if (pinHash == null) {
            setPinHash(new StringFilter());
        }
        return pinHash;
    }

    public void setPinHash(StringFilter pinHash) {
        this.pinHash = pinHash;
    }

    public StringFilter getPinSalt() {
        return pinSalt;
    }

    public Optional<StringFilter> optionalPinSalt() {
        return Optional.ofNullable(pinSalt);
    }

    public StringFilter pinSalt() {
        if (pinSalt == null) {
            setPinSalt(new StringFilter());
        }
        return pinSalt;
    }

    public void setPinSalt(StringFilter pinSalt) {
        this.pinSalt = pinSalt;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final TenantEncryptionKeyCriteria that = (TenantEncryptionKeyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(keyVersion, that.keyVersion) &&
            Objects.equals(encryptedDataKey, that.encryptedDataKey) &&
            Objects.equals(pinHash, that.pinHash) &&
            Objects.equals(pinSalt, that.pinSalt) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyVersion, encryptedDataKey, pinHash, pinSalt, isActive, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEncryptionKeyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKeyVersion().map(f -> "keyVersion=" + f + ", ").orElse("") +
            optionalEncryptedDataKey().map(f -> "encryptedDataKey=" + f + ", ").orElse("") +
            optionalPinHash().map(f -> "pinHash=" + f + ", ").orElse("") +
            optionalPinSalt().map(f -> "pinSalt=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
