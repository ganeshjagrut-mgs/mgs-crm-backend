package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.SubPipeline} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String nameSearch;

    @NotNull
    private Integer sequenceOrder;

    private Integer probability;

    @NotNull
    private Boolean isClosingStage;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private PipelineDTO pipeline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Boolean getIsClosingStage() {
        return isClosingStage;
    }

    public void setIsClosingStage(Boolean isClosingStage) {
        this.isClosingStage = isClosingStage;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
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
        if (!(o instanceof SubPipelineDTO)) {
            return false;
        }

        SubPipelineDTO subPipelineDTO = (SubPipelineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subPipelineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameSearch='" + getNameSearch() + "'" +
            ", sequenceOrder=" + getSequenceOrder() +
            ", probability=" + getProbability() +
            ", isClosingStage='" + getIsClosingStage() + "'" +
            ", tenant=" + getTenant() +
            ", pipeline=" + getPipeline() +
            "}";
    }
}
