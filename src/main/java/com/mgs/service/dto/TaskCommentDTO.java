package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.TaskComment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskCommentDTO implements Serializable {

    private Long id;

    @NotNull
    private String comment;

    @NotNull
    private TaskDTO task;

    @NotNull
    private UserDTO createdByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public UserDTO getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserDTO createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskCommentDTO)) {
            return false;
        }

        TaskCommentDTO taskCommentDTO = (TaskCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCommentDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", task=" + getTask() +
            ", createdByUser=" + getCreatedByUser() +
            "}";
    }
}
