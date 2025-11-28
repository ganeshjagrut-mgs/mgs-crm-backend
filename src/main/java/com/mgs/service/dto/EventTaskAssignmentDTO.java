package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.EventTaskAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTaskAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private EventDTO event;

    @NotNull
    private TaskDTO task;

    @NotNull
    private UserDTO assignedTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public UserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTaskAssignmentDTO)) {
            return false;
        }

        EventTaskAssignmentDTO eventTaskAssignmentDTO = (EventTaskAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTaskAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTaskAssignmentDTO{" +
            "id=" + getId() +
            ", event=" + getEvent() +
            ", task=" + getTask() +
            ", assignedTo=" + getAssignedTo() +
            "}";
    }
}
