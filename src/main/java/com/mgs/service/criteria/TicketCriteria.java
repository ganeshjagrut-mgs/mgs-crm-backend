package com.mgs.service.criteria;

import com.mgs.domain.enumeration.TicketPriority;
import com.mgs.domain.enumeration.TicketStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Ticket} entity. This class is used
 * in {@link com.mgs.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TicketPriority
     */
    public static class TicketPriorityFilter extends Filter<TicketPriority> {

        public TicketPriorityFilter() {}

        public TicketPriorityFilter(TicketPriorityFilter filter) {
            super(filter);
        }

        @Override
        public TicketPriorityFilter copy() {
            return new TicketPriorityFilter(this);
        }
    }

    /**
     * Class for filtering TicketStatus
     */
    public static class TicketStatusFilter extends Filter<TicketStatus> {

        public TicketStatusFilter() {}

        public TicketStatusFilter(TicketStatusFilter filter) {
            super(filter);
        }

        @Override
        public TicketStatusFilter copy() {
            return new TicketStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ticketNumber;

    private StringFilter subject;

    private StringFilter description;

    private TicketPriorityFilter priority;

    private TicketStatusFilter status;

    private LongFilter tenantId;

    private LongFilter customerId;

    private LongFilter contactId;

    private LongFilter assignedToId;

    private Boolean distinct;

    public TicketCriteria() {}

    public TicketCriteria(TicketCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ticketNumber = other.optionalTicketNumber().map(StringFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(TicketPriorityFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TicketStatusFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(LongFilter::copy).orElse(null);
        this.assignedToId = other.optionalAssignedToId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
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

    public StringFilter getTicketNumber() {
        return ticketNumber;
    }

    public Optional<StringFilter> optionalTicketNumber() {
        return Optional.ofNullable(ticketNumber);
    }

    public StringFilter ticketNumber() {
        if (ticketNumber == null) {
            setTicketNumber(new StringFilter());
        }
        return ticketNumber;
    }

    public void setTicketNumber(StringFilter ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public Optional<StringFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public StringFilter subject() {
        if (subject == null) {
            setSubject(new StringFilter());
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public TicketPriorityFilter getPriority() {
        return priority;
    }

    public Optional<TicketPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public TicketPriorityFilter priority() {
        if (priority == null) {
            setPriority(new TicketPriorityFilter());
        }
        return priority;
    }

    public void setPriority(TicketPriorityFilter priority) {
        this.priority = priority;
    }

    public TicketStatusFilter getStatus() {
        return status;
    }

    public Optional<TicketStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TicketStatusFilter status() {
        if (status == null) {
            setStatus(new TicketStatusFilter());
        }
        return status;
    }

    public void setStatus(TicketStatusFilter status) {
        this.status = status;
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

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public Optional<LongFilter> optionalContactId() {
        return Optional.ofNullable(contactId);
    }

    public LongFilter contactId() {
        if (contactId == null) {
            setContactId(new LongFilter());
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public Optional<LongFilter> optionalAssignedToId() {
        return Optional.ofNullable(assignedToId);
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            setAssignedToId(new LongFilter());
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
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
        final TicketCriteria that = (TicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ticketNumber, that.ticketNumber) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ticketNumber,
            subject,
            description,
            priority,
            status,
            tenantId,
            customerId,
            contactId,
            assignedToId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTicketNumber().map(f -> "ticketNumber=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalAssignedToId().map(f -> "assignedToId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
