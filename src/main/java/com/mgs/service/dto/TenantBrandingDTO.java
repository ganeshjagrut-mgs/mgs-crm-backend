package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.TenantBranding} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantBrandingDTO implements Serializable {

    private Long id;

    private String logoPath;

    private String logoDarkPath;

    private String faviconPath;

    private String primaryColor;

    private String secondaryColor;

    private String accentColor;

    private String pdfHeaderLogoPath;

    private String pdfFooterText;

    private String pdfPrimaryColor;

    @NotNull
    private TenantDTO tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogoDarkPath() {
        return logoDarkPath;
    }

    public void setLogoDarkPath(String logoDarkPath) {
        this.logoDarkPath = logoDarkPath;
    }

    public String getFaviconPath() {
        return faviconPath;
    }

    public void setFaviconPath(String faviconPath) {
        this.faviconPath = faviconPath;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public String getPdfHeaderLogoPath() {
        return pdfHeaderLogoPath;
    }

    public void setPdfHeaderLogoPath(String pdfHeaderLogoPath) {
        this.pdfHeaderLogoPath = pdfHeaderLogoPath;
    }

    public String getPdfFooterText() {
        return pdfFooterText;
    }

    public void setPdfFooterText(String pdfFooterText) {
        this.pdfFooterText = pdfFooterText;
    }

    public String getPdfPrimaryColor() {
        return pdfPrimaryColor;
    }

    public void setPdfPrimaryColor(String pdfPrimaryColor) {
        this.pdfPrimaryColor = pdfPrimaryColor;
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
        if (!(o instanceof TenantBrandingDTO)) {
            return false;
        }

        TenantBrandingDTO tenantBrandingDTO = (TenantBrandingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantBrandingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantBrandingDTO{" +
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
            ", tenant=" + getTenant() +
            "}";
    }
}
