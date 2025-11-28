package com.mgs.service.dto;

import com.mgs.domain.enumeration.SubscriptionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.TenantSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private SubscriptionStatus status;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate trialEndDate;

    private Instant lastRenewedAt;

    private Instant nextBillingAt;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private PlanDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(LocalDate trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public Instant getLastRenewedAt() {
        return lastRenewedAt;
    }

    public void setLastRenewedAt(Instant lastRenewedAt) {
        this.lastRenewedAt = lastRenewedAt;
    }

    public Instant getNextBillingAt() {
        return nextBillingAt;
    }

    public void setNextBillingAt(Instant nextBillingAt) {
        this.nextBillingAt = nextBillingAt;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantSubscriptionDTO)) {
            return false;
        }

        TenantSubscriptionDTO tenantSubscriptionDTO = (TenantSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSubscriptionDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", trialEndDate='" + getTrialEndDate() + "'" +
            ", lastRenewedAt='" + getLastRenewedAt() + "'" +
            ", nextBillingAt='" + getNextBillingAt() + "'" +
            ", tenant=" + getTenant() +
            ", plan=" + getPlan() +
            "}";
    }
}
