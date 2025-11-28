package com.mgs.service.criteria;

import com.mgs.domain.enumeration.ComplaintPriority;
import com.mgs.domain.enumeration.ComplaintSource;
import com.mgs.domain.enumeration.ComplaintStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Complaint} entity. This class is used
 * in {@link com.mgs.web.rest.ComplaintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /complaints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComplaintCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ComplaintPriority
     */
    public static class ComplaintPriorityFilter extends Filter<ComplaintPriority> {

        public ComplaintPriorityFilter() {}

        public ComplaintPriorityFilter(ComplaintPriorityFilter filter) {
            super(filter);
        }

        @Override
        public ComplaintPriorityFilter copy() {
            return new ComplaintPriorityFilter(this);
        }
    }

    /**
     * Class for filtering ComplaintStatus
     */
    public static class ComplaintStatusFilter extends Filter<ComplaintStatus> {

        public ComplaintStatusFilter() {}

        public ComplaintStatusFilter(ComplaintStatusFilter filter) {
            super(filter);
        }

        @Override
        public ComplaintStatusFilter copy() {
            return new ComplaintStatusFilter(this);
        }
    }

    /**
     * Class for filtering ComplaintSource
     */
    public static class ComplaintSourceFilter extends Filter<ComplaintSource> {

        public ComplaintSourceFilter() {}

        public ComplaintSourceFilter(ComplaintSourceFilter filter) {
            super(filter);
        }

        @Override
        public ComplaintSourceFilter copy() {
            return new ComplaintSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter complaintNumber;

    private StringFilter subject;

    private StringFilter description;

    private ComplaintPriorityFilter priority;

    private ComplaintStatusFilter status;

    private ComplaintSourceFilter source;

    private LongFilter tenantId;

    private LongFilter customerId;

    private LongFilter contactId;

    private LongFilter categoryId;

    private LongFilter pipelineId;

    private LongFilter stageId;

    private LongFilter assignedDepartmentId;

    private LongFilter assignedUserId;

    private Boolean distinct;

    public ComplaintCriteria() {}

    public ComplaintCriteria(ComplaintCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.complaintNumber = other.optionalComplaintNumber().map(StringFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(ComplaintPriorityFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ComplaintStatusFilter::copy).orElse(null);
        this.source = other.optionalSource().map(ComplaintSourceFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.pipelineId = other.optionalPipelineId().map(LongFilter::copy).orElse(null);
        this.stageId = other.optionalStageId().map(LongFilter::copy).orElse(null);
        this.assignedDepartmentId = other.optionalAssignedDepartmentId().map(LongFilter::copy).orElse(null);
        this.assignedUserId = other.optionalAssignedUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ComplaintCriteria copy() {
        return new ComplaintCriteria(this);
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

    public StringFilter getComplaintNumber() {
        return complaintNumber;
    }

    public Optional<StringFilter> optionalComplaintNumber() {
        return Optional.ofNullable(complaintNumber);
    }

    public StringFilter complaintNumber() {
        if (complaintNumber == null) {
            setComplaintNumber(new StringFilter());
        }
        return complaintNumber;
    }

    public void setComplaintNumber(StringFilter complaintNumber) {
        this.complaintNumber = complaintNumber;
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

    public ComplaintPriorityFilter getPriority() {
        return priority;
    }

    public Optional<ComplaintPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public ComplaintPriorityFilter priority() {
        if (priority == null) {
            setPriority(new ComplaintPriorityFilter());
        }
        return priority;
    }

    public void setPriority(ComplaintPriorityFilter priority) {
        this.priority = priority;
    }

    public ComplaintStatusFilter getStatus() {
        return status;
    }

    public Optional<ComplaintStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ComplaintStatusFilter status() {
        if (status == null) {
            setStatus(new ComplaintStatusFilter());
        }
        return status;
    }

    public void setStatus(ComplaintStatusFilter status) {
        this.status = status;
    }

    public ComplaintSourceFilter getSource() {
        return source;
    }

    public Optional<ComplaintSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public ComplaintSourceFilter source() {
        if (source == null) {
            setSource(new ComplaintSourceFilter());
        }
        return source;
    }

    public void setSource(ComplaintSourceFilter source) {
        this.source = source;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getPipelineId() {
        return pipelineId;
    }

    public Optional<LongFilter> optionalPipelineId() {
        return Optional.ofNullable(pipelineId);
    }

    public LongFilter pipelineId() {
        if (pipelineId == null) {
            setPipelineId(new LongFilter());
        }
        return pipelineId;
    }

    public void setPipelineId(LongFilter pipelineId) {
        this.pipelineId = pipelineId;
    }

    public LongFilter getStageId() {
        return stageId;
    }

    public Optional<LongFilter> optionalStageId() {
        return Optional.ofNullable(stageId);
    }

    public LongFilter stageId() {
        if (stageId == null) {
            setStageId(new LongFilter());
        }
        return stageId;
    }

    public void setStageId(LongFilter stageId) {
        this.stageId = stageId;
    }

    public LongFilter getAssignedDepartmentId() {
        return assignedDepartmentId;
    }

    public Optional<LongFilter> optionalAssignedDepartmentId() {
        return Optional.ofNullable(assignedDepartmentId);
    }

    public LongFilter assignedDepartmentId() {
        if (assignedDepartmentId == null) {
            setAssignedDepartmentId(new LongFilter());
        }
        return assignedDepartmentId;
    }

    public void setAssignedDepartmentId(LongFilter assignedDepartmentId) {
        this.assignedDepartmentId = assignedDepartmentId;
    }

    public LongFilter getAssignedUserId() {
        return assignedUserId;
    }

    public Optional<LongFilter> optionalAssignedUserId() {
        return Optional.ofNullable(assignedUserId);
    }

    public LongFilter assignedUserId() {
        if (assignedUserId == null) {
            setAssignedUserId(new LongFilter());
        }
        return assignedUserId;
    }

    public void setAssignedUserId(LongFilter assignedUserId) {
        this.assignedUserId = assignedUserId;
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
        final ComplaintCriteria that = (ComplaintCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(complaintNumber, that.complaintNumber) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(description, that.description) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(status, that.status) &&
            Objects.equals(source, that.source) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(pipelineId, that.pipelineId) &&
            Objects.equals(stageId, that.stageId) &&
            Objects.equals(assignedDepartmentId, that.assignedDepartmentId) &&
            Objects.equals(assignedUserId, that.assignedUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            complaintNumber,
            subject,
            description,
            priority,
            status,
            source,
            tenantId,
            customerId,
            contactId,
            categoryId,
            pipelineId,
            stageId,
            assignedDepartmentId,
            assignedUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplaintCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalComplaintNumber().map(f -> "complaintNumber=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalPipelineId().map(f -> "pipelineId=" + f + ", ").orElse("") +
            optionalStageId().map(f -> "stageId=" + f + ", ").orElse("") +
            optionalAssignedDepartmentId().map(f -> "assignedDepartmentId=" + f + ", ").orElse("") +
            optionalAssignedUserId().map(f -> "assignedUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
