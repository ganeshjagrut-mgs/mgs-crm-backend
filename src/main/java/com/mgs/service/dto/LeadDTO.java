package com.mgs.service.dto;

import com.mgs.domain.enumeration.LeadStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Lead} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LeadStatus status;

    private BigDecimal estimatedValue;

    @Size(max = 10)
    private String currency;

    private String notes;

    @NotNull
    private TenantDTO tenant;

    private CustomerDTO customer;

    private ContactDTO contact;

    private LeadSourceDTO source;

    private PipelineDTO pipeline;

    private SubPipelineDTO stage;

    @NotNull
    private UserDTO ownerUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public LeadSourceDTO getSource() {
        return source;
    }

    public void setSource(LeadSourceDTO source) {
        this.source = source;
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

    public UserDTO getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(UserDTO ownerUser) {
        this.ownerUser = ownerUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeadDTO)) {
            return false;
        }

        LeadDTO leadDTO = (LeadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeadDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", status='" + getStatus() + "'" +
            ", estimatedValue=" + getEstimatedValue() +
            ", currency='" + getCurrency() + "'" +
            ", notes='" + getNotes() + "'" +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            ", contact=" + getContact() +
            ", source=" + getSource() +
            ", pipeline=" + getPipeline() +
            ", stage=" + getStage() +
            ", ownerUser=" + getOwnerUser() +
            "}";
    }
}
