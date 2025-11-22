package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SubPipelineOpenStage - Open stages in pipeline
 */
@Entity
@Table(name = "sub_pipeline_open_stage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineOpenStage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "index")
    private Integer index;

    @ManyToOne(optional = false)
    @NotNull
    private MasterStaticType stage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "openStages", "closeStages" }, allowSetters = true)
    private SubPipeline subPipeline;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubPipelineOpenStage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return this.index;
    }

    public SubPipelineOpenStage index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public MasterStaticType getStage() {
        return this.stage;
    }

    public void setStage(MasterStaticType masterStaticType) {
        this.stage = masterStaticType;
    }

    public SubPipelineOpenStage stage(MasterStaticType masterStaticType) {
        this.setStage(masterStaticType);
        return this;
    }

    public SubPipeline getSubPipeline() {
        return this.subPipeline;
    }

    public void setSubPipeline(SubPipeline subPipeline) {
        this.subPipeline = subPipeline;
    }

    public SubPipelineOpenStage subPipeline(SubPipeline subPipeline) {
        this.setSubPipeline(subPipeline);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubPipelineOpenStage)) {
            return false;
        }
        return getId() != null && getId().equals(((SubPipelineOpenStage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineOpenStage{" +
            "id=" + getId() +
            ", index=" + getIndex() +
            "}";
    }
}
