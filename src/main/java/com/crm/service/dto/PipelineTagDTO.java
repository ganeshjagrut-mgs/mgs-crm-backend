package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.PipelineTag} entity.
 */
@Schema(description = "PipelineTag - Tags for pipelines")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineTagDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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
        if (!(o instanceof PipelineTagDTO)) {
            return false;
        }

        PipelineTagDTO pipelineTagDTO = (PipelineTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pipelineTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineTagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pipeline=" + getPipeline() +
            "}";
    }
}
