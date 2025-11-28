package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.SubPipeline} entity. This class is used
 * in {@link com.mgs.web.rest.SubPipelineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sub-pipelines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nameSearch;

    private IntegerFilter sequenceOrder;

    private IntegerFilter probability;

    private BooleanFilter isClosingStage;

    private LongFilter tenantId;

    private LongFilter pipelineId;

    private Boolean distinct;

    public SubPipelineCriteria() {}

    public SubPipelineCriteria(SubPipelineCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nameSearch = other.optionalNameSearch().map(StringFilter::copy).orElse(null);
        this.sequenceOrder = other.optionalSequenceOrder().map(IntegerFilter::copy).orElse(null);
        this.probability = other.optionalProbability().map(IntegerFilter::copy).orElse(null);
        this.isClosingStage = other.optionalIsClosingStage().map(BooleanFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.pipelineId = other.optionalPipelineId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SubPipelineCriteria copy() {
        return new SubPipelineCriteria(this);
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

    public IntegerFilter getSequenceOrder() {
        return sequenceOrder;
    }

    public Optional<IntegerFilter> optionalSequenceOrder() {
        return Optional.ofNullable(sequenceOrder);
    }

    public IntegerFilter sequenceOrder() {
        if (sequenceOrder == null) {
            setSequenceOrder(new IntegerFilter());
        }
        return sequenceOrder;
    }

    public void setSequenceOrder(IntegerFilter sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public IntegerFilter getProbability() {
        return probability;
    }

    public Optional<IntegerFilter> optionalProbability() {
        return Optional.ofNullable(probability);
    }

    public IntegerFilter probability() {
        if (probability == null) {
            setProbability(new IntegerFilter());
        }
        return probability;
    }

    public void setProbability(IntegerFilter probability) {
        this.probability = probability;
    }

    public BooleanFilter getIsClosingStage() {
        return isClosingStage;
    }

    public Optional<BooleanFilter> optionalIsClosingStage() {
        return Optional.ofNullable(isClosingStage);
    }

    public BooleanFilter isClosingStage() {
        if (isClosingStage == null) {
            setIsClosingStage(new BooleanFilter());
        }
        return isClosingStage;
    }

    public void setIsClosingStage(BooleanFilter isClosingStage) {
        this.isClosingStage = isClosingStage;
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

    public LongFilter getPipelineId() {
        return pipelineId;
    }

    public Optional<LongFilter> optionalPipelineId() {
        return Optional.ofNullable(pipelineId);
    }

    public LongFilter pipelineId() {
        if (pipelineId == null) {
            setPipelineId(new LongFilter());
        }
        return pipelineId;
    }

    public void setPipelineId(LongFilter pipelineId) {
        this.pipelineId = pipelineId;
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
        final SubPipelineCriteria that = (SubPipelineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nameSearch, that.nameSearch) &&
            Objects.equals(sequenceOrder, that.sequenceOrder) &&
            Objects.equals(probability, that.probability) &&
            Objects.equals(isClosingStage, that.isClosingStage) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(pipelineId, that.pipelineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameSearch, sequenceOrder, probability, isClosingStage, tenantId, pipelineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNameSearch().map(f -> "nameSearch=" + f + ", ").orElse("") +
            optionalSequenceOrder().map(f -> "sequenceOrder=" + f + ", ").orElse("") +
            optionalProbability().map(f -> "probability=" + f + ", ").orElse("") +
            optionalIsClosingStage().map(f -> "isClosingStage=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalPipelineId().map(f -> "pipelineId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
