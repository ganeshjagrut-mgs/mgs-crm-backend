package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.ReportRun} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportRunDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String filterJson;

    @Size(max = 10)
    private String format;

    private String filePath;

    @NotNull
    private TenantDTO tenant;

    private ReportTemplateDTO template;

    @NotNull
    private UserDTO generatedByUser;

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

    public String getFilterJson() {
        return filterJson;
    }

    public void setFilterJson(String filterJson) {
        this.filterJson = filterJson;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public ReportTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(ReportTemplateDTO template) {
        this.template = template;
    }

    public UserDTO getGeneratedByUser() {
        return generatedByUser;
    }

    public void setGeneratedByUser(UserDTO generatedByUser) {
        this.generatedByUser = generatedByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportRunDTO)) {
            return false;
        }

        ReportRunDTO reportRunDTO = (ReportRunDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportRunDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRunDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", filterJson='" + getFilterJson() + "'" +
            ", format='" + getFormat() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", tenant=" + getTenant() +
            ", template=" + getTemplate() +
            ", generatedByUser=" + getGeneratedByUser() +
            "}";
    }
}
