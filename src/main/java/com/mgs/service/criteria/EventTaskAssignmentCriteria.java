package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.EventTaskAssignment} entity. This class is used
 * in {@link com.mgs.web.rest.EventTaskAssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-task-assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTaskAssignmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter eventId;

    private LongFilter taskId;

    private LongFilter assignedToId;

    private Boolean distinct;

    public EventTaskAssignmentCriteria() {}

    public EventTaskAssignmentCriteria(EventTaskAssignmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.taskId = other.optionalTaskId().map(LongFilter::copy).orElse(null);
        this.assignedToId = other.optionalAssignedToId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventTaskAssignmentCriteria copy() {
        return new EventTaskAssignmentCriteria(this);
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

    public LongFilter getEventId() {
        return eventId;
    }

    public Optional<LongFilter> optionalEventId() {
        return Optional.ofNullable(eventId);
    }

    public LongFilter eventId() {
        if (eventId == null) {
            setEventId(new LongFilter());
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public Optional<LongFilter> optionalAssignedToId() {
        return Optional.ofNullable(assignedToId);
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            setAssignedToId(new LongFilter());
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
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
        final EventTaskAssignmentCriteria that = (EventTaskAssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, taskId, assignedToId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTaskAssignmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
            optionalTaskId().map(f -> "taskId=" + f + ", ").orElse("") +
            optionalAssignedToId().map(f -> "assignedToId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
