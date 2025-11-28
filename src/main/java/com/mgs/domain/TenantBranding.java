package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantBranding.
 */
@Entity
@Table(name = "tenant_branding")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantBranding extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "logo_dark_path")
    private String logoDarkPath;

    @Column(name = "favicon_path")
    private String faviconPath;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "secondary_color")
    private String secondaryColor;

    @Column(name = "accent_color")
    private String accentColor;

    @Column(name = "pdf_header_logo_path")
    private String pdfHeaderLogoPath;

    @Column(name = "pdf_footer_text")
    private String pdfFooterText;

    @Column(name = "pdf_primary_color")
    private String pdfPrimaryColor;

    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantBranding id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public TenantBranding logoPath(String logoPath) {
        this.setLogoPath(logoPath);
        return this;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogoDarkPath() {
        return this.logoDarkPath;
    }

    public TenantBranding logoDarkPath(String logoDarkPath) {
        this.setLogoDarkPath(logoDarkPath);
        return this;
    }

    public void setLogoDarkPath(String logoDarkPath) {
        this.logoDarkPath = logoDarkPath;
    }

    public String getFaviconPath() {
        return this.faviconPath;
    }

    public TenantBranding faviconPath(String faviconPath) {
        this.setFaviconPath(faviconPath);
        return this;
    }

    public void setFaviconPath(String faviconPath) {
        this.faviconPath = faviconPath;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public TenantBranding primaryColor(String primaryColor) {
        this.setPrimaryColor(primaryColor);
        return this;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return this.secondaryColor;
    }

    public TenantBranding secondaryColor(String secondaryColor) {
        this.setSecondaryColor(secondaryColor);
        return this;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getAccentColor() {
        return this.accentColor;
    }

    public TenantBranding accentColor(String accentColor) {
        this.setAccentColor(accentColor);
        return this;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public String getPdfHeaderLogoPath() {
        return this.pdfHeaderLogoPath;
    }

    public TenantBranding pdfHeaderLogoPath(String pdfHeaderLogoPath) {
        this.setPdfHeaderLogoPath(pdfHeaderLogoPath);
        return this;
    }

    public void setPdfHeaderLogoPath(String pdfHeaderLogoPath) {
        this.pdfHeaderLogoPath = pdfHeaderLogoPath;
    }

    public String getPdfFooterText() {
        return this.pdfFooterText;
    }

    public TenantBranding pdfFooterText(String pdfFooterText) {
        this.setPdfFooterText(pdfFooterText);
        return this;
    }

    public void setPdfFooterText(String pdfFooterText) {
        this.pdfFooterText = pdfFooterText;
    }

    public String getPdfPrimaryColor() {
        return this.pdfPrimaryColor;
    }

    public TenantBranding pdfPrimaryColor(String pdfPrimaryColor) {
        this.setPdfPrimaryColor(pdfPrimaryColor);
        return this;
    }

    public void setPdfPrimaryColor(String pdfPrimaryColor) {
        this.pdfPrimaryColor = pdfPrimaryColor;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantBranding tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantBranding)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantBranding) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantBranding{" +
            "id=" + getId() +
            ", logoPath='" + getLogoPath() + "'" +
            ", logoDarkPath='" + getLogoDarkPath() + "'" +
            ", faviconPath='" + getFaviconPath() + "'" +
            ", primaryColor='" + getPrimaryColor() + "'" +
            ", secondaryColor='" + getSecondaryColor() + "'" +
            ", accentColor='" + getAccentColor() + "'" +
            ", pdfHeaderLogoPath='" + getPdfHeaderLogoPath() + "'" +
            ", pdfFooterText='" + getPdfFooterText() + "'" +
            ", pdfPrimaryColor='" + getPdfPrimaryColor() + "'" +
            "}";
    }
}
