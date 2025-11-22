package com.crm.service.dto;

import com.crm.domain.enumeration.Status;
import com.crm.domain.enumeration.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.crm.domain.Task} entity.
 */
@Schema(description = "Task - Tasks related to sales activities")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private TaskType taskType;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private String taskName;

    @NotNull
    private Status status;

    private LocalDate taskCompletionDate;

    private UUID correlationId;

    private UserDTO taskOwner;

    private CustomerDTO customer;

    private MasterStaticTypeDTO relatedTo;

    private PipelineDTO pipeline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getTaskCompletionDate() {
        return taskCompletionDate;
    }

    public void setTaskCompletionDate(LocalDate taskCompletionDate) {
        this.taskCompletionDate = taskCompletionDate;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public UserDTO getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(UserDTO taskOwner) {
        this.taskOwner = taskOwner;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public MasterStaticTypeDTO getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(MasterStaticTypeDTO relatedTo) {
        this.relatedTo = relatedTo;
    }

    public PipelineDTO getPipeline() {
        return pipeline;
    }

    public void setPipeline(PipelineDTO pipeline) {
        this.pipeline = pipeline;
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
            ", taskType='" + getTaskType() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", taskName='" + getTaskName() + "'" +
            ", status='" + getStatus() + "'" +
            ", taskCompletionDate='" + getTaskCompletionDate() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", taskOwner=" + getTaskOwner() +
            ", customer=" + getCustomer() +
            ", relatedTo=" + getRelatedTo() +
            ", pipeline=" + getPipeline() +
            "}";
    }
}
