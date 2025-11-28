package com.mgs.service.dto;

import com.mgs.domain.enumeration.ComplaintPriority;
import com.mgs.domain.enumeration.ComplaintSource;
import com.mgs.domain.enumeration.ComplaintStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Complaint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComplaintDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String complaintNumber;

    @NotNull
    private String subject;

    private String description;

    @NotNull
    private ComplaintPriority priority;

    @NotNull
    private ComplaintStatus status;

    private ComplaintSource source;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private CustomerDTO customer;

    private ContactDTO contact;

    private ComplaintCategoryDTO category;

    private PipelineDTO pipeline;

    private SubPipelineDTO stage;

    private DepartmentDTO assignedDepartment;

    private UserDTO assignedUser;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintPriority getPriority() {
        return priority;
    }

    public void setPriority(ComplaintPriority priority) {
        this.priority = priority;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public ComplaintSource getSource() {
        return source;
    }

    public void setSource(ComplaintSource source) {
        this.source = source;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public ComplaintCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategoryDTO category) {
        this.category = category;
    }

    public PipelineDTO getPipeline() {
        return pipeline;
    }

    public void setPipeline(PipelineDTO pipeline) {
        this.pipeline = pipeline;
    }

    public SubPipelineDTO getStage() {
        return stage;
    }

    public void setStage(SubPipelineDTO stage) {
        this.stage = stage;
    }

    public DepartmentDTO getAssignedDepartment() {
        return assignedDepartment;
    }

    public void setAssignedDepartment(DepartmentDTO assignedDepartment) {
        this.assignedDepartment = assignedDepartment;
    }

    public UserDTO getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserDTO assignedUser) {
        this.assignedUser = assignedUser;
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
            ", subject='" + getSubject() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", status='" + getStatus() + "'" +
            ", source='" + getSource() + "'" +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            ", contact=" + getContact() +
            ", category=" + getCategory() +
            ", pipeline=" + getPipeline() +
            ", stage=" + getStage() +
            ", assignedDepartment=" + getAssignedDepartment() +
            ", assignedUser=" + getAssignedUser() +
            "}";
    }
}
