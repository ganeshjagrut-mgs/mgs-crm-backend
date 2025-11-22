package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.MasterStaticType} entity.
 */
@Schema(description = "MasterStaticType - Master data for various dropdowns and selections")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterStaticTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private Integer displayOrder;

    private Boolean isActive;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterStaticTypeDTO)) {
            return false;
        }

        MasterStaticTypeDTO masterStaticTypeDTO = (MasterStaticTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, masterStaticTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterStaticTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
