package com.mgs.service.criteria;

import com.mgs.domain.enumeration.SubscriptionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.TenantSubscription} entity. This class is used
 * in {@link com.mgs.web.rest.TenantSubscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-subscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSubscriptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SubscriptionStatus
     */
    public static class SubscriptionStatusFilter extends Filter<SubscriptionStatus> {

        public SubscriptionStatusFilter() {}

        public SubscriptionStatusFilter(SubscriptionStatusFilter filter) {
            super(filter);
        }

        @Override
        public SubscriptionStatusFilter copy() {
            return new SubscriptionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SubscriptionStatusFilter status;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter trialEndDate;

    private InstantFilter lastRenewedAt;

    private InstantFilter nextBillingAt;

    private LongFilter tenantId;

    private LongFilter planId;

    private Boolean distinct;

    public TenantSubscriptionCriteria() {}

    public TenantSubscriptionCriteria(TenantSubscriptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(SubscriptionStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.trialEndDate = other.optionalTrialEndDate().map(LocalDateFilter::copy).orElse(null);
        this.lastRenewedAt = other.optionalLastRenewedAt().map(InstantFilter::copy).orElse(null);
        this.nextBillingAt = other.optionalNextBillingAt().map(InstantFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.planId = other.optionalPlanId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantSubscriptionCriteria copy() {
        return new TenantSubscriptionCriteria(this);
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

    public SubscriptionStatusFilter getStatus() {
        return status;
    }

    public Optional<SubscriptionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public SubscriptionStatusFilter status() {
        if (status == null) {
            setStatus(new SubscriptionStatusFilter());
        }
        return status;
    }

    public void setStatus(SubscriptionStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LocalDateFilter getTrialEndDate() {
        return trialEndDate;
    }

    public Optional<LocalDateFilter> optionalTrialEndDate() {
        return Optional.ofNullable(trialEndDate);
    }

    public LocalDateFilter trialEndDate() {
        if (trialEndDate == null) {
            setTrialEndDate(new LocalDateFilter());
        }
        return trialEndDate;
    }

    public void setTrialEndDate(LocalDateFilter trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public InstantFilter getLastRenewedAt() {
        return lastRenewedAt;
    }

    public Optional<InstantFilter> optionalLastRenewedAt() {
        return Optional.ofNullable(lastRenewedAt);
    }

    public InstantFilter lastRenewedAt() {
        if (lastRenewedAt == null) {
            setLastRenewedAt(new InstantFilter());
        }
        return lastRenewedAt;
    }

    public void setLastRenewedAt(InstantFilter lastRenewedAt) {
        this.lastRenewedAt = lastRenewedAt;
    }

    public InstantFilter getNextBillingAt() {
        return nextBillingAt;
    }

    public Optional<InstantFilter> optionalNextBillingAt() {
        return Optional.ofNullable(nextBillingAt);
    }

    public InstantFilter nextBillingAt() {
        if (nextBillingAt == null) {
            setNextBillingAt(new InstantFilter());
        }
        return nextBillingAt;
    }

    public void setNextBillingAt(InstantFilter nextBillingAt) {
        this.nextBillingAt = nextBillingAt;
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

    public LongFilter getPlanId() {
        return planId;
    }

    public Optional<LongFilter> optionalPlanId() {
        return Optional.ofNullable(planId);
    }

    public LongFilter planId() {
        if (planId == null) {
            setPlanId(new LongFilter());
        }
        return planId;
    }

    public void setPlanId(LongFilter planId) {
        this.planId = planId;
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
        final TenantSubscriptionCriteria that = (TenantSubscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(trialEndDate, that.trialEndDate) &&
            Objects.equals(lastRenewedAt, that.lastRenewedAt) &&
            Objects.equals(nextBillingAt, that.nextBillingAt) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, startDate, endDate, trialEndDate, lastRenewedAt, nextBillingAt, tenantId, planId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSubscriptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalTrialEndDate().map(f -> "trialEndDate=" + f + ", ").orElse("") +
            optionalLastRenewedAt().map(f -> "lastRenewedAt=" + f + ", ").orElse("") +
            optionalNextBillingAt().map(f -> "nextBillingAt=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalPlanId().map(f -> "planId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
