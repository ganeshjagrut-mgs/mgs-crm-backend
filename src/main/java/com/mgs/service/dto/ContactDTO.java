package com.mgs.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Contact} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactDTO implements Serializable {

    private Long id;

    @Schema(description = "PII – encrypted long string", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Schema(description = "PII – encrypted long string", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Schema(description = "PII – encrypted long string")
    private String email;

    @Schema(description = "PII – encrypted long string")
    private String phone;

    @Schema(description = "May contain PII – encrypted long string")
    private String notes;

    @NotNull
    private TenantDTO tenant;

    private CustomerDTO customer;

    private AddressDTO address;

    @NotNull
    private UserDTO ownerUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
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
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", notes='" + getNotes() + "'" +
            ", tenant=" + getTenant() +
            ", customer=" + getCustomer() +
            ", address=" + getAddress() +
            ", ownerUser=" + getOwnerUser() +
            "}";
    }
}
