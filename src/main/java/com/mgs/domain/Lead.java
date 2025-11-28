package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.LeadStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lead.
 */
@Entity
@Table(name = "lead")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lead extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeadStatus status;

    @Column(name = "estimated_value", precision = 21, scale = 2)
    private BigDecimal estimatedValue;

    @Size(max = 10)
    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "department", "billingAddress", "shippingAddress", "primaryContact" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "address", "ownerUser" }, allowSetters = true)
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private LeadSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "pipeline" }, allowSetters = true)
    private SubPipeline stage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User ownerUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lead id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Lead title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LeadStatus getStatus() {
        return this.status;
    }

    public Lead status(LeadStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public BigDecimal getEstimatedValue() {
        return this.estimatedValue;
    }

    public Lead estimatedValue(BigDecimal estimatedValue) {
        this.setEstimatedValue(estimatedValue);
        return this;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Lead currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotes() {
        return this.notes;
    }

    public Lead notes(String notes) {
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

    public Lead tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Lead customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Lead contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public LeadSource getSource() {
        return this.source;
    }

    public void setSource(LeadSource leadSource) {
        this.source = leadSource;
    }

    public Lead source(LeadSource leadSource) {
        this.setSource(leadSource);
        return this;
    }

    public Pipeline getPipeline() {
        return this.pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Lead pipeline(Pipeline pipeline) {
        this.setPipeline(pipeline);
        return this;
    }

    public SubPipeline getStage() {
        return this.stage;
    }

    public void setStage(SubPipeline subPipeline) {
        this.stage = subPipeline;
    }

    public Lead stage(SubPipeline subPipeline) {
        this.setStage(subPipeline);
        return this;
    }

    public User getOwnerUser() {
        return this.ownerUser;
    }

    public void setOwnerUser(User user) {
        this.ownerUser = user;
    }

    public Lead ownerUser(User user) {
        this.setOwnerUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lead)) {
            return false;
        }
        return getId() != null && getId().equals(((Lead) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lead{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", status='" + getStatus() + "'" +
            ", estimatedValue=" + getEstimatedValue() +
            ", currency='" + getCurrency() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
