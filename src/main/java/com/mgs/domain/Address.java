package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "line_1", nullable = false)
    private String line1;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "line_2")
    private String line2;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "postal_code")
    private String postalCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @JsonIgnoreProperties(value = { "tenant", "address" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private TenantProfile tenantProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressType getAddressType() {
        return this.addressType;
    }

    public Address addressType(AddressType addressType) {
        this.setAddressType(addressType);
        return this;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getLine1() {
        return this.line1;
    }

    public Address line1(String line1) {
        this.setLine1(line1);
        return this;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public Address line2(String line2) {
        this.setLine2(line2);
        return this;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Address tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public TenantProfile getTenantProfile() {
        return this.tenantProfile;
    }

    public void setTenantProfile(TenantProfile tenantProfile) {
        if (this.tenantProfile != null) {
            this.tenantProfile.setAddress(null);
        }
        if (tenantProfile != null) {
            tenantProfile.setAddress(this);
        }
        this.tenantProfile = tenantProfile;
    }

    public Address tenantProfile(TenantProfile tenantProfile) {
        this.setTenantProfile(tenantProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return getId() != null && getId().equals(((Address) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", addressType='" + getAddressType() + "'" +
            ", line1='" + getLine1() + "'" +
            ", line2='" + getLine2() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
