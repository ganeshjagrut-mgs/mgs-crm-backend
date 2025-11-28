package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RolePermission.
 */
@Entity
@Table(name = "role_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermission extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "can_read", nullable = false)
    private Boolean canRead;

    @NotNull
    @Column(name = "can_create", nullable = false)
    private Boolean canCreate;

    @NotNull
    @Column(name = "can_update", nullable = false)
    private Boolean canUpdate;

    @NotNull
    @Column(name = "can_delete", nullable = false)
    private Boolean canDelete;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private Role role;

    @ManyToOne(optional = false)
    @NotNull
    private PermissionModule module;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RolePermission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCanRead() {
        return this.canRead;
    }

    public RolePermission canRead(Boolean canRead) {
        this.setCanRead(canRead);
        return this;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanCreate() {
        return this.canCreate;
    }

    public RolePermission canCreate(Boolean canCreate) {
        this.setCanCreate(canCreate);
        return this;
    }

    public void setCanCreate(Boolean canCreate) {
        this.canCreate = canCreate;
    }

    public Boolean getCanUpdate() {
        return this.canUpdate;
    }

    public RolePermission canUpdate(Boolean canUpdate) {
        this.setCanUpdate(canUpdate);
        return this;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanDelete() {
        return this.canDelete;
    }

    public RolePermission canDelete(Boolean canDelete) {
        this.setCanDelete(canDelete);
        return this;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RolePermission role(Role role) {
        this.setRole(role);
        return this;
    }

    public PermissionModule getModule() {
        return this.module;
    }

    public void setModule(PermissionModule permissionModule) {
        this.module = permissionModule;
    }

    public RolePermission module(PermissionModule permissionModule) {
        this.setModule(permissionModule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermission)) {
            return false;
        }
        return getId() != null && getId().equals(((RolePermission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermission{" +
            "id=" + getId() +
            ", canRead='" + getCanRead() + "'" +
            ", canCreate='" + getCanCreate() + "'" +
            ", canUpdate='" + getCanUpdate() + "'" +
            ", canDelete='" + getCanDelete() + "'" +
            "}";
    }
}
