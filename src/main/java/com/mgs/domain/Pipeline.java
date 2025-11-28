package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.PipelineModule;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pipeline.
 */
@Entity
@Table(name = "pipeline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pipeline extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "name_search", length = 255)
    private String nameSearch;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false)
    private PipelineModule module;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pipeline id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pipeline name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSearch() {
        return this.nameSearch;
    }

    public Pipeline nameSearch(String nameSearch) {
        this.setNameSearch(nameSearch);
        return this;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public PipelineModule getModule() {
        return this.module;
    }

    public Pipeline module(PipelineModule module) {
        this.setModule(module);
        return this;
    }

    public void setModule(PipelineModule module) {
        this.module = module;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public Pipeline isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Pipeline tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pipeline)) {
            return false;
        }
        return getId() != null && getId().equals(((Pipeline) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pipeline{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameSearch='" + getNameSearch() + "'" +
            ", module='" + getModule() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            "}";
    }
}
