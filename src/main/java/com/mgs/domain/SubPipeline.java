package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubPipeline.
 */
@Entity
@Table(name = "sub_pipeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipeline extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "name_search", length = 255)
    private String nameSearch;

    @NotNull
    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "probability")
    private Integer probability;

    @NotNull
    @Column(name = "is_closing_stage", nullable = false)
    private Boolean isClosingStage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private Pipeline pipeline;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubPipeline id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubPipeline name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSearch() {
        return this.nameSearch;
    }

    public SubPipeline nameSearch(String nameSearch) {
        this.setNameSearch(nameSearch);
        return this;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public Integer getSequenceOrder() {
        return this.sequenceOrder;
    }

    public SubPipeline sequenceOrder(Integer sequenceOrder) {
        this.setSequenceOrder(sequenceOrder);
        return this;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Integer getProbability() {
        return this.probability;
    }

    public SubPipeline probability(Integer probability) {
        this.setProbability(probability);
        return this;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Boolean getIsClosingStage() {
        return this.isClosingStage;
    }

    public SubPipeline isClosingStage(Boolean isClosingStage) {
        this.setIsClosingStage(isClosingStage);
        return this;
    }

    public void setIsClosingStage(Boolean isClosingStage) {
        this.isClosingStage = isClosingStage;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public SubPipeline tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Pipeline getPipeline() {
        return this.pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public SubPipeline pipeline(Pipeline pipeline) {
        this.setPipeline(pipeline);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubPipeline)) {
            return false;
        }
        return getId() != null && getId().equals(((SubPipeline) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipeline{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameSearch='" + getNameSearch() + "'" +
            ", sequenceOrder=" + getSequenceOrder() +
            ", probability=" + getProbability() +
            ", isClosingStage='" + getIsClosingStage() + "'" +
            "}";
    }
}
