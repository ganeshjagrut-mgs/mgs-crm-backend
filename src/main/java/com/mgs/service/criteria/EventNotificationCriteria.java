package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.EventNotification} entity. This class is used
 * in {@link com.mgs.web.rest.EventNotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventNotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notificationType;

    private StringFilter message;

    private LongFilter eventId;

    private LongFilter userId;

    private Boolean distinct;

    public EventNotificationCriteria() {}

    public EventNotificationCriteria(EventNotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.notificationType = other.optionalNotificationType().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventNotificationCriteria copy() {
        return new EventNotificationCriteria(this);
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

    public StringFilter getNotificationType() {
        return notificationType;
    }

    public Optional<StringFilter> optionalNotificationType() {
        return Optional.ofNullable(notificationType);
    }

    public StringFilter notificationType() {
        if (notificationType == null) {
            setNotificationType(new StringFilter());
        }
        return notificationType;
    }

    public void setNotificationType(StringFilter notificationType) {
        this.notificationType = notificationType;
    }

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final EventNotificationCriteria that = (EventNotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(notificationType, that.notificationType) &&
            Objects.equals(message, that.message) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notificationType, message, eventId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventNotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNotificationType().map(f -> "notificationType=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
