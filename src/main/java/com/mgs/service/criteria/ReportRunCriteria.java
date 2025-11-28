package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.ReportRun} entity. This class is used
 * in {@link com.mgs.web.rest.ReportRunResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-runs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportRunCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter filterJson;

    private StringFilter format;

    private StringFilter filePath;

    private LongFilter tenantId;

    private LongFilter templateId;

    private LongFilter generatedByUserId;

    private Boolean distinct;

    public ReportRunCriteria() {}

    public ReportRunCriteria(ReportRunCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.filterJson = other.optionalFilterJson().map(StringFilter::copy).orElse(null);
        this.format = other.optionalFormat().map(StringFilter::copy).orElse(null);
        this.filePath = other.optionalFilePath().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.templateId = other.optionalTemplateId().map(LongFilter::copy).orElse(null);
        this.generatedByUserId = other.optionalGeneratedByUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReportRunCriteria copy() {
        return new ReportRunCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFilterJson() {
        return filterJson;
    }

    public Optional<StringFilter> optionalFilterJson() {
        return Optional.ofNullable(filterJson);
    }

    public StringFilter filterJson() {
        if (filterJson == null) {
            setFilterJson(new StringFilter());
        }
        return filterJson;
    }

    public void setFilterJson(StringFilter filterJson) {
        this.filterJson = filterJson;
    }

    public StringFilter getFormat() {
        return format;
    }

    public Optional<StringFilter> optionalFormat() {
        return Optional.ofNullable(format);
    }

    public StringFilter format() {
        if (format == null) {
            setFormat(new StringFilter());
        }
        return format;
    }

    public void setFormat(StringFilter format) {
        this.format = format;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public Optional<StringFilter> optionalFilePath() {
        return Optional.ofNullable(filePath);
    }

    public StringFilter filePath() {
        if (filePath == null) {
            setFilePath(new StringFilter());
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
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

    public LongFilter getTemplateId() {
        return templateId;
    }

    public Optional<LongFilter> optionalTemplateId() {
        return Optional.ofNullable(templateId);
    }

    public LongFilter templateId() {
        if (templateId == null) {
            setTemplateId(new LongFilter());
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public LongFilter getGeneratedByUserId() {
        return generatedByUserId;
    }

    public Optional<LongFilter> optionalGeneratedByUserId() {
        return Optional.ofNullable(generatedByUserId);
    }

    public LongFilter generatedByUserId() {
        if (generatedByUserId == null) {
            setGeneratedByUserId(new LongFilter());
        }
        return generatedByUserId;
    }

    public void setGeneratedByUserId(LongFilter generatedByUserId) {
        this.generatedByUserId = generatedByUserId;
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
        final ReportRunCriteria that = (ReportRunCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(filterJson, that.filterJson) &&
            Objects.equals(format, that.format) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(generatedByUserId, that.generatedByUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filterJson, format, filePath, tenantId, templateId, generatedByUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRunCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalFilterJson().map(f -> "filterJson=" + f + ", ").orElse("") +
            optionalFormat().map(f -> "format=" + f + ", ").orElse("") +
            optionalFilePath().map(f -> "filePath=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTemplateId().map(f -> "templateId=" + f + ", ").orElse("") +
            optionalGeneratedByUserId().map(f -> "generatedByUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
