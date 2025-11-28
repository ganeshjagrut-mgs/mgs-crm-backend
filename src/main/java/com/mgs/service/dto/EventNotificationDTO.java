package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.EventNotification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventNotificationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String notificationType;

    @NotNull
    private String message;

    @NotNull
    private EventDTO event;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventNotificationDTO)) {
            return false;
        }

        EventNotificationDTO eventNotificationDTO = (EventNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventNotificationDTO{" +
            "id=" + getId() +
            ", notificationType='" + getNotificationType() + "'" +
            ", message='" + getMessage() + "'" +
            ", event=" + getEvent() +
            ", user=" + getUser() +
            "}";
    }
}
