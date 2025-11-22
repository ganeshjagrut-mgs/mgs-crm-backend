package com.crm.domain;

import com.crm.domain.enumeration.ComplaintStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Complaint - Customer complaints
 */
@Entity
@Table(name = "complaint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "complaint_number", nullable = false, unique = true)
    private String complaintNumber;

    @NotNull
    @Column(name = "complaint_date", nullable = false)
    private Instant complaintDate;

    @Column(name = "record_numbers")
    private String recordNumbers;

    @Column(name = "customer_contact_number")
    private String customerContactNumber;

    @Column(name = "customer_contact_email")
    private String customerContactEmail;

    @Lob
    @Column(name = "complaint_description")
    private String complaintDescription;

    @Column(name = "expected_closure_date")
    private Instant expectedClosureDate;

    @Lob
    @Column(name = "root_cause")
    private String rootCause;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_status")
    private ComplaintStatus complaintStatus;

    @Lob
    @Column(name = "corrective_action", nullable = false)
    private String correctiveAction;

    @Lob
    @Column(name = "preventive_action")
    private String preventiveAction;

    @Column(name = "complaint_closure_date")
    private Instant complaintClosureDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "addresses",
            "company",
            "user",
            "customerType",
            "customerStatus",
            "ownershipType",
            "industryType",
            "customerCategory",
            "paymentTerms",
            "invoiceFrequency",
            "gstTreatment",
            "outstandingPerson",
            "department",
            "tenat",
            "masterCategories",
        },
        allowSetters = true
    )
    private Customer customerName;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType complaintRelatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType typeOfComplaint;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_complaint__complaint_related_persons",
        joinColumns = @JoinColumn(name = "complaint_id"),
        inverseJoinColumns = @JoinColumn(name = "complaint_related_persons_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> complaintRelatedPersons = new HashSet<>();

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

    public Instant getComplaintDate() {
        return this.complaintDate;
    }

    public Complaint complaintDate(Instant complaintDate) {
        this.setComplaintDate(complaintDate);
        return this;
    }

    public void setComplaintDate(Instant complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getRecordNumbers() {
        return this.recordNumbers;
    }

    public Complaint recordNumbers(String recordNumbers) {
        this.setRecordNumbers(recordNumbers);
        return this;
    }

    public void setRecordNumbers(String recordNumbers) {
        this.recordNumbers = recordNumbers;
    }

    public String getCustomerContactNumber() {
        return this.customerContactNumber;
    }

    public Complaint customerContactNumber(String customerContactNumber) {
        this.setCustomerContactNumber(customerContactNumber);
        return this;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerContactEmail() {
        return this.customerContactEmail;
    }

    public Complaint customerContactEmail(String customerContactEmail) {
        this.setCustomerContactEmail(customerContactEmail);
        return this;
    }

    public void setCustomerContactEmail(String customerContactEmail) {
        this.customerContactEmail = customerContactEmail;
    }

    public String getComplaintDescription() {
        return this.complaintDescription;
    }

    public Complaint complaintDescription(String complaintDescription) {
        this.setComplaintDescription(complaintDescription);
        return this;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public Instant getExpectedClosureDate() {
        return this.expectedClosureDate;
    }

    public Complaint expectedClosureDate(Instant expectedClosureDate) {
        this.setExpectedClosureDate(expectedClosureDate);
        return this;
    }

    public void setExpectedClosureDate(Instant expectedClosureDate) {
        this.expectedClosureDate = expectedClosureDate;
    }

    public String getRootCause() {
        return this.rootCause;
    }

    public Complaint rootCause(String rootCause) {
        this.setRootCause(rootCause);
        return this;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public ComplaintStatus getComplaintStatus() {
        return this.complaintStatus;
    }

    public Complaint complaintStatus(ComplaintStatus complaintStatus) {
        this.setComplaintStatus(complaintStatus);
        return this;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getCorrectiveAction() {
        return this.correctiveAction;
    }

    public Complaint correctiveAction(String correctiveAction) {
        this.setCorrectiveAction(correctiveAction);
        return this;
    }

    public void setCorrectiveAction(String correctiveAction) {
        this.correctiveAction = correctiveAction;
    }

    public String getPreventiveAction() {
        return this.preventiveAction;
    }

    public Complaint preventiveAction(String preventiveAction) {
        this.setPreventiveAction(preventiveAction);
        return this;
    }

    public void setPreventiveAction(String preventiveAction) {
        this.preventiveAction = preventiveAction;
    }

    public Instant getComplaintClosureDate() {
        return this.complaintClosureDate;
    }

    public Complaint complaintClosureDate(Instant complaintClosureDate) {
        this.setComplaintClosureDate(complaintClosureDate);
        return this;
    }

    public void setComplaintClosureDate(Instant complaintClosureDate) {
        this.complaintClosureDate = complaintClosureDate;
    }

    public Customer getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(Customer customer) {
        this.customerName = customer;
    }

    public Complaint customerName(Customer customer) {
        this.setCustomerName(customer);
        return this;
    }

    public MasterStaticType getComplaintRelatedTo() {
        return this.complaintRelatedTo;
    }

    public void setComplaintRelatedTo(MasterStaticType masterStaticType) {
        this.complaintRelatedTo = masterStaticType;
    }

    public Complaint complaintRelatedTo(MasterStaticType masterStaticType) {
        this.setComplaintRelatedTo(masterStaticType);
        return this;
    }

    public MasterStaticType getTypeOfComplaint() {
        return this.typeOfComplaint;
    }

    public void setTypeOfComplaint(MasterStaticType masterStaticType) {
        this.typeOfComplaint = masterStaticType;
    }

    public Complaint typeOfComplaint(MasterStaticType masterStaticType) {
        this.setTypeOfComplaint(masterStaticType);
        return this;
    }

    public Set<User> getComplaintRelatedPersons() {
        return this.complaintRelatedPersons;
    }

    public void setComplaintRelatedPersons(Set<User> users) {
        this.complaintRelatedPersons = users;
    }

    public Complaint complaintRelatedPersons(Set<User> users) {
        this.setComplaintRelatedPersons(users);
        return this;
    }

    public Complaint addComplaintRelatedPersons(User user) {
        this.complaintRelatedPersons.add(user);
        return this;
    }

    public Complaint removeComplaintRelatedPersons(User user) {
        this.complaintRelatedPersons.remove(user);
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
            ", complaintDate='" + getComplaintDate() + "'" +
            ", recordNumbers='" + getRecordNumbers() + "'" +
            ", customerContactNumber='" + getCustomerContactNumber() + "'" +
            ", customerContactEmail='" + getCustomerContactEmail() + "'" +
            ", complaintDescription='" + getComplaintDescription() + "'" +
            ", expectedClosureDate='" + getExpectedClosureDate() + "'" +
            ", rootCause='" + getRootCause() + "'" +
            ", complaintStatus='" + getComplaintStatus() + "'" +
            ", correctiveAction='" + getCorrectiveAction() + "'" +
            ", preventiveAction='" + getPreventiveAction() + "'" +
            ", complaintClosureDate='" + getComplaintClosureDate() + "'" +
            "}";
    }
}
