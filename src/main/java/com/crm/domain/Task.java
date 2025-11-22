package com.crm.domain;

import com.crm.domain.enumeration.Status;
import com.crm.domain.enumeration.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task - Tasks related to sales activities
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull
    @Column(name = "task_name", nullable = false)
    private String taskName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "task_completion_date")
    private LocalDate taskCompletionDate;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User taskOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "addresses",
            "company",
            "user",
            "customerType",
            "customerStatus",
            "ownershipType",
            "industryType",
            "customerCategory",
            "paymentTerms",
            "invoiceFrequency",
            "gstTreatment",
            "outstandingPerson",
            "department",
            "tenat",
            "masterCategories",
        },
        allowSetters = true
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType relatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "pipelineTags", "tasks", "pipelineOwner", "customer", "stageOfPipeline", "subPipeline" },
        allowSetters = true
    )
    private Pipeline pipeline;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    public Task taskType(TaskType taskType) {
        this.setTaskType(taskType);
        return this;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Task dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public Task taskName(String taskName) {
        this.setTaskName(taskName);
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Status getStatus() {
        return this.status;
    }

    public Task status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getTaskCompletionDate() {
        return this.taskCompletionDate;
    }

    public Task taskCompletionDate(LocalDate taskCompletionDate) {
        this.setTaskCompletionDate(taskCompletionDate);
        return this;
    }

    public void setTaskCompletionDate(LocalDate taskCompletionDate) {
        this.taskCompletionDate = taskCompletionDate;
    }

    public UUID getCorrelationId() {
        return this.correlationId;
    }

    public Task correlationId(UUID correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public User getTaskOwner() {
        return this.taskOwner;
    }

    public void setTaskOwner(User user) {
        this.taskOwner = user;
    }

    public Task taskOwner(User user) {
        this.setTaskOwner(user);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Task customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public MasterStaticType getRelatedTo() {
        return this.relatedTo;
    }

    public void setRelatedTo(MasterStaticType masterStaticType) {
        this.relatedTo = masterStaticType;
    }

    public Task relatedTo(MasterStaticType masterStaticType) {
        this.setRelatedTo(masterStaticType);
        return this;
    }

    public Pipeline getPipeline() {
        return this.pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Task pipeline(Pipeline pipeline) {
        this.setPipeline(pipeline);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return getId() != null && getId().equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", taskType='" + getTaskType() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", taskName='" + getTaskName() + "'" +
            ", status='" + getStatus() + "'" +
            ", taskCompletionDate='" + getTaskCompletionDate() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            "}";
    }
}
