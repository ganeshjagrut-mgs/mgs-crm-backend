package com.mgs.service.dto;

import com.mgs.domain.enumeration.PipelineModule;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Pipeline} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String nameSearch;

    @NotNull
    private PipelineModule module;

    private Boolean isDefault;

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

    public PipelineModule getModule() {
        return module;
    }

    public void setModule(PipelineModule module) {
        this.module = module;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
        if (!(o instanceof PipelineDTO)) {
            return false;
        }

        PipelineDTO pipelineDTO = (PipelineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pipelineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameSearch='" + getNameSearch() + "'" +
            ", module='" + getModule() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", tenant=" + getTenant() +
            "}";
    }
}
