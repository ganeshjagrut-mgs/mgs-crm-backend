package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.SubPipelineOpenStage} entity.
 */
@Schema(description = "SubPipelineOpenStage - Open stages in pipeline")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineOpenStageDTO implements Serializable {

    private Long id;

    private Integer index;

    private MasterStaticTypeDTO stage;

    private SubPipelineDTO subPipeline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public MasterStaticTypeDTO getStage() {
        return stage;
    }

    public void setStage(MasterStaticTypeDTO stage) {
        this.stage = stage;
    }

    public SubPipelineDTO getSubPipeline() {
        return subPipeline;
    }

    public void setSubPipeline(SubPipelineDTO subPipeline) {
        this.subPipeline = subPipeline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubPipelineOpenStageDTO)) {
            return false;
        }

        SubPipelineOpenStageDTO subPipelineOpenStageDTO = (SubPipelineOpenStageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subPipelineOpenStageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineOpenStageDTO{" +
            "id=" + getId() +
            ", index=" + getIndex() +
            ", stage=" + getStage() +
            ", subPipeline=" + getSubPipeline() +
            "}";
    }
}
