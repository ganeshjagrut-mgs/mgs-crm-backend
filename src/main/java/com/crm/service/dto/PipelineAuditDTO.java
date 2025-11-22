package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.crm.domain.PipelineAudit} entity.
 */
@Schema(description = "PipelineAudit - Audit trail for pipeline changes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelineAuditDTO implements Serializable {

    private Long id;

    private Instant eventTimestamp;

    private String action;

    private UUID rowId;

    @Lob
    private String changes;

    private UUID correlationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UUID getRowId() {
        return rowId;
    }

    public void setRowId(UUID rowId) {
        this.rowId = rowId;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PipelineAuditDTO)) {
            return false;
        }

        PipelineAuditDTO pipelineAuditDTO = (PipelineAuditDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pipelineAuditDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelineAuditDTO{" +
            "id=" + getId() +
            ", eventTimestamp='" + getEventTimestamp() + "'" +
            ", action='" + getAction() + "'" +
            ", rowId='" + getRowId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            "}";
    }
}
