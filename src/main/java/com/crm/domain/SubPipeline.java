package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SubPipeline - Pipeline configuration
 */
@Entity
@Table(name = "sub_pipeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipeline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "index")
    private Integer index;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subPipeline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stage", "subPipeline" }, allowSetters = true)
    private Set<SubPipelineOpenStage> openStages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subPipeline")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stage", "subPipeline" }, allowSetters = true)
    private Set<SubPipelineCloseStage> closeStages = new HashSet<>();

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

    public Integer getIndex() {
        return this.index;
    }

    public SubPipeline index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Set<SubPipelineOpenStage> getOpenStages() {
        return this.openStages;
    }

    public void setOpenStages(Set<SubPipelineOpenStage> subPipelineOpenStages) {
        if (this.openStages != null) {
            this.openStages.forEach(i -> i.setSubPipeline(null));
        }
        if (subPipelineOpenStages != null) {
            subPipelineOpenStages.forEach(i -> i.setSubPipeline(this));
        }
        this.openStages = subPipelineOpenStages;
    }

    public SubPipeline openStages(Set<SubPipelineOpenStage> subPipelineOpenStages) {
        this.setOpenStages(subPipelineOpenStages);
        return this;
    }

    public SubPipeline addOpenStages(SubPipelineOpenStage subPipelineOpenStage) {
        this.openStages.add(subPipelineOpenStage);
        subPipelineOpenStage.setSubPipeline(this);
        return this;
    }

    public SubPipeline removeOpenStages(SubPipelineOpenStage subPipelineOpenStage) {
        this.openStages.remove(subPipelineOpenStage);
        subPipelineOpenStage.setSubPipeline(null);
        return this;
    }

    public Set<SubPipelineCloseStage> getCloseStages() {
        return this.closeStages;
    }

    public void setCloseStages(Set<SubPipelineCloseStage> subPipelineCloseStages) {
        if (this.closeStages != null) {
            this.closeStages.forEach(i -> i.setSubPipeline(null));
        }
        if (subPipelineCloseStages != null) {
            subPipelineCloseStages.forEach(i -> i.setSubPipeline(this));
        }
        this.closeStages = subPipelineCloseStages;
    }

    public SubPipeline closeStages(Set<SubPipelineCloseStage> subPipelineCloseStages) {
        this.setCloseStages(subPipelineCloseStages);
        return this;
    }

    public SubPipeline addCloseStages(SubPipelineCloseStage subPipelineCloseStage) {
        this.closeStages.add(subPipelineCloseStage);
        subPipelineCloseStage.setSubPipeline(this);
        return this;
    }

    public SubPipeline removeCloseStages(SubPipelineCloseStage subPipelineCloseStage) {
        this.closeStages.remove(subPipelineCloseStage);
        subPipelineCloseStage.setSubPipeline(null);
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
            ", index=" + getIndex() +
            "}";
    }
}
