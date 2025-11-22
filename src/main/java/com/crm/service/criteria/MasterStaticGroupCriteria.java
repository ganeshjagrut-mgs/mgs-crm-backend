package com.crm.service.criteria;

import com.crm.domain.enumeration.MasterStaticGroupEditable;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.MasterStaticGroup} entity. This class is used
 * in {@link com.crm.web.rest.MasterStaticGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /master-static-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterStaticGroupCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MasterStaticGroupEditable
     */
    public static class MasterStaticGroupEditableFilter extends Filter<MasterStaticGroupEditable> {

        public MasterStaticGroupEditableFilter() {}

        public MasterStaticGroupEditableFilter(MasterStaticGroupEditableFilter filter) {
            super(filter);
        }

        @Override
        public MasterStaticGroupEditableFilter copy() {
            return new MasterStaticGroupEditableFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter uiLabel;

    private MasterStaticGroupEditableFilter editable;

    private LongFilter entityTypeId;

    private Boolean distinct;

    public MasterStaticGroupCriteria() {}

    public MasterStaticGroupCriteria(MasterStaticGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.uiLabel = other.uiLabel == null ? null : other.uiLabel.copy();
        this.editable = other.editable == null ? null : other.editable.copy();
        this.entityTypeId = other.entityTypeId == null ? null : other.entityTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MasterStaticGroupCriteria copy() {
        return new MasterStaticGroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getUiLabel() {
        return uiLabel;
    }

    public StringFilter uiLabel() {
        if (uiLabel == null) {
            uiLabel = new StringFilter();
        }
        return uiLabel;
    }

    public void setUiLabel(StringFilter uiLabel) {
        this.uiLabel = uiLabel;
    }

    public MasterStaticGroupEditableFilter getEditable() {
        return editable;
    }

    public MasterStaticGroupEditableFilter editable() {
        if (editable == null) {
            editable = new MasterStaticGroupEditableFilter();
        }
        return editable;
    }

    public void setEditable(MasterStaticGroupEditableFilter editable) {
        this.editable = editable;
    }

    public LongFilter getEntityTypeId() {
        return entityTypeId;
    }

    public LongFilter entityTypeId() {
        if (entityTypeId == null) {
            entityTypeId = new LongFilter();
        }
        return entityTypeId;
    }

    public void setEntityTypeId(LongFilter entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Boolean getDistinct() {
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
        final MasterStaticGroupCriteria that = (MasterStaticGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(uiLabel, that.uiLabel) &&
            Objects.equals(editable, that.editable) &&
            Objects.equals(entityTypeId, that.entityTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, uiLabel, editable, entityTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterStaticGroupCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (uiLabel != null ? "uiLabel=" + uiLabel + ", " : "") +
            (editable != null ? "editable=" + editable + ", " : "") +
            (entityTypeId != null ? "entityTypeId=" + entityTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
