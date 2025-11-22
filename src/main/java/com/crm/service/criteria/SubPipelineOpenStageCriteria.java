package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.SubPipelineOpenStage} entity. This class is used
 * in {@link com.crm.web.rest.SubPipelineOpenStageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sub-pipeline-open-stages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineOpenStageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter index;

    private LongFilter stageId;

    private LongFilter subPipelineId;

    private Boolean distinct;

    public SubPipelineOpenStageCriteria() {}

    public SubPipelineOpenStageCriteria(SubPipelineOpenStageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.index = other.index == null ? null : other.index.copy();
        this.stageId = other.stageId == null ? null : other.stageId.copy();
        this.subPipelineId = other.subPipelineId == null ? null : other.subPipelineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubPipelineOpenStageCriteria copy() {
        return new SubPipelineOpenStageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getIndex() {
        return index;
    }

    public IntegerFilter index() {
        if (index == null) {
            index = new IntegerFilter();
        }
        return index;
    }

    public void setIndex(IntegerFilter index) {
        this.index = index;
    }

    public LongFilter getStageId() {
        return stageId;
    }

    public LongFilter stageId() {
        if (stageId == null) {
            stageId = new LongFilter();
        }
        return stageId;
    }

    public void setStageId(LongFilter stageId) {
        this.stageId = stageId;
    }

    public LongFilter getSubPipelineId() {
        return subPipelineId;
    }

    public LongFilter subPipelineId() {
        if (subPipelineId == null) {
            subPipelineId = new LongFilter();
        }
        return subPipelineId;
    }

    public void setSubPipelineId(LongFilter subPipelineId) {
        this.subPipelineId = subPipelineId;
    }

    public Boolean getDistinct() {
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
        final SubPipelineOpenStageCriteria that = (SubPipelineOpenStageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(index, that.index) &&
            Objects.equals(stageId, that.stageId) &&
            Objects.equals(subPipelineId, that.subPipelineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, stageId, subPipelineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineOpenStageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (index != null ? "index=" + index + ", " : "") +
            (stageId != null ? "stageId=" + stageId + ", " : "") +
            (subPipelineId != null ? "subPipelineId=" + subPipelineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
