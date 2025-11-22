package com.crm.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.crm.domain.Lead} entity.
 */
@Schema(description = "Lead - Sales leads")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String leadNumber;

    private Integer annualRevenue;

    private UserDTO user;

    private CustomerDTO customer;

    private MasterStaticTypeDTO leadSource;

    private MasterStaticTypeDTO industryType;

    private MasterStaticTypeDTO leadStatus;

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

    public String getLeadNumber() {
        return leadNumber;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public Integer getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(Integer annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public MasterStaticTypeDTO getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(MasterStaticTypeDTO leadSource) {
        this.leadSource = leadSource;
    }

    public MasterStaticTypeDTO getIndustryType() {
        return industryType;
    }

    public void setIndustryType(MasterStaticTypeDTO industryType) {
        this.industryType = industryType;
    }

    public MasterStaticTypeDTO getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(MasterStaticTypeDTO leadStatus) {
        this.leadStatus = leadStatus;
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
            ", name='" + getName() + "'" +
            ", leadNumber='" + getLeadNumber() + "'" +
            ", annualRevenue=" + getAnnualRevenue() +
            ", user=" + getUser() +
            ", customer=" + getCustomer() +
            ", leadSource=" + getLeadSource() +
            ", industryType=" + getIndustryType() +
            ", leadStatus=" + getLeadStatus() +
            "}";
    }
}
