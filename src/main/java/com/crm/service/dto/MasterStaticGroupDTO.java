package com.crm.service.dto;

import com.crm.domain.enumeration.MasterStaticGroupEditable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.MasterStaticGroup} entity.
 */
@Schema(description = "MasterStaticGroup - Groups master static data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterStaticGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private String uiLabel;

    private MasterStaticGroupEditable editable;

    private EntityTypeDTO entityType;

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

    public String getUiLabel() {
        return uiLabel;
    }

    public void setUiLabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    public MasterStaticGroupEditable getEditable() {
        return editable;
    }

    public void setEditable(MasterStaticGroupEditable editable) {
        this.editable = editable;
    }

    public EntityTypeDTO getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityTypeDTO entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterStaticGroupDTO)) {
            return false;
        }

        MasterStaticGroupDTO masterStaticGroupDTO = (MasterStaticGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, masterStaticGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterStaticGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", uiLabel='" + getUiLabel() + "'" +
            ", editable='" + getEditable() + "'" +
            ", entityType=" + getEntityType() +
            "}";
    }
}
