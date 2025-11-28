package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.RolePermission} entity. This class is used
 * in {@link com.mgs.web.rest.RolePermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /role-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermissionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter canRead;

    private BooleanFilter canCreate;

    private BooleanFilter canUpdate;

    private BooleanFilter canDelete;

    private LongFilter roleId;

    private LongFilter moduleId;

    private Boolean distinct;

    public RolePermissionCriteria() {}

    public RolePermissionCriteria(RolePermissionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.canRead = other.optionalCanRead().map(BooleanFilter::copy).orElse(null);
        this.canCreate = other.optionalCanCreate().map(BooleanFilter::copy).orElse(null);
        this.canUpdate = other.optionalCanUpdate().map(BooleanFilter::copy).orElse(null);
        this.canDelete = other.optionalCanDelete().map(BooleanFilter::copy).orElse(null);
        this.roleId = other.optionalRoleId().map(LongFilter::copy).orElse(null);
        this.moduleId = other.optionalModuleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RolePermissionCriteria copy() {
        return new RolePermissionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getCanRead() {
        return canRead;
    }

    public Optional<BooleanFilter> optionalCanRead() {
        return Optional.ofNullable(canRead);
    }

    public BooleanFilter canRead() {
        if (canRead == null) {
            setCanRead(new BooleanFilter());
        }
        return canRead;
    }

    public void setCanRead(BooleanFilter canRead) {
        this.canRead = canRead;
    }

    public BooleanFilter getCanCreate() {
        return canCreate;
    }

    public Optional<BooleanFilter> optionalCanCreate() {
        return Optional.ofNullable(canCreate);
    }

    public BooleanFilter canCreate() {
        if (canCreate == null) {
            setCanCreate(new BooleanFilter());
        }
        return canCreate;
    }

    public void setCanCreate(BooleanFilter canCreate) {
        this.canCreate = canCreate;
    }

    public BooleanFilter getCanUpdate() {
        return canUpdate;
    }

    public Optional<BooleanFilter> optionalCanUpdate() {
        return Optional.ofNullable(canUpdate);
    }

    public BooleanFilter canUpdate() {
        if (canUpdate == null) {
            setCanUpdate(new BooleanFilter());
        }
        return canUpdate;
    }

    public void setCanUpdate(BooleanFilter canUpdate) {
        this.canUpdate = canUpdate;
    }

    public BooleanFilter getCanDelete() {
        return canDelete;
    }

    public Optional<BooleanFilter> optionalCanDelete() {
        return Optional.ofNullable(canDelete);
    }

    public BooleanFilter canDelete() {
        if (canDelete == null) {
            setCanDelete(new BooleanFilter());
        }
        return canDelete;
    }

    public void setCanDelete(BooleanFilter canDelete) {
        this.canDelete = canDelete;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public Optional<LongFilter> optionalRoleId() {
        return Optional.ofNullable(roleId);
    }

    public LongFilter roleId() {
        if (roleId == null) {
            setRoleId(new LongFilter());
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
    }

    public LongFilter getModuleId() {
        return moduleId;
    }

    public Optional<LongFilter> optionalModuleId() {
        return Optional.ofNullable(moduleId);
    }

    public LongFilter moduleId() {
        if (moduleId == null) {
            setModuleId(new LongFilter());
        }
        return moduleId;
    }

    public void setModuleId(LongFilter moduleId) {
        this.moduleId = moduleId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RolePermissionCriteria that = (RolePermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(canRead, that.canRead) &&
            Objects.equals(canCreate, that.canCreate) &&
            Objects.equals(canUpdate, that.canUpdate) &&
            Objects.equals(canDelete, that.canDelete) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(moduleId, that.moduleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, canRead, canCreate, canUpdate, canDelete, roleId, moduleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermissionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCanRead().map(f -> "canRead=" + f + ", ").orElse("") +
            optionalCanCreate().map(f -> "canCreate=" + f + ", ").orElse("") +
            optionalCanUpdate().map(f -> "canUpdate=" + f + ", ").orElse("") +
            optionalCanDelete().map(f -> "canDelete=" + f + ", ").orElse("") +
            optionalRoleId().map(f -> "roleId=" + f + ", ").orElse("") +
            optionalModuleId().map(f -> "moduleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
