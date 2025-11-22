package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.SubPipeline} entity.
 */
@Schema(description = "SubPipeline - Pipeline configuration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer index;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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
            ", index=" + getIndex() +
            "}";
    }
}
