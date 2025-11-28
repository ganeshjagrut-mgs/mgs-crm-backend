package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.TenantBranding} entity. This class is used
 * in {@link com.mgs.web.rest.TenantBrandingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-brandings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantBrandingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter logoPath;

    private StringFilter logoDarkPath;

    private StringFilter faviconPath;

    private StringFilter primaryColor;

    private StringFilter secondaryColor;

    private StringFilter accentColor;

    private StringFilter pdfHeaderLogoPath;

    private StringFilter pdfFooterText;

    private StringFilter pdfPrimaryColor;

    private LongFilter tenantId;

    private Boolean distinct;

    public TenantBrandingCriteria() {}

    public TenantBrandingCriteria(TenantBrandingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.logoPath = other.optionalLogoPath().map(StringFilter::copy).orElse(null);
        this.logoDarkPath = other.optionalLogoDarkPath().map(StringFilter::copy).orElse(null);
        this.faviconPath = other.optionalFaviconPath().map(StringFilter::copy).orElse(null);
        this.primaryColor = other.optionalPrimaryColor().map(StringFilter::copy).orElse(null);
        this.secondaryColor = other.optionalSecondaryColor().map(StringFilter::copy).orElse(null);
        this.accentColor = other.optionalAccentColor().map(StringFilter::copy).orElse(null);
        this.pdfHeaderLogoPath = other.optionalPdfHeaderLogoPath().map(StringFilter::copy).orElse(null);
        this.pdfFooterText = other.optionalPdfFooterText().map(StringFilter::copy).orElse(null);
        this.pdfPrimaryColor = other.optionalPdfPrimaryColor().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantBrandingCriteria copy() {
        return new TenantBrandingCriteria(this);
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

    public StringFilter getLogoPath() {
        return logoPath;
    }

    public Optional<StringFilter> optionalLogoPath() {
        return Optional.ofNullable(logoPath);
    }

    public StringFilter logoPath() {
        if (logoPath == null) {
            setLogoPath(new StringFilter());
        }
        return logoPath;
    }

    public void setLogoPath(StringFilter logoPath) {
        this.logoPath = logoPath;
    }

    public StringFilter getLogoDarkPath() {
        return logoDarkPath;
    }

    public Optional<StringFilter> optionalLogoDarkPath() {
        return Optional.ofNullable(logoDarkPath);
    }

    public StringFilter logoDarkPath() {
        if (logoDarkPath == null) {
            setLogoDarkPath(new StringFilter());
        }
        return logoDarkPath;
    }

    public void setLogoDarkPath(StringFilter logoDarkPath) {
        this.logoDarkPath = logoDarkPath;
    }

    public StringFilter getFaviconPath() {
        return faviconPath;
    }

    public Optional<StringFilter> optionalFaviconPath() {
        return Optional.ofNullable(faviconPath);
    }

    public StringFilter faviconPath() {
        if (faviconPath == null) {
            setFaviconPath(new StringFilter());
        }
        return faviconPath;
    }

    public void setFaviconPath(StringFilter faviconPath) {
        this.faviconPath = faviconPath;
    }

    public StringFilter getPrimaryColor() {
        return primaryColor;
    }

    public Optional<StringFilter> optionalPrimaryColor() {
        return Optional.ofNullable(primaryColor);
    }

    public StringFilter primaryColor() {
        if (primaryColor == null) {
            setPrimaryColor(new StringFilter());
        }
        return primaryColor;
    }

    public void setPrimaryColor(StringFilter primaryColor) {
        this.primaryColor = primaryColor;
    }

    public StringFilter getSecondaryColor() {
        return secondaryColor;
    }

    public Optional<StringFilter> optionalSecondaryColor() {
        return Optional.ofNullable(secondaryColor);
    }

    public StringFilter secondaryColor() {
        if (secondaryColor == null) {
            setSecondaryColor(new StringFilter());
        }
        return secondaryColor;
    }

    public void setSecondaryColor(StringFilter secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public StringFilter getAccentColor() {
        return accentColor;
    }

    public Optional<StringFilter> optionalAccentColor() {
        return Optional.ofNullable(accentColor);
    }

    public StringFilter accentColor() {
        if (accentColor == null) {
            setAccentColor(new StringFilter());
        }
        return accentColor;
    }

    public void setAccentColor(StringFilter accentColor) {
        this.accentColor = accentColor;
    }

    public StringFilter getPdfHeaderLogoPath() {
        return pdfHeaderLogoPath;
    }

    public Optional<StringFilter> optionalPdfHeaderLogoPath() {
        return Optional.ofNullable(pdfHeaderLogoPath);
    }

    public StringFilter pdfHeaderLogoPath() {
        if (pdfHeaderLogoPath == null) {
            setPdfHeaderLogoPath(new StringFilter());
        }
        return pdfHeaderLogoPath;
    }

    public void setPdfHeaderLogoPath(StringFilter pdfHeaderLogoPath) {
        this.pdfHeaderLogoPath = pdfHeaderLogoPath;
    }

    public StringFilter getPdfFooterText() {
        return pdfFooterText;
    }

    public Optional<StringFilter> optionalPdfFooterText() {
        return Optional.ofNullable(pdfFooterText);
    }

    public StringFilter pdfFooterText() {
        if (pdfFooterText == null) {
            setPdfFooterText(new StringFilter());
        }
        return pdfFooterText;
    }

    public void setPdfFooterText(StringFilter pdfFooterText) {
        this.pdfFooterText = pdfFooterText;
    }

    public StringFilter getPdfPrimaryColor() {
        return pdfPrimaryColor;
    }

    public Optional<StringFilter> optionalPdfPrimaryColor() {
        return Optional.ofNullable(pdfPrimaryColor);
    }

    public StringFilter pdfPrimaryColor() {
        if (pdfPrimaryColor == null) {
            setPdfPrimaryColor(new StringFilter());
        }
        return pdfPrimaryColor;
    }

    public void setPdfPrimaryColor(StringFilter pdfPrimaryColor) {
        this.pdfPrimaryColor = pdfPrimaryColor;
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
        final TenantBrandingCriteria that = (TenantBrandingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(logoPath, that.logoPath) &&
            Objects.equals(logoDarkPath, that.logoDarkPath) &&
            Objects.equals(faviconPath, that.faviconPath) &&
            Objects.equals(primaryColor, that.primaryColor) &&
            Objects.equals(secondaryColor, that.secondaryColor) &&
            Objects.equals(accentColor, that.accentColor) &&
            Objects.equals(pdfHeaderLogoPath, that.pdfHeaderLogoPath) &&
            Objects.equals(pdfFooterText, that.pdfFooterText) &&
            Objects.equals(pdfPrimaryColor, that.pdfPrimaryColor) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            logoPath,
            logoDarkPath,
            faviconPath,
            primaryColor,
            secondaryColor,
            accentColor,
            pdfHeaderLogoPath,
            pdfFooterText,
            pdfPrimaryColor,
            tenantId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantBrandingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLogoPath().map(f -> "logoPath=" + f + ", ").orElse("") +
            optionalLogoDarkPath().map(f -> "logoDarkPath=" + f + ", ").orElse("") +
            optionalFaviconPath().map(f -> "faviconPath=" + f + ", ").orElse("") +
            optionalPrimaryColor().map(f -> "primaryColor=" + f + ", ").orElse("") +
            optionalSecondaryColor().map(f -> "secondaryColor=" + f + ", ").orElse("") +
            optionalAccentColor().map(f -> "accentColor=" + f + ", ").orElse("") +
            optionalPdfHeaderLogoPath().map(f -> "pdfHeaderLogoPath=" + f + ", ").orElse("") +
            optionalPdfFooterText().map(f -> "pdfFooterText=" + f + ", ").orElse("") +
            optionalPdfPrimaryColor().map(f -> "pdfPrimaryColor=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
