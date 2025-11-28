package com.mgs.service.dto;

import com.mgs.domain.enumeration.DealStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Deal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DealDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String dealNumber;

    private BigDecimal dealValue;

    @NotNull
    private DealStatus status;

    @Size(max = 10)
    private String currency;

    private Instant startDate;

    private Instant closeDate;

    private String notes;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private CustomerDTO customer;

    private ContactDTO contact;

    private LeadDTO lead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public BigDecimal getDealValue() {
        return dealValue;
    }

    public void setDealValue(BigDecimal dealValue) {
        this.dealValue = dealValue;
    }

    public DealStatus getStatus() {
        return status;
    }

    public void setStatus(DealStatus status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Instant closeDate) {
        this.closeDate = closeDate;
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

    public LeadDTO getLead() {
        return lead;
    }

    public void setLead(LeadDTO lead) {
        this.lead = lead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealDTO)) {
            return false;
        }

        DealDTO dealDTO = (DealDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dealDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealDTO{" +
            "id=" + getId() +
            ", dealNumber='" + getDealNumber() + "'" +
            ", dealValue=" + getDealValue() +
            ", status='" + getStatus() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", closeDate='" + getCloseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            ", contact=" + getContact() +
            ", lead=" + getLead() +
            "}";
    }
}
