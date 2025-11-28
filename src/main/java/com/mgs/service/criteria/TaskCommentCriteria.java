package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.TaskComment} entity. This class is used
 * in {@link com.mgs.web.rest.TaskCommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskCommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comment;

    private LongFilter taskId;

    private LongFilter createdByUserId;

    private Boolean distinct;

    public TaskCommentCriteria() {}

    public TaskCommentCriteria(TaskCommentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.comment = other.optionalComment().map(StringFilter::copy).orElse(null);
        this.taskId = other.optionalTaskId().map(LongFilter::copy).orElse(null);
        this.createdByUserId = other.optionalCreatedByUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TaskCommentCriteria copy() {
        return new TaskCommentCriteria(this);
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

    public StringFilter getComment() {
        return comment;
    }

    public Optional<StringFilter> optionalComment() {
        return Optional.ofNullable(comment);
    }

    public StringFilter comment() {
        if (comment == null) {
            setComment(new StringFilter());
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public Optional<LongFilter> optionalTaskId() {
        return Optional.ofNullable(taskId);
    }

    public LongFilter taskId() {
        if (taskId == null) {
            setTaskId(new LongFilter());
        }
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
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
        final TaskCommentCriteria that = (TaskCommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(createdByUserId, that.createdByUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, taskId, createdByUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCommentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalComment().map(f -> "comment=" + f + ", ").orElse("") +
            optionalTaskId().map(f -> "taskId=" + f + ", ").orElse("") +
            optionalCreatedByUserId().map(f -> "createdByUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
