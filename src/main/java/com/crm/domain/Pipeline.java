package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Pipeline - Sales pipeline/opportunities
 */
@Entity
@Table(name = "pipeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pipeline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pipeline_name", nullable = false)
    private String pipelineName;

    @Column(name = "total_amount", precision = 21, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "no_of_samples")
    private Integer noOfSamples;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pipeline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pipeline" }, allowSetters = true)
    private Set<PipelineTag> pipelineTags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pipeline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "taskOwner", "customer", "relatedTo", "pipeline" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User pipelineOwner;

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
    private MasterStaticType stageOfPipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "openStages", "closeStages" }, allowSetters = true)
    private SubPipeline subPipeline;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pipeline id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPipelineName() {
        return this.pipelineName;
    }

    public Pipeline pipelineName(String pipelineName) {
        this.setPipelineName(pipelineName);
        return this;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Pipeline totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNoOfSamples() {
        return this.noOfSamples;
    }

    public Pipeline noOfSamples(Integer noOfSamples) {
        this.setNoOfSamples(noOfSamples);
        return this;
    }

    public void setNoOfSamples(Integer noOfSamples) {
        this.noOfSamples = noOfSamples;
    }

    public UUID getCorrelationId() {
        return this.correlationId;
    }

    public Pipeline correlationId(UUID correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public Set<PipelineTag> getPipelineTags() {
        return this.pipelineTags;
    }

    public void setPipelineTags(Set<PipelineTag> pipelineTags) {
        if (this.pipelineTags != null) {
            this.pipelineTags.forEach(i -> i.setPipeline(null));
        }
        if (pipelineTags != null) {
            pipelineTags.forEach(i -> i.setPipeline(this));
        }
        this.pipelineTags = pipelineTags;
    }

    public Pipeline pipelineTags(Set<PipelineTag> pipelineTags) {
        this.setPipelineTags(pipelineTags);
        return this;
    }

    public Pipeline addPipelineTags(PipelineTag pipelineTag) {
        this.pipelineTags.add(pipelineTag);
        pipelineTag.setPipeline(this);
        return this;
    }

    public Pipeline removePipelineTags(PipelineTag pipelineTag) {
        this.pipelineTags.remove(pipelineTag);
        pipelineTag.setPipeline(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setPipeline(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setPipeline(this));
        }
        this.tasks = tasks;
    }

    public Pipeline tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Pipeline addTasks(Task task) {
        this.tasks.add(task);
        task.setPipeline(this);
        return this;
    }

    public Pipeline removeTasks(Task task) {
        this.tasks.remove(task);
        task.setPipeline(null);
        return this;
    }

    public User getPipelineOwner() {
        return this.pipelineOwner;
    }

    public void setPipelineOwner(User user) {
        this.pipelineOwner = user;
    }

    public Pipeline pipelineOwner(User user) {
        this.setPipelineOwner(user);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Pipeline customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public MasterStaticType getStageOfPipeline() {
        return this.stageOfPipeline;
    }

    public void setStageOfPipeline(MasterStaticType masterStaticType) {
        this.stageOfPipeline = masterStaticType;
    }

    public Pipeline stageOfPipeline(MasterStaticType masterStaticType) {
        this.setStageOfPipeline(masterStaticType);
        return this;
    }

    public SubPipeline getSubPipeline() {
        return this.subPipeline;
    }

    public void setSubPipeline(SubPipeline subPipeline) {
        this.subPipeline = subPipeline;
    }

    public Pipeline subPipeline(SubPipeline subPipeline) {
        this.setSubPipeline(subPipeline);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pipeline)) {
            return false;
        }
        return getId() != null && getId().equals(((Pipeline) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pipeline{" +
            "id=" + getId() +
            ", pipelineName='" + getPipelineName() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", noOfSamples=" + getNoOfSamples() +
            ", correlationId='" + getCorrelationId() + "'" +
            "}";
    }
}
