package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.TenantEncryptionKey} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEncryptionKeyDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer keyVersion;

    @NotNull
    private String encryptedDataKey;

    private String pinHash;

    private String pinSalt;

    @NotNull
    private Boolean isActive;

    @NotNull
    private TenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(Integer keyVersion) {
        this.keyVersion = keyVersion;
    }

    public String getEncryptedDataKey() {
        return encryptedDataKey;
    }

    public void setEncryptedDataKey(String encryptedDataKey) {
        this.encryptedDataKey = encryptedDataKey;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getPinSalt() {
        return pinSalt;
    }

    public void setPinSalt(String pinSalt) {
        this.pinSalt = pinSalt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantEncryptionKeyDTO)) {
            return false;
        }

        TenantEncryptionKeyDTO tenantEncryptionKeyDTO = (TenantEncryptionKeyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantEncryptionKeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEncryptionKeyDTO{" +
            "id=" + getId() +
            ", keyVersion=" + getKeyVersion() +
            ", encryptedDataKey='" + getEncryptedDataKey() + "'" +
            ", pinHash='" + getPinHash() + "'" +
            ", pinSalt='" + getPinSalt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
