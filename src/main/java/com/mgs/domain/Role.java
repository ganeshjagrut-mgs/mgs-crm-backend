package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "is_system")
    private Boolean isSystem;

    @Column(name = "role_level")
    private Integer roleLevel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentRole", "tenant" }, allowSetters = true)
    private Role parentRole;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Role code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public Role isSystem(Boolean isSystem) {
        this.setIsSystem(isSystem);
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Integer getRoleLevel() {
        return this.roleLevel;
    }

    public Role roleLevel(Integer roleLevel) {
        this.setRoleLevel(roleLevel);
        return this;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Role getParentRole() {
        return this.parentRole;
    }

    public Role parentRole(Role parentRole) {
        this.setParentRole(parentRole);
        return this;
    }

    public void setParentRole(Role parentRole) {
        this.parentRole = parentRole;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Role tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return getId() != null && getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", isSystem='" + getIsSystem() + "'" +
            ", roleLevel=" + getRoleLevel() +
            "}";
    }
}
