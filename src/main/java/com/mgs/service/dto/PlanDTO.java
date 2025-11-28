package com.mgs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mgs.domain.Plan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    private Integer maxUsers;

    private Integer maxStorageMb;

    private Integer maxCustomers;

    private Integer maxContacts;

    private Integer maxQuotations;

    private Integer maxComplaints;

    @NotNull
    private BigDecimal pricePerMonth;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getMaxStorageMb() {
        return maxStorageMb;
    }

    public void setMaxStorageMb(Integer maxStorageMb) {
        this.maxStorageMb = maxStorageMb;
    }

    public Integer getMaxCustomers() {
        return maxCustomers;
    }

    public void setMaxCustomers(Integer maxCustomers) {
        this.maxCustomers = maxCustomers;
    }

    public Integer getMaxContacts() {
        return maxContacts;
    }

    public void setMaxContacts(Integer maxContacts) {
        this.maxContacts = maxContacts;
    }

    public Integer getMaxQuotations() {
        return maxQuotations;
    }

    public void setMaxQuotations(Integer maxQuotations) {
        this.maxQuotations = maxQuotations;
    }

    public Integer getMaxComplaints() {
        return maxComplaints;
    }

    public void setMaxComplaints(Integer maxComplaints) {
        this.maxComplaints = maxComplaints;
    }

    public BigDecimal getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(BigDecimal pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanDTO)) {
            return false;
        }

        PlanDTO planDTO = (PlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxUsers=" + getMaxUsers() +
            ", maxStorageMb=" + getMaxStorageMb() +
            ", maxCustomers=" + getMaxCustomers() +
            ", maxContacts=" + getMaxContacts() +
            ", maxQuotations=" + getMaxQuotations() +
            ", maxComplaints=" + getMaxComplaints() +
            ", pricePerMonth=" + getPricePerMonth() +
            ", currency='" + getCurrency() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
