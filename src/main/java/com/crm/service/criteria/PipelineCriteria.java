package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Pipeline} entity. This class is used
 * in {@link com.crm.web.rest.PipelineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pipelines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pipelineName;

    private BigDecimalFilter totalAmount;

    private IntegerFilter noOfSamples;

    private UUIDFilter correlationId;

    private LongFilter pipelineTagsId;

    private LongFilter tasksId;

    private LongFilter pipelineOwnerId;

    private LongFilter customerId;

    private LongFilter stageOfPipelineId;

    private LongFilter subPipelineId;

    private Boolean distinct;

    public PipelineCriteria() {}

    public PipelineCriteria(PipelineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pipelineName = other.pipelineName == null ? null : other.pipelineName.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.noOfSamples = other.noOfSamples == null ? null : other.noOfSamples.copy();
        this.correlationId = other.correlationId == null ? null : other.correlationId.copy();
        this.pipelineTagsId = other.pipelineTagsId == null ? null : other.pipelineTagsId.copy();
        this.tasksId = other.tasksId == null ? null : other.tasksId.copy();
        this.pipelineOwnerId = other.pipelineOwnerId == null ? null : other.pipelineOwnerId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.stageOfPipelineId = other.stageOfPipelineId == null ? null : other.stageOfPipelineId.copy();
        this.subPipelineId = other.subPipelineId == null ? null : other.subPipelineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PipelineCriteria copy() {
        return new PipelineCriteria(this);
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

    public StringFilter getPipelineName() {
        return pipelineName;
    }

    public StringFilter pipelineName() {
        if (pipelineName == null) {
            pipelineName = new StringFilter();
        }
        return pipelineName;
    }

    public void setPipelineName(StringFilter pipelineName) {
        this.pipelineName = pipelineName;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimalFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public IntegerFilter getNoOfSamples() {
        return noOfSamples;
    }

    public IntegerFilter noOfSamples() {
        if (noOfSamples == null) {
            noOfSamples = new IntegerFilter();
        }
        return noOfSamples;
    }

    public void setNoOfSamples(IntegerFilter noOfSamples) {
        this.noOfSamples = noOfSamples;
    }

    public UUIDFilter getCorrelationId() {
        return correlationId;
    }

    public UUIDFilter correlationId() {
        if (correlationId == null) {
            correlationId = new UUIDFilter();
        }
        return correlationId;
    }

    public void setCorrelationId(UUIDFilter correlationId) {
        this.correlationId = correlationId;
    }

    public LongFilter getPipelineTagsId() {
        return pipelineTagsId;
    }

    public LongFilter pipelineTagsId() {
        if (pipelineTagsId == null) {
            pipelineTagsId = new LongFilter();
        }
        return pipelineTagsId;
    }

    public void setPipelineTagsId(LongFilter pipelineTagsId) {
        this.pipelineTagsId = pipelineTagsId;
    }

    public LongFilter getTasksId() {
        return tasksId;
    }

    public LongFilter tasksId() {
        if (tasksId == null) {
            tasksId = new LongFilter();
        }
        return tasksId;
    }

    public void setTasksId(LongFilter tasksId) {
        this.tasksId = tasksId;
    }

    public LongFilter getPipelineOwnerId() {
        return pipelineOwnerId;
    }

    public LongFilter pipelineOwnerId() {
        if (pipelineOwnerId == null) {
            pipelineOwnerId = new LongFilter();
        }
        return pipelineOwnerId;
    }

    public void setPipelineOwnerId(LongFilter pipelineOwnerId) {
        this.pipelineOwnerId = pipelineOwnerId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getStageOfPipelineId() {
        return stageOfPipelineId;
    }

    public LongFilter stageOfPipelineId() {
        if (stageOfPipelineId == null) {
            stageOfPipelineId = new LongFilter();
        }
        return stageOfPipelineId;
    }

    public void setStageOfPipelineId(LongFilter stageOfPipelineId) {
        this.stageOfPipelineId = stageOfPipelineId;
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
        final PipelineCriteria that = (PipelineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pipelineName, that.pipelineName) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(noOfSamples, that.noOfSamples) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(pipelineTagsId, that.pipelineTagsId) &&
            Objects.equals(tasksId, that.tasksId) &&
            Objects.equals(pipelineOwnerId, that.pipelineOwnerId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(stageOfPipelineId, that.stageOfPipelineId) &&
            Objects.equals(subPipelineId, that.subPipelineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pipelineName,
            totalAmount,
            noOfSamples,
            correlationId,
            pipelineTagsId,
            tasksId,
            pipelineOwnerId,
            customerId,
            stageOfPipelineId,
            subPipelineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pipelineName != null ? "pipelineName=" + pipelineName + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (noOfSamples != null ? "noOfSamples=" + noOfSamples + ", " : "") +
            (correlationId != null ? "correlationId=" + correlationId + ", " : "") +
            (pipelineTagsId != null ? "pipelineTagsId=" + pipelineTagsId + ", " : "") +
            (tasksId != null ? "tasksId=" + tasksId + ", " : "") +
            (pipelineOwnerId != null ? "pipelineOwnerId=" + pipelineOwnerId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (stageOfPipelineId != null ? "stageOfPipelineId=" + stageOfPipelineId + ", " : "") +
            (subPipelineId != null ? "subPipelineId=" + subPipelineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
