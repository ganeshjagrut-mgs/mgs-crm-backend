package com.crm.service.criteria;

import com.crm.domain.enumeration.Status;
import com.crm.domain.enumeration.TaskType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Task} entity. This class is used
 * in {@link com.crm.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TaskType
     */
    public static class TaskTypeFilter extends Filter<TaskType> {

        public TaskTypeFilter() {}

        public TaskTypeFilter(TaskTypeFilter filter) {
            super(filter);
        }

        @Override
        public TaskTypeFilter copy() {
            return new TaskTypeFilter(this);
        }
    }

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TaskTypeFilter taskType;

    private LocalDateFilter dueDate;

    private StringFilter taskName;

    private StatusFilter status;

    private LocalDateFilter taskCompletionDate;

    private UUIDFilter correlationId;

    private LongFilter taskOwnerId;

    private LongFilter customerId;

    private LongFilter relatedToId;

    private LongFilter pipelineId;

    private Boolean distinct;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskType = other.taskType == null ? null : other.taskType.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.taskName = other.taskName == null ? null : other.taskName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.taskCompletionDate = other.taskCompletionDate == null ? null : other.taskCompletionDate.copy();
        this.correlationId = other.correlationId == null ? null : other.correlationId.copy();
        this.taskOwnerId = other.taskOwnerId == null ? null : other.taskOwnerId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.relatedToId = other.relatedToId == null ? null : other.relatedToId.copy();
        this.pipelineId = other.pipelineId == null ? null : other.pipelineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public TaskTypeFilter getTaskType() {
        return taskType;
    }

    public TaskTypeFilter taskType() {
        if (taskType == null) {
            taskType = new TaskTypeFilter();
        }
        return taskType;
    }

    public void setTaskType(TaskTypeFilter taskType) {
        this.taskType = taskType;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public LocalDateFilter dueDate() {
        if (dueDate == null) {
            dueDate = new LocalDateFilter();
        }
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public StringFilter getTaskName() {
        return taskName;
    }

    public StringFilter taskName() {
        if (taskName == null) {
            taskName = new StringFilter();
        }
        return taskName;
    }

    public void setTaskName(StringFilter taskName) {
        this.taskName = taskName;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getTaskCompletionDate() {
        return taskCompletionDate;
    }

    public LocalDateFilter taskCompletionDate() {
        if (taskCompletionDate == null) {
            taskCompletionDate = new LocalDateFilter();
        }
        return taskCompletionDate;
    }

    public void setTaskCompletionDate(LocalDateFilter taskCompletionDate) {
        this.taskCompletionDate = taskCompletionDate;
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

    public LongFilter getTaskOwnerId() {
        return taskOwnerId;
    }

    public LongFilter taskOwnerId() {
        if (taskOwnerId == null) {
            taskOwnerId = new LongFilter();
        }
        return taskOwnerId;
    }

    public void setTaskOwnerId(LongFilter taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
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

    public LongFilter getRelatedToId() {
        return relatedToId;
    }

    public LongFilter relatedToId() {
        if (relatedToId == null) {
            relatedToId = new LongFilter();
        }
        return relatedToId;
    }

    public void setRelatedToId(LongFilter relatedToId) {
        this.relatedToId = relatedToId;
    }

    public LongFilter getPipelineId() {
        return pipelineId;
    }

    public LongFilter pipelineId() {
        if (pipelineId == null) {
            pipelineId = new LongFilter();
        }
        return pipelineId;
    }

    public void setPipelineId(LongFilter pipelineId) {
        this.pipelineId = pipelineId;
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
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(taskType, that.taskType) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(taskName, that.taskName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(taskCompletionDate, that.taskCompletionDate) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(taskOwnerId, that.taskOwnerId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(relatedToId, that.relatedToId) &&
            Objects.equals(pipelineId, that.pipelineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            taskType,
            dueDate,
            taskName,
            status,
            taskCompletionDate,
            correlationId,
            taskOwnerId,
            customerId,
            relatedToId,
            pipelineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (taskType != null ? "taskType=" + taskType + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (taskName != null ? "taskName=" + taskName + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (taskCompletionDate != null ? "taskCompletionDate=" + taskCompletionDate + ", " : "") +
            (correlationId != null ? "correlationId=" + correlationId + ", " : "") +
            (taskOwnerId != null ? "taskOwnerId=" + taskOwnerId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (relatedToId != null ? "relatedToId=" + relatedToId + ", " : "") +
            (pipelineId != null ? "pipelineId=" + pipelineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
