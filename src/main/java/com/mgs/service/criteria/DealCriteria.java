package com.mgs.service.criteria;

import com.mgs.domain.enumeration.DealStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Deal} entity. This class is used
 * in {@link com.mgs.web.rest.DealResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /deals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DealStatus
     */
    public static class DealStatusFilter extends Filter<DealStatus> {

        public DealStatusFilter() {}

        public DealStatusFilter(DealStatusFilter filter) {
            super(filter);
        }

        @Override
        public DealStatusFilter copy() {
            return new DealStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dealNumber;

    private BigDecimalFilter dealValue;

    private DealStatusFilter status;

    private StringFilter currency;

    private InstantFilter startDate;

    private InstantFilter closeDate;

    private StringFilter notes;

    private LongFilter tenantId;

    private LongFilter customerId;

    private LongFilter contactId;

    private LongFilter leadId;

    private Boolean distinct;

    public DealCriteria() {}

    public DealCriteria(DealCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dealNumber = other.optionalDealNumber().map(StringFilter::copy).orElse(null);
        this.dealValue = other.optionalDealValue().map(BigDecimalFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DealStatusFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.closeDate = other.optionalCloseDate().map(InstantFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(LongFilter::copy).orElse(null);
        this.leadId = other.optionalLeadId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DealCriteria copy() {
        return new DealCriteria(this);
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

    public StringFilter getDealNumber() {
        return dealNumber;
    }

    public Optional<StringFilter> optionalDealNumber() {
        return Optional.ofNullable(dealNumber);
    }

    public StringFilter dealNumber() {
        if (dealNumber == null) {
            setDealNumber(new StringFilter());
        }
        return dealNumber;
    }

    public void setDealNumber(StringFilter dealNumber) {
        this.dealNumber = dealNumber;
    }

    public BigDecimalFilter getDealValue() {
        return dealValue;
    }

    public Optional<BigDecimalFilter> optionalDealValue() {
        return Optional.ofNullable(dealValue);
    }

    public BigDecimalFilter dealValue() {
        if (dealValue == null) {
            setDealValue(new BigDecimalFilter());
        }
        return dealValue;
    }

    public void setDealValue(BigDecimalFilter dealValue) {
        this.dealValue = dealValue;
    }

    public DealStatusFilter getStatus() {
        return status;
    }

    public Optional<DealStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DealStatusFilter status() {
        if (status == null) {
            setStatus(new DealStatusFilter());
        }
        return status;
    }

    public void setStatus(DealStatusFilter status) {
        this.status = status;
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

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getCloseDate() {
        return closeDate;
    }

    public Optional<InstantFilter> optionalCloseDate() {
        return Optional.ofNullable(closeDate);
    }

    public InstantFilter closeDate() {
        if (closeDate == null) {
            setCloseDate(new InstantFilter());
        }
        return closeDate;
    }

    public void setCloseDate(InstantFilter closeDate) {
        this.closeDate = closeDate;
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

    public LongFilter getLeadId() {
        return leadId;
    }

    public Optional<LongFilter> optionalLeadId() {
        return Optional.ofNullable(leadId);
    }

    public LongFilter leadId() {
        if (leadId == null) {
            setLeadId(new LongFilter());
        }
        return leadId;
    }

    public void setLeadId(LongFilter leadId) {
        this.leadId = leadId;
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
        final DealCriteria that = (DealCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dealNumber, that.dealNumber) &&
            Objects.equals(dealValue, that.dealValue) &&
            Objects.equals(status, that.status) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(closeDate, that.closeDate) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(leadId, that.leadId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dealNumber,
            dealValue,
            status,
            currency,
            startDate,
            closeDate,
            notes,
            tenantId,
            customerId,
            contactId,
            leadId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDealNumber().map(f -> "dealNumber=" + f + ", ").orElse("") +
            optionalDealValue().map(f -> "dealValue=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalCloseDate().map(f -> "closeDate=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalLeadId().map(f -> "leadId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
