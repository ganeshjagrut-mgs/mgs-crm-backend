package com.crm.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * TaskAudit - Audit trail for task changes
 */
@Entity
@Table(name = "task_audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "event_timestamp")
    private Instant eventTimestamp;

    @Column(name = "action")
    private String action;

    @Column(name = "row_id")
    private UUID rowId;

    @Lob
    @Column(name = "changes")
    private String changes;

    @Column(name = "correlation_id")
    private UUID correlationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaskAudit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTimestamp() {
        return this.eventTimestamp;
    }

    public TaskAudit eventTimestamp(Instant eventTimestamp) {
        this.setEventTimestamp(eventTimestamp);
        return this;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getAction() {
        return this.action;
    }

    public TaskAudit action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UUID getRowId() {
        return this.rowId;
    }

    public TaskAudit rowId(UUID rowId) {
        this.setRowId(rowId);
        return this;
    }

    public void setRowId(UUID rowId) {
        this.rowId = rowId;
    }

    public String getChanges() {
        return this.changes;
    }

    public TaskAudit changes(String changes) {
        this.setChanges(changes);
        return this;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public UUID getCorrelationId() {
        return this.correlationId;
    }

    public TaskAudit correlationId(UUID correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskAudit)) {
            return false;
        }
        return getId() != null && getId().equals(((TaskAudit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskAudit{" +
            "id=" + getId() +
            ", eventTimestamp='" + getEventTimestamp() + "'" +
            ", action='" + getAction() + "'" +
            ", rowId='" + getRowId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            "}";
    }
}
