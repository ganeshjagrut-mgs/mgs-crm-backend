package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.TenantStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tenant.
 */
@Entity
@Table(name = "tenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tenant extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TenantStatus status;

    @JsonIgnoreProperties(value = { "tenant", "address" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tenant")
    private TenantProfile tenantProfile;

    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tenant")
    private TenantBranding tenantBranding;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tenant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Tenant code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TenantStatus getStatus() {
        return this.status;
    }

    public Tenant status(TenantStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TenantStatus status) {
        this.status = status;
    }

    public TenantProfile getTenantProfile() {
        return this.tenantProfile;
    }

    public void setTenantProfile(TenantProfile tenantProfile) {
        if (this.tenantProfile != null) {
            this.tenantProfile.setTenant(null);
        }
        if (tenantProfile != null) {
            tenantProfile.setTenant(this);
        }
        this.tenantProfile = tenantProfile;
    }

    public Tenant tenantProfile(TenantProfile tenantProfile) {
        this.setTenantProfile(tenantProfile);
        return this;
    }

    public TenantBranding getTenantBranding() {
        return this.tenantBranding;
    }

    public void setTenantBranding(TenantBranding tenantBranding) {
        if (this.tenantBranding != null) {
            this.tenantBranding.setTenant(null);
        }
        if (tenantBranding != null) {
            tenantBranding.setTenant(this);
        }
        this.tenantBranding = tenantBranding;
    }

    public Tenant tenantBranding(TenantBranding tenantBranding) {
        this.setTenantBranding(tenantBranding);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return getId() != null && getId().equals(((Tenant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
