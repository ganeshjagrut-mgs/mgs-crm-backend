package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantProfile.
 */
@Entity
@Table(name = "tenant_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantProfile extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "subdomain", length = 100)
    private String subdomain;

    @Size(max = 255)
    @Column(name = "custom_domain", length = 255)
    private String customDomain;

    @NotNull
    @Column(name = "domain_verified", nullable = false)
    private Boolean domainVerified;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "legal_name", nullable = false)
    private String legalName;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "short_name")
    private String shortName;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "registration_number")
    private String registrationNumber;

    /**
     * PII – encrypted long string
     */
    @Lob
    @Column(name = "tax_id")
    private String taxId;

    /**
     * Contact PII – encrypted long string
     */
    @Lob
    @Column(name = "contact_person")
    private String contactPerson;

    @Lob
    @Column(name = "contact_email")
    private String contactEmail;

    @Lob
    @Column(name = "contact_phone")
    private String contactPhone;

    @Lob
    @Column(name = "website_url")
    private String websiteUrl;

    @Size(max = 10)
    @Column(name = "default_locale", length = 10)
    private String defaultLocale;

    @Size(max = 50)
    @Column(name = "timezone", length = 50)
    private String timezone;

    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Tenant tenant;

    @JsonIgnoreProperties(value = { "tenant", "tenantProfile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubdomain() {
        return this.subdomain;
    }

    public TenantProfile subdomain(String subdomain) {
        this.setSubdomain(subdomain);
        return this;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getCustomDomain() {
        return this.customDomain;
    }

    public TenantProfile customDomain(String customDomain) {
        this.setCustomDomain(customDomain);
        return this;
    }

    public void setCustomDomain(String customDomain) {
        this.customDomain = customDomain;
    }

    public Boolean getDomainVerified() {
        return this.domainVerified;
    }

    public TenantProfile domainVerified(Boolean domainVerified) {
        this.setDomainVerified(domainVerified);
        return this;
    }

    public void setDomainVerified(Boolean domainVerified) {
        this.domainVerified = domainVerified;
    }

    public String getLegalName() {
        return this.legalName;
    }

    public TenantProfile legalName(String legalName) {
        this.setLegalName(legalName);
        return this;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public TenantProfile shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public TenantProfile registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxId() {
        return this.taxId;
    }

    public TenantProfile taxId(String taxId) {
        this.setTaxId(taxId);
        return this;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public TenantProfile contactPerson(String contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public TenantProfile contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public TenantProfile contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public TenantProfile websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getDefaultLocale() {
        return this.defaultLocale;
    }

    public TenantProfile defaultLocale(String defaultLocale) {
        this.setDefaultLocale(defaultLocale);
        return this;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public TenantProfile timezone(String timezone) {
        this.setTimezone(timezone);
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantProfile tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public TenantProfile address(Address address) {
        this.setAddress(address);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantProfile{" +
            "id=" + getId() +
            ", subdomain='" + getSubdomain() + "'" +
            ", customDomain='" + getCustomDomain() + "'" +
            ", domainVerified='" + getDomainVerified() + "'" +
            ", legalName='" + getLegalName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", taxId='" + getTaxId() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", defaultLocale='" + getDefaultLocale() + "'" +
            ", timezone='" + getTimezone() + "'" +
            "}";
    }
}
