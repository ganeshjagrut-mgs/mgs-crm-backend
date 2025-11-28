package com.mgs.service.criteria;

import com.mgs.domain.enumeration.TaskPriority;
import com.mgs.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Task} entity. This class is used
 * in {@link com.mgs.web.rest.TaskResource} to receive all the possible filtering options from
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
     * Class for filtering TaskStatus
     */
    public static class TaskStatusFilter extends Filter<TaskStatus> {

        public TaskStatusFilter() {}

        public TaskStatusFilter(TaskStatusFilter filter) {
            super(filter);
        }

        @Override
        public TaskStatusFilter copy() {
            return new TaskStatusFilter(this);
        }
    }

    /**
     * Class for filtering TaskPriority
     */
    public static class TaskPriorityFilter extends Filter<TaskPriority> {

        public TaskPriorityFilter() {}

        public TaskPriorityFilter(TaskPriorityFilter filter) {
            super(filter);
        }

        @Override
        public TaskPriorityFilter copy() {
            return new TaskPriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter relatedType;

    private TaskStatusFilter status;

    private TaskPriorityFilter priority;

    private InstantFilter dueAt;

    private InstantFilter completedAt;

    private LongFilter tenantId;

    private LongFilter taskTypeId;

    private LongFilter assignedUserId;

    private LongFilter createdByUserId;

    private Boolean distinct;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.relatedType = other.optionalRelatedType().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TaskStatusFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(TaskPriorityFilter::copy).orElse(null);
        this.dueAt = other.optionalDueAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.taskTypeId = other.optionalTaskTypeId().map(LongFilter::copy).orElse(null);
        this.assignedUserId = other.optionalAssignedUserId().map(LongFilter::copy).orElse(null);
        this.createdByUserId = other.optionalCreatedByUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getRelatedType() {
        return relatedType;
    }

    public Optional<StringFilter> optionalRelatedType() {
        return Optional.ofNullable(relatedType);
    }

    public StringFilter relatedType() {
        if (relatedType == null) {
            setRelatedType(new StringFilter());
        }
        return relatedType;
    }

    public void setRelatedType(StringFilter relatedType) {
        this.relatedType = relatedType;
    }

    public TaskStatusFilter getStatus() {
        return status;
    }

    public Optional<TaskStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TaskStatusFilter status() {
        if (status == null) {
            setStatus(new TaskStatusFilter());
        }
        return status;
    }

    public void setStatus(TaskStatusFilter status) {
        this.status = status;
    }

    public TaskPriorityFilter getPriority() {
        return priority;
    }

    public Optional<TaskPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public TaskPriorityFilter priority() {
        if (priority == null) {
            setPriority(new TaskPriorityFilter());
        }
        return priority;
    }

    public void setPriority(TaskPriorityFilter priority) {
        this.priority = priority;
    }

    public InstantFilter getDueAt() {
        return dueAt;
    }

    public Optional<InstantFilter> optionalDueAt() {
        return Optional.ofNullable(dueAt);
    }

    public InstantFilter dueAt() {
        if (dueAt == null) {
            setDueAt(new InstantFilter());
        }
        return dueAt;
    }

    public void setDueAt(InstantFilter dueAt) {
        this.dueAt = dueAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
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

    public LongFilter getTaskTypeId() {
        return taskTypeId;
    }

    public Optional<LongFilter> optionalTaskTypeId() {
        return Optional.ofNullable(taskTypeId);
    }

    public LongFilter taskTypeId() {
        if (taskTypeId == null) {
            setTaskTypeId(new LongFilter());
        }
        return taskTypeId;
    }

    public void setTaskTypeId(LongFilter taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public LongFilter getAssignedUserId() {
        return assignedUserId;
    }

    public Optional<LongFilter> optionalAssignedUserId() {
        return Optional.ofNullable(assignedUserId);
    }

    public LongFilter assignedUserId() {
        if (assignedUserId == null) {
            setAssignedUserId(new LongFilter());
        }
        return assignedUserId;
    }

    public void setAssignedUserId(LongFilter assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public LongFilter getCreatedByUserId() {
        return createdByUserId;
    }

    public Optional<LongFilter> optionalCreatedByUserId() {
        return Optional.ofNullable(createdByUserId);
    }

    public LongFilter createdByUserId() {
        if (createdByUserId == null) {
            setCreatedByUserId(new LongFilter());
        }
        return createdByUserId;
    }

    public void setCreatedByUserId(LongFilter createdByUserId) {
        this.createdByUserId = createdByUserId;
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
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(relatedType, that.relatedType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(dueAt, that.dueAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(taskTypeId, that.taskTypeId) &&
            Objects.equals(assignedUserId, that.assignedUserId) &&
            Objects.equals(createdByUserId, that.createdByUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            relatedType,
            status,
            priority,
            dueAt,
            completedAt,
            tenantId,
            taskTypeId,
            assignedUserId,
            createdByUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalRelatedType().map(f -> "relatedType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalDueAt().map(f -> "dueAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTaskTypeId().map(f -> "taskTypeId=" + f + ", ").orElse("") +
            optionalAssignedUserId().map(f -> "assignedUserId=" + f + ", ").orElse("") +
            optionalCreatedByUserId().map(f -> "createdByUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
