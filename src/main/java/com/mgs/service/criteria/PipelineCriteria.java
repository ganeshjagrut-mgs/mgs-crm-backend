package com.mgs.service.criteria;

import com.mgs.domain.enumeration.PipelineModule;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Pipeline} entity. This class is used
 * in {@link com.mgs.web.rest.PipelineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pipelines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PipelineModule
     */
    public static class PipelineModuleFilter extends Filter<PipelineModule> {

        public PipelineModuleFilter() {}

        public PipelineModuleFilter(PipelineModuleFilter filter) {
            super(filter);
        }

        @Override
        public PipelineModuleFilter copy() {
            return new PipelineModuleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nameSearch;

    private PipelineModuleFilter module;

    private BooleanFilter isDefault;

    private LongFilter tenantId;

    private Boolean distinct;

    public PipelineCriteria() {}

    public PipelineCriteria(PipelineCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nameSearch = other.optionalNameSearch().map(StringFilter::copy).orElse(null);
        this.module = other.optionalModule().map(PipelineModuleFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PipelineCriteria copy() {
        return new PipelineCriteria(this);
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

    public StringFilter getNameSearch() {
        return nameSearch;
    }

    public Optional<StringFilter> optionalNameSearch() {
        return Optional.ofNullable(nameSearch);
    }

    public StringFilter nameSearch() {
        if (nameSearch == null) {
            setNameSearch(new StringFilter());
        }
        return nameSearch;
    }

    public void setNameSearch(StringFilter nameSearch) {
        this.nameSearch = nameSearch;
    }

    public PipelineModuleFilter getModule() {
        return module;
    }

    public Optional<PipelineModuleFilter> optionalModule() {
        return Optional.ofNullable(module);
    }

    public PipelineModuleFilter module() {
        if (module == null) {
            setModule(new PipelineModuleFilter());
        }
        return module;
    }

    public void setModule(PipelineModuleFilter module) {
        this.module = module;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
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
        final PipelineCriteria that = (PipelineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nameSearch, that.nameSearch) &&
            Objects.equals(module, that.module) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameSearch, module, isDefault, tenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNameSearch().map(f -> "nameSearch=" + f + ", ").orElse("") +
            optionalModule().map(f -> "module=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
