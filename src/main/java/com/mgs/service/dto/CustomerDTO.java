package com.mgs.service.dto;

import com.mgs.domain.enumeration.CustomerStatus;
import com.mgs.domain.enumeration.CustomerType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    private Long id;

    private String name;

    private CustomerType customerType;

    private CustomerStatus status;

    @Size(max = 255)
    private String segment;

    @Size(max = 255)
    private String industry;

    @NotNull
    private TenantDTO tenant;

    private DepartmentDTO department;

    private AddressDTO billingAddress;

    private AddressDTO shippingAddress;

    private ContactDTO primaryContact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public AddressDTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressDTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public AddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ContactDTO getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(ContactDTO primaryContact) {
        this.primaryContact = primaryContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", status='" + getStatus() + "'" +
            ", segment='" + getSegment() + "'" +
            ", industry='" + getIndustry() + "'" +
            ", tenant=" + getTenant() +
            ", department=" + getDepartment() +
            ", billingAddress=" + getBillingAddress() +
            ", shippingAddress=" + getShippingAddress() +
            ", primaryContact=" + getPrimaryContact() +
            "}";
    }
}
