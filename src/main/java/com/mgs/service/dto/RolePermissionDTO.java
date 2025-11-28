package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.RolePermission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermissionDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean canRead;

    @NotNull
    private Boolean canCreate;

    @NotNull
    private Boolean canUpdate;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private RoleDTO role;

    @NotNull
    private PermissionModuleDTO module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(Boolean canCreate) {
        this.canCreate = canCreate;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public PermissionModuleDTO getModule() {
        return module;
    }

    public void setModule(PermissionModuleDTO module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermissionDTO)) {
            return false;
        }

        RolePermissionDTO rolePermissionDTO = (RolePermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rolePermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionDTO{" +
            "id=" + getId() +
            ", canRead='" + getCanRead() + "'" +
            ", canCreate='" + getCanCreate() + "'" +
            ", canUpdate='" + getCanUpdate() + "'" +
            ", canDelete='" + getCanDelete() + "'" +
            ", role=" + getRole() +
            ", module=" + getModule() +
            "}";
    }
}
