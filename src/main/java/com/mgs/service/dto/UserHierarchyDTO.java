package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.UserHierarchy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserHierarchyDTO implements Serializable {

    private Long id;

    @Size(max = 30)
    private String relationshipType;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private UserDTO parentUser;

    @NotNull
    private UserDTO childUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public UserDTO getParentUser() {
        return parentUser;
    }

    public void setParentUser(UserDTO parentUser) {
        this.parentUser = parentUser;
    }

    public UserDTO getChildUser() {
        return childUser;
    }

    public void setChildUser(UserDTO childUser) {
        this.childUser = childUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserHierarchyDTO)) {
            return false;
        }

        UserHierarchyDTO userHierarchyDTO = (UserHierarchyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userHierarchyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserHierarchyDTO{" +
            "id=" + getId() +
            ", relationshipType='" + getRelationshipType() + "'" +
            ", tenant=" + getTenant() +
            ", parentUser=" + getParentUser() +
            ", childUser=" + getChildUser() +
            "}";
    }
}
