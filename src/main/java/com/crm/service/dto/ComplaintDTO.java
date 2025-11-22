package com.crm.service.dto;

import com.crm.domain.enumeration.ComplaintStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.crm.domain.Complaint} entity.
 */
@Schema(description = "Complaint - Customer complaints")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComplaintDTO implements Serializable {

    private Long id;

    @NotNull
    private String complaintNumber;

    @NotNull
    private Instant complaintDate;

    private String recordNumbers;

    private String customerContactNumber;

    private String customerContactEmail;

    @Lob
    private String complaintDescription;

    private Instant expectedClosureDate;

    @Lob
    private String rootCause;

    private ComplaintStatus complaintStatus;

    @Lob
    private String correctiveAction;

    @Lob
    private String preventiveAction;

    private Instant complaintClosureDate;

    private CustomerDTO customerName;

    private MasterStaticTypeDTO complaintRelatedTo;

    private MasterStaticTypeDTO typeOfComplaint;

    private Set<UserDTO> complaintRelatedPersons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public Instant getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(Instant complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getRecordNumbers() {
        return recordNumbers;
    }

    public void setRecordNumbers(String recordNumbers) {
        this.recordNumbers = recordNumbers;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerContactEmail() {
        return customerContactEmail;
    }

    public void setCustomerContactEmail(String customerContactEmail) {
        this.customerContactEmail = customerContactEmail;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public Instant getExpectedClosureDate() {
        return expectedClosureDate;
    }

    public void setExpectedClosureDate(Instant expectedClosureDate) {
        this.expectedClosureDate = expectedClosureDate;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public ComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getCorrectiveAction() {
        return correctiveAction;
    }

    public void setCorrectiveAction(String correctiveAction) {
        this.correctiveAction = correctiveAction;
    }

    public String getPreventiveAction() {
        return preventiveAction;
    }

    public void setPreventiveAction(String preventiveAction) {
        this.preventiveAction = preventiveAction;
    }

    public Instant getComplaintClosureDate() {
        return complaintClosureDate;
    }

    public void setComplaintClosureDate(Instant complaintClosureDate) {
        this.complaintClosureDate = complaintClosureDate;
    }

    public CustomerDTO getCustomerName() {
        return customerName;
    }

    public void setCustomerName(CustomerDTO customerName) {
        this.customerName = customerName;
    }

    public MasterStaticTypeDTO getComplaintRelatedTo() {
        return complaintRelatedTo;
    }

    public void setComplaintRelatedTo(MasterStaticTypeDTO complaintRelatedTo) {
        this.complaintRelatedTo = complaintRelatedTo;
    }

    public MasterStaticTypeDTO getTypeOfComplaint() {
        return typeOfComplaint;
    }

    public void setTypeOfComplaint(MasterStaticTypeDTO typeOfComplaint) {
        this.typeOfComplaint = typeOfComplaint;
    }

    public Set<UserDTO> getComplaintRelatedPersons() {
        return complaintRelatedPersons;
    }

    public void setComplaintRelatedPersons(Set<UserDTO> complaintRelatedPersons) {
        this.complaintRelatedPersons = complaintRelatedPersons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplaintDTO)) {
            return false;
        }

        ComplaintDTO complaintDTO = (ComplaintDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, complaintDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplaintDTO{" +
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
            ", customerName=" + getCustomerName() +
            ", complaintRelatedTo=" + getComplaintRelatedTo() +
            ", typeOfComplaint=" + getTypeOfComplaint() +
            ", complaintRelatedPersons=" + getComplaintRelatedPersons() +
            "}";
    }
}
