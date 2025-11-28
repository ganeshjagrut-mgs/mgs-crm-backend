package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.AuditLog} entity. This class is used
 * in {@link com.mgs.web.rest.AuditLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /audit-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter actionType;

    private StringFilter entityType;

    private UUIDFilter entityId;

    private StringFilter oldValue;

    private StringFilter newValue;

    private LongFilter tenantId;

    private LongFilter performedById;

    private Boolean distinct;

    public AuditLogCriteria() {}

    public AuditLogCriteria(AuditLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.actionType = other.optionalActionType().map(StringFilter::copy).orElse(null);
        this.entityType = other.optionalEntityType().map(StringFilter::copy).orElse(null);
        this.entityId = other.optionalEntityId().map(UUIDFilter::copy).orElse(null);
        this.oldValue = other.optionalOldValue().map(StringFilter::copy).orElse(null);
        this.newValue = other.optionalNewValue().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.performedById = other.optionalPerformedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AuditLogCriteria copy() {
        return new AuditLogCriteria(this);
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

    public StringFilter getActionType() {
        return actionType;
    }

    public Optional<StringFilter> optionalActionType() {
        return Optional.ofNullable(actionType);
    }

    public StringFilter actionType() {
        if (actionType == null) {
            setActionType(new StringFilter());
        }
        return actionType;
    }

    public void setActionType(StringFilter actionType) {
        this.actionType = actionType;
    }

    public StringFilter getEntityType() {
        return entityType;
    }

    public Optional<StringFilter> optionalEntityType() {
        return Optional.ofNullable(entityType);
    }

    public StringFilter entityType() {
        if (entityType == null) {
            setEntityType(new StringFilter());
        }
        return entityType;
    }

    public void setEntityType(StringFilter entityType) {
        this.entityType = entityType;
    }

    public UUIDFilter getEntityId() {
        return entityId;
    }

    public Optional<UUIDFilter> optionalEntityId() {
        return Optional.ofNullable(entityId);
    }

    public UUIDFilter entityId() {
        if (entityId == null) {
            setEntityId(new UUIDFilter());
        }
        return entityId;
    }

    public void setEntityId(UUIDFilter entityId) {
        this.entityId = entityId;
    }

    public StringFilter getOldValue() {
        return oldValue;
    }

    public Optional<StringFilter> optionalOldValue() {
        return Optional.ofNullable(oldValue);
    }

    public StringFilter oldValue() {
        if (oldValue == null) {
            setOldValue(new StringFilter());
        }
        return oldValue;
    }

    public void setOldValue(StringFilter oldValue) {
        this.oldValue = oldValue;
    }

    public StringFilter getNewValue() {
        return newValue;
    }

    public Optional<StringFilter> optionalNewValue() {
        return Optional.ofNullable(newValue);
    }

    public StringFilter newValue() {
        if (newValue == null) {
            setNewValue(new StringFilter());
        }
        return newValue;
    }

    public void setNewValue(StringFilter newValue) {
        this.newValue = newValue;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getPerformedById() {
        return performedById;
    }

    public Optional<LongFilter> optionalPerformedById() {
        return Optional.ofNullable(performedById);
    }

    public LongFilter performedById() {
        if (performedById == null) {
            setPerformedById(new LongFilter());
        }
        return performedById;
    }

    public void setPerformedById(LongFilter performedById) {
        this.performedById = performedById;
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
        final AuditLogCriteria that = (AuditLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(actionType, that.actionType) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(entityId, that.entityId) &&
            Objects.equals(oldValue, that.oldValue) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(performedById, that.performedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionType, entityType, entityId, oldValue, newValue, tenantId, performedById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalActionType().map(f -> "actionType=" + f + ", ").orElse("") +
            optionalEntityType().map(f -> "entityType=" + f + ", ").orElse("") +
            optionalEntityId().map(f -> "entityId=" + f + ", ").orElse("") +
            optionalOldValue().map(f -> "oldValue=" + f + ", ").orElse("") +
            optionalNewValue().map(f -> "newValue=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalPerformedById().map(f -> "performedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
