package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.PipelineAudit} entity. This class is used
 * in {@link com.crm.web.rest.PipelineAuditResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pipeline-audits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineAuditCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter eventTimestamp;

    private StringFilter action;

    private UUIDFilter rowId;

    private UUIDFilter correlationId;

    private Boolean distinct;

    public PipelineAuditCriteria() {}

    public PipelineAuditCriteria(PipelineAuditCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.eventTimestamp = other.eventTimestamp == null ? null : other.eventTimestamp.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.rowId = other.rowId == null ? null : other.rowId.copy();
        this.correlationId = other.correlationId == null ? null : other.correlationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PipelineAuditCriteria copy() {
        return new PipelineAuditCriteria(this);
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

    public InstantFilter getEventTimestamp() {
        return eventTimestamp;
    }

    public InstantFilter eventTimestamp() {
        if (eventTimestamp == null) {
            eventTimestamp = new InstantFilter();
        }
        return eventTimestamp;
    }

    public void setEventTimestamp(InstantFilter eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public StringFilter getAction() {
        return action;
    }

    public StringFilter action() {
        if (action == null) {
            action = new StringFilter();
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public UUIDFilter getRowId() {
        return rowId;
    }

    public UUIDFilter rowId() {
        if (rowId == null) {
            rowId = new UUIDFilter();
        }
        return rowId;
    }

    public void setRowId(UUIDFilter rowId) {
        this.rowId = rowId;
    }

    public UUIDFilter getCorrelationId() {
        return correlationId;
    }

    public UUIDFilter correlationId() {
        if (correlationId == null) {
            correlationId = new UUIDFilter();
        }
        return correlationId;
    }

    public void setCorrelationId(UUIDFilter correlationId) {
        this.correlationId = correlationId;
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
        final PipelineAuditCriteria that = (PipelineAuditCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventTimestamp, that.eventTimestamp) &&
            Objects.equals(action, that.action) &&
            Objects.equals(rowId, that.rowId) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventTimestamp, action, rowId, correlationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineAuditCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (eventTimestamp != null ? "eventTimestamp=" + eventTimestamp + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (rowId != null ? "rowId=" + rowId + ", " : "") +
            (correlationId != null ? "correlationId=" + correlationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
