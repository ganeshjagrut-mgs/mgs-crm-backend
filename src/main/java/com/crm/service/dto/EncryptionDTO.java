package com.crm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.Encryption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EncryptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String pin;

    private TenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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
        if (!(o instanceof EncryptionDTO)) {
            return false;
        }

        EncryptionDTO encryptionDTO = (EncryptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, encryptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EncryptionDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", pin='" + getPin() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
