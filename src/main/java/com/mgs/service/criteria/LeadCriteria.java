package com.mgs.service.criteria;

import com.mgs.domain.enumeration.LeadStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Lead} entity. This class is used
 * in {@link com.mgs.web.rest.LeadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LeadStatus
     */
    public static class LeadStatusFilter extends Filter<LeadStatus> {

        public LeadStatusFilter() {}

        public LeadStatusFilter(LeadStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeadStatusFilter copy() {
            return new LeadStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private LeadStatusFilter status;

    private BigDecimalFilter estimatedValue;

    private StringFilter currency;

    private StringFilter notes;

    private LongFilter tenantId;

    private LongFilter customerId;

    private LongFilter contactId;

    private LongFilter sourceId;

    private LongFilter pipelineId;

    private LongFilter stageId;

    private LongFilter ownerUserId;

    private Boolean distinct;

    public LeadCriteria() {}

    public LeadCriteria(LeadCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(LeadStatusFilter::copy).orElse(null);
        this.estimatedValue = other.optionalEstimatedValue().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(LongFilter::copy).orElse(null);
        this.sourceId = other.optionalSourceId().map(LongFilter::copy).orElse(null);
        this.pipelineId = other.optionalPipelineId().map(LongFilter::copy).orElse(null);
        this.stageId = other.optionalStageId().map(LongFilter::copy).orElse(null);
        this.ownerUserId = other.optionalOwnerUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LeadCriteria copy() {
        return new LeadCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public LeadStatusFilter getStatus() {
        return status;
    }

    public Optional<LeadStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LeadStatusFilter status() {
        if (status == null) {
            setStatus(new LeadStatusFilter());
        }
        return status;
    }

    public void setStatus(LeadStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getEstimatedValue() {
        return estimatedValue;
    }

    public Optional<BigDecimalFilter> optionalEstimatedValue() {
        return Optional.ofNullable(estimatedValue);
    }

    public BigDecimalFilter estimatedValue() {
        if (estimatedValue == null) {
            setEstimatedValue(new BigDecimalFilter());
        }
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimalFilter estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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

    public LongFilter getSourceId() {
        return sourceId;
    }

    public Optional<LongFilter> optionalSourceId() {
        return Optional.ofNullable(sourceId);
    }

    public LongFilter sourceId() {
        if (sourceId == null) {
            setSourceId(new LongFilter());
        }
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
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

    public LongFilter getOwnerUserId() {
        return ownerUserId;
    }

    public Optional<LongFilter> optionalOwnerUserId() {
        return Optional.ofNullable(ownerUserId);
    }

    public LongFilter ownerUserId() {
        if (ownerUserId == null) {
            setOwnerUserId(new LongFilter());
        }
        return ownerUserId;
    }

    public void setOwnerUserId(LongFilter ownerUserId) {
        this.ownerUserId = ownerUserId;
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
        final LeadCriteria that = (LeadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(status, that.status) &&
            Objects.equals(estimatedValue, that.estimatedValue) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(pipelineId, that.pipelineId) &&
            Objects.equals(stageId, that.stageId) &&
            Objects.equals(ownerUserId, that.ownerUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            status,
            estimatedValue,
            currency,
            notes,
            tenantId,
            customerId,
            contactId,
            sourceId,
            pipelineId,
            stageId,
            ownerUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeadCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalEstimatedValue().map(f -> "estimatedValue=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalSourceId().map(f -> "sourceId=" + f + ", ").orElse("") +
            optionalPipelineId().map(f -> "pipelineId=" + f + ", ").orElse("") +
            optionalStageId().map(f -> "stageId=" + f + ", ").orElse("") +
            optionalOwnerUserId().map(f -> "ownerUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
