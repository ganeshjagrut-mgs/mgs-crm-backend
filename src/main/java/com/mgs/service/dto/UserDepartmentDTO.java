package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.UserDepartment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private UserDTO user;

    @NotNull
    private DepartmentDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDepartmentDTO)) {
            return false;
        }

        UserDepartmentDTO userDepartmentDTO = (UserDepartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userDepartmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDepartmentDTO{" +
            "id=" + getId() +
            ", tenant=" + getTenant() +
            ", user=" + getUser() +
            ", department=" + getDepartment() +
            "}";
    }
}
