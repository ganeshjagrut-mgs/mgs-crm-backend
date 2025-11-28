package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.DealStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deal.
 */
@Entity
@Table(name = "deal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Deal extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "deal_number", length = 50, nullable = false)
    private String dealNumber;

    @Column(name = "deal_value", precision = 21, scale = 2)
    private BigDecimal dealValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DealStatus status;

    @Size(max = 10)
    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "close_date")
    private Instant closeDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant", "department", "billingAddress", "shippingAddress", "primaryContact" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "address", "ownerUser" }, allowSetters = true)
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "contact", "source", "pipeline", "stage", "ownerUser" }, allowSetters = true)
    private Lead lead;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealNumber() {
        return this.dealNumber;
    }

    public Deal dealNumber(String dealNumber) {
        this.setDealNumber(dealNumber);
        return this;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public BigDecimal getDealValue() {
        return this.dealValue;
    }

    public Deal dealValue(BigDecimal dealValue) {
        this.setDealValue(dealValue);
        return this;
    }

    public void setDealValue(BigDecimal dealValue) {
        this.dealValue = dealValue;
    }

    public DealStatus getStatus() {
        return this.status;
    }

    public Deal status(DealStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DealStatus status) {
        this.status = status;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Deal currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Deal startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCloseDate() {
        return this.closeDate;
    }

    public Deal closeDate(Instant closeDate) {
        this.setCloseDate(closeDate);
        return this;
    }

    public void setCloseDate(Instant closeDate) {
        this.closeDate = closeDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public Deal notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Deal tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Deal customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Deal contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public Lead getLead() {
        return this.lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Deal lead(Lead lead) {
        this.setLead(lead);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deal)) {
            return false;
        }
        return getId() != null && getId().equals(((Deal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deal{" +
            "id=" + getId() +
            ", dealNumber='" + getDealNumber() + "'" +
            ", dealValue=" + getDealValue() +
            ", status='" + getStatus() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", closeDate='" + getCloseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
