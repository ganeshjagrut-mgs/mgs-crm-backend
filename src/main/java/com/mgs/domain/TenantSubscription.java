package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantSubscription.
 */
@Entity
@Table(name = "tenant_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSubscription extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "trial_end_date")
    private LocalDate trialEndDate;

    @Column(name = "last_renewed_at")
    private Instant lastRenewedAt;

    @Column(name = "next_billing_at")
    private Instant nextBillingAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    private Plan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public TenantSubscription status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public TenantSubscription startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public TenantSubscription endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getTrialEndDate() {
        return this.trialEndDate;
    }

    public TenantSubscription trialEndDate(LocalDate trialEndDate) {
        this.setTrialEndDate(trialEndDate);
        return this;
    }

    public void setTrialEndDate(LocalDate trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public Instant getLastRenewedAt() {
        return this.lastRenewedAt;
    }

    public TenantSubscription lastRenewedAt(Instant lastRenewedAt) {
        this.setLastRenewedAt(lastRenewedAt);
        return this;
    }

    public void setLastRenewedAt(Instant lastRenewedAt) {
        this.lastRenewedAt = lastRenewedAt;
    }

    public Instant getNextBillingAt() {
        return this.nextBillingAt;
    }

    public TenantSubscription nextBillingAt(Instant nextBillingAt) {
        this.setNextBillingAt(nextBillingAt);
        return this;
    }

    public void setNextBillingAt(Instant nextBillingAt) {
        this.nextBillingAt = nextBillingAt;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantSubscription tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public TenantSubscription plan(Plan plan) {
        this.setPlan(plan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSubscription{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", trialEndDate='" + getTrialEndDate() + "'" +
            ", lastRenewedAt='" + getLastRenewedAt() + "'" +
            ", nextBillingAt='" + getNextBillingAt() + "'" +
            "}";
    }
}
