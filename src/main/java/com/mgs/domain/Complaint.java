package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.ComplaintPriority;
import com.mgs.domain.enumeration.ComplaintSource;
import com.mgs.domain.enumeration.ComplaintStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Complaint.
 */
@Entity
@Table(name = "complaint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Complaint extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "complaint_number", length = 50, nullable = false)
    private String complaintNumber;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private ComplaintPriority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ComplaintStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private ComplaintSource source;

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
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ComplaintCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private Pipeline pipeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "pipeline" }, allowSetters = true)
    private SubPipeline stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "headUser" }, allowSetters = true)
    private Department assignedDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User assignedUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Complaint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintNumber() {
        return this.complaintNumber;
    }

    public Complaint complaintNumber(String complaintNumber) {
        this.setComplaintNumber(complaintNumber);
        return this;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public String getSubject() {
        return this.subject;
    }

    public Complaint subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public Complaint description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintPriority getPriority() {
        return this.priority;
    }

    public Complaint priority(ComplaintPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(ComplaintPriority priority) {
        this.priority = priority;
    }

    public ComplaintStatus getStatus() {
        return this.status;
    }

    public Complaint status(ComplaintStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public ComplaintSource getSource() {
        return this.source;
    }

    public Complaint source(ComplaintSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(ComplaintSource source) {
        this.source = source;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Complaint tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Complaint customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Complaint contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public ComplaintCategory getCategory() {
        return this.category;
    }

    public void setCategory(ComplaintCategory complaintCategory) {
        this.category = complaintCategory;
    }

    public Complaint category(ComplaintCategory complaintCategory) {
        this.setCategory(complaintCategory);
        return this;
    }

    public Pipeline getPipeline() {
        return this.pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Complaint pipeline(Pipeline pipeline) {
        this.setPipeline(pipeline);
        return this;
    }

    public SubPipeline getStage() {
        return this.stage;
    }

    public void setStage(SubPipeline subPipeline) {
        this.stage = subPipeline;
    }

    public Complaint stage(SubPipeline subPipeline) {
        this.setStage(subPipeline);
        return this;
    }

    public Department getAssignedDepartment() {
        return this.assignedDepartment;
    }

    public void setAssignedDepartment(Department department) {
        this.assignedDepartment = department;
    }

    public Complaint assignedDepartment(Department department) {
        this.setAssignedDepartment(department);
        return this;
    }

    public User getAssignedUser() {
        return this.assignedUser;
    }

    public void setAssignedUser(User user) {
        this.assignedUser = user;
    }

    public Complaint assignedUser(User user) {
        this.setAssignedUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complaint)) {
            return false;
        }
        return getId() != null && getId().equals(((Complaint) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Complaint{" +
            "id=" + getId() +
            ", complaintNumber='" + getComplaintNumber() + "'" +
            ", subject='" + getSubject() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", status='" + getStatus() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
