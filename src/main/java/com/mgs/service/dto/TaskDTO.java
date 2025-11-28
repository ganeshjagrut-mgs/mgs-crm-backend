package com.mgs.service.dto;

import com.mgs.domain.enumeration.TaskPriority;
import com.mgs.domain.enumeration.TaskStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Task} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    @Size(max = 30)
    private String relatedType;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    private Instant dueAt;

    private Instant completedAt;

    @NotNull
    private TenantDTO tenant;

    private TaskTypeDTO taskType;

    @NotNull
    private UserDTO assignedUser;

    @NotNull
    private UserDTO createdByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public TaskTypeDTO getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeDTO taskType) {
        this.taskType = taskType;
    }

    public UserDTO getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserDTO assignedUser) {
        this.assignedUser = assignedUser;
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
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", relatedType='" + getRelatedType() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", dueAt='" + getDueAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", tenant=" + getTenant() +
            ", taskType=" + getTaskType() +
            ", assignedUser=" + getAssignedUser() +
            ", createdByUser=" + getCreatedByUser() +
            "}";
    }
}
