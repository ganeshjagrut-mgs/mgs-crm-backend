package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantEncryptionKey.
 */
@Entity
@Table(name = "tenant_encryption_key")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEncryptionKey extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "key_version", nullable = false)
    private Integer keyVersion;

    @NotNull
    @Column(name = "encrypted_data_key", nullable = false)
    private String encryptedDataKey;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "pin_salt")
    private String pinSalt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantEncryptionKey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKeyVersion() {
        return this.keyVersion;
    }

    public TenantEncryptionKey keyVersion(Integer keyVersion) {
        this.setKeyVersion(keyVersion);
        return this;
    }

    public void setKeyVersion(Integer keyVersion) {
        this.keyVersion = keyVersion;
    }

    public String getEncryptedDataKey() {
        return this.encryptedDataKey;
    }

    public TenantEncryptionKey encryptedDataKey(String encryptedDataKey) {
        this.setEncryptedDataKey(encryptedDataKey);
        return this;
    }

    public void setEncryptedDataKey(String encryptedDataKey) {
        this.encryptedDataKey = encryptedDataKey;
    }

    public String getPinHash() {
        return this.pinHash;
    }

    public TenantEncryptionKey pinHash(String pinHash) {
        this.setPinHash(pinHash);
        return this;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getPinSalt() {
        return this.pinSalt;
    }

    public TenantEncryptionKey pinSalt(String pinSalt) {
        this.setPinSalt(pinSalt);
        return this;
    }

    public void setPinSalt(String pinSalt) {
        this.pinSalt = pinSalt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TenantEncryptionKey isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantEncryptionKey tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantEncryptionKey)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantEncryptionKey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEncryptionKey{" +
            "id=" + getId() +
            ", keyVersion=" + getKeyVersion() +
            ", encryptedDataKey='" + getEncryptedDataKey() + "'" +
            ", pinHash='" + getPinHash() + "'" +
            ", pinSalt='" + getPinSalt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
