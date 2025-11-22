package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.SubPipeline} entity. This class is used
 * in {@link com.crm.web.rest.SubPipelineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sub-pipelines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubPipelineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter index;

    private LongFilter openStagesId;

    private LongFilter closeStagesId;

    private Boolean distinct;

    public SubPipelineCriteria() {}

    public SubPipelineCriteria(SubPipelineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.index = other.index == null ? null : other.index.copy();
        this.openStagesId = other.openStagesId == null ? null : other.openStagesId.copy();
        this.closeStagesId = other.closeStagesId == null ? null : other.closeStagesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SubPipelineCriteria copy() {
        return new SubPipelineCriteria(this);
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

    public IntegerFilter getIndex() {
        return index;
    }

    public IntegerFilter index() {
        if (index == null) {
            index = new IntegerFilter();
        }
        return index;
    }

    public void setIndex(IntegerFilter index) {
        this.index = index;
    }

    public LongFilter getOpenStagesId() {
        return openStagesId;
    }

    public LongFilter openStagesId() {
        if (openStagesId == null) {
            openStagesId = new LongFilter();
        }
        return openStagesId;
    }

    public void setOpenStagesId(LongFilter openStagesId) {
        this.openStagesId = openStagesId;
    }

    public LongFilter getCloseStagesId() {
        return closeStagesId;
    }

    public LongFilter closeStagesId() {
        if (closeStagesId == null) {
            closeStagesId = new LongFilter();
        }
        return closeStagesId;
    }

    public void setCloseStagesId(LongFilter closeStagesId) {
        this.closeStagesId = closeStagesId;
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
        final SubPipelineCriteria that = (SubPipelineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(index, that.index) &&
            Objects.equals(openStagesId, that.openStagesId) &&
            Objects.equals(closeStagesId, that.closeStagesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, index, openStagesId, closeStagesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubPipelineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (index != null ? "index=" + index + ", " : "") +
            (openStagesId != null ? "openStagesId=" + openStagesId + ", " : "") +
            (closeStagesId != null ? "closeStagesId=" + closeStagesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
