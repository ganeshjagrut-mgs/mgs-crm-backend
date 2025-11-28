package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.UserHierarchy} entity. This class is used
 * in {@link com.mgs.web.rest.UserHierarchyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-hierarchies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserHierarchyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter relationshipType;

    private LongFilter tenantId;

    private LongFilter parentUserId;

    private LongFilter childUserId;

    private Boolean distinct;

    public UserHierarchyCriteria() {}

    public UserHierarchyCriteria(UserHierarchyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.relationshipType = other.optionalRelationshipType().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.parentUserId = other.optionalParentUserId().map(LongFilter::copy).orElse(null);
        this.childUserId = other.optionalChildUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserHierarchyCriteria copy() {
        return new UserHierarchyCriteria(this);
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

    public StringFilter getRelationshipType() {
        return relationshipType;
    }

    public Optional<StringFilter> optionalRelationshipType() {
        return Optional.ofNullable(relationshipType);
    }

    public StringFilter relationshipType() {
        if (relationshipType == null) {
            setRelationshipType(new StringFilter());
        }
        return relationshipType;
    }

    public void setRelationshipType(StringFilter relationshipType) {
        this.relationshipType = relationshipType;
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

    public LongFilter getParentUserId() {
        return parentUserId;
    }

    public Optional<LongFilter> optionalParentUserId() {
        return Optional.ofNullable(parentUserId);
    }

    public LongFilter parentUserId() {
        if (parentUserId == null) {
            setParentUserId(new LongFilter());
        }
        return parentUserId;
    }

    public void setParentUserId(LongFilter parentUserId) {
        this.parentUserId = parentUserId;
    }

    public LongFilter getChildUserId() {
        return childUserId;
    }

    public Optional<LongFilter> optionalChildUserId() {
        return Optional.ofNullable(childUserId);
    }

    public LongFilter childUserId() {
        if (childUserId == null) {
            setChildUserId(new LongFilter());
        }
        return childUserId;
    }

    public void setChildUserId(LongFilter childUserId) {
        this.childUserId = childUserId;
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
        final UserHierarchyCriteria that = (UserHierarchyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(relationshipType, that.relationshipType) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(parentUserId, that.parentUserId) &&
            Objects.equals(childUserId, that.childUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationshipType, tenantId, parentUserId, childUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserHierarchyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRelationshipType().map(f -> "relationshipType=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalParentUserId().map(f -> "parentUserId=" + f + ", ").orElse("") +
            optionalChildUserId().map(f -> "childUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
