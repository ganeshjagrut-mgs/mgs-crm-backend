package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.EntityType} entity.
 */
@Schema(description = "EntityType - Defines types of entities in the system")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntityTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String label;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityTypeDTO)) {
            return false;
        }

        EntityTypeDTO entityTypeDTO = (EntityTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entityTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntityTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
