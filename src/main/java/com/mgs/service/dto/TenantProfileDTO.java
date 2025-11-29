package com.mgs.service.dto;

import com.mgs.util.EncryptedField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.TenantProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantProfileDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String subdomain;

    @Size(max = 255)
    private String customDomain;

    @NotNull
    private Boolean domainVerified;

    @Schema(description = "PII – encrypted long string", requiredMode = Schema.RequiredMode.REQUIRED)
    @EncryptedField
    private String legalName;

    @Schema(description = "PII – encrypted long string")
    @EncryptedField
    private String shortName;

    @Schema(description = "PII – encrypted long string")
    @EncryptedField
    private String registrationNumber;

    @Schema(description = "PII – encrypted long string")
    @EncryptedField
    private String taxId;

    @Schema(description = "Contact PII – encrypted long string")
    @EncryptedField
    private String contactPerson;

    private String contactEmail;

    private String contactPhone;

    private String websiteUrl;

    @Size(max = 10)
    private String defaultLocale;

    @Size(max = 50)
    private String timezone;

    @NotNull
    private TenantDTO tenant;

    private AddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(String customDomain) {
        this.customDomain = customDomain;
    }

    public Boolean getDomainVerified() {
        return domainVerified;
    }

    public void setDomainVerified(Boolean domainVerified) {
        this.domainVerified = domainVerified;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantProfileDTO)) {
            return false;
        }

        TenantProfileDTO tenantProfileDTO = (TenantProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantProfileDTO{" +
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
            ", tenant=" + getTenant() +
            ", address=" + getAddress() +
            "}";
    }
}
