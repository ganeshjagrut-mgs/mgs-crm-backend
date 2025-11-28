package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.LeadSource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadSourceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String nameSearch;

    private Boolean isActive;

    @NotNull
    private TenantDTO tenant;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeadSourceDTO)) {
            return false;
        }

        LeadSourceDTO leadSourceDTO = (LeadSourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leadSourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeadSourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameSearch='" + getNameSearch() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
