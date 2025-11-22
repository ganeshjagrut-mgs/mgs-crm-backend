package com.crm.service.dto;

import com.crm.domain.enumeration.AccountManagement;
import com.crm.domain.enumeration.AccountType;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.crm.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String companyCity;

    private String companyArea;

    private String website;

    private String customerName;

    private String currencyType;

    private Double maxInvoiceAmount;

    private String gstNo;

    private String panNo;

    private String serviceTaxNo;

    private String tanNo;

    @Lob
    private String customFieldData;

    private UUID correlationId;

    private String accountNo;

    private String gstStateName;

    private String gstStateCode;

    private Boolean isSubmitSampleWithoutPO;

    private Boolean isBlock;

    private AccountType accountType;

    private AccountManagement accountManagement;

    private Double revenuePotential;

    private Double samplePotential;

    @Lob
    private String remarks;

    private Integer totalPipeline;

    private String type;

    private CustomerCompanyDTO company;

    private UserDTO user;

    private MasterStaticTypeDTO customerType;

    private MasterStaticTypeDTO customerStatus;

    private MasterStaticTypeDTO ownershipType;

    private MasterStaticTypeDTO industryType;

    private MasterStaticTypeDTO customerCategory;

    private MasterStaticTypeDTO paymentTerms;

    private MasterStaticTypeDTO invoiceFrequency;

    private MasterStaticTypeDTO gstTreatment;

    private UserDTO outstandingPerson;

    private DepartmentDTO department;

    private TenantDTO tenat;

    private Set<MasterCategoryDTO> masterCategories = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyArea() {
        return companyArea;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Double getMaxInvoiceAmount() {
        return maxInvoiceAmount;
    }

    public void setMaxInvoiceAmount(Double maxInvoiceAmount) {
        this.maxInvoiceAmount = maxInvoiceAmount;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getServiceTaxNo() {
        return serviceTaxNo;
    }

    public void setServiceTaxNo(String serviceTaxNo) {
        this.serviceTaxNo = serviceTaxNo;
    }

    public String getTanNo() {
        return tanNo;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getCustomFieldData() {
        return customFieldData;
    }

    public void setCustomFieldData(String customFieldData) {
        this.customFieldData = customFieldData;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getGstStateName() {
        return gstStateName;
    }

    public void setGstStateName(String gstStateName) {
        this.gstStateName = gstStateName;
    }

    public String getGstStateCode() {
        return gstStateCode;
    }

    public void setGstStateCode(String gstStateCode) {
        this.gstStateCode = gstStateCode;
    }

    public Boolean getIsSubmitSampleWithoutPO() {
        return isSubmitSampleWithoutPO;
    }

    public void setIsSubmitSampleWithoutPO(Boolean isSubmitSampleWithoutPO) {
        this.isSubmitSampleWithoutPO = isSubmitSampleWithoutPO;
    }

    public Boolean getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Boolean isBlock) {
        this.isBlock = isBlock;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountManagement getAccountManagement() {
        return accountManagement;
    }

    public void setAccountManagement(AccountManagement accountManagement) {
        this.accountManagement = accountManagement;
    }

    public Double getRevenuePotential() {
        return revenuePotential;
    }

    public void setRevenuePotential(Double revenuePotential) {
        this.revenuePotential = revenuePotential;
    }

    public Double getSamplePotential() {
        return samplePotential;
    }

    public void setSamplePotential(Double samplePotential) {
        this.samplePotential = samplePotential;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getTotalPipeline() {
        return totalPipeline;
    }

    public void setTotalPipeline(Integer totalPipeline) {
        this.totalPipeline = totalPipeline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CustomerCompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CustomerCompanyDTO company) {
        this.company = company;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public MasterStaticTypeDTO getCustomerType() {
        return customerType;
    }

    public void setCustomerType(MasterStaticTypeDTO customerType) {
        this.customerType = customerType;
    }

    public MasterStaticTypeDTO getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(MasterStaticTypeDTO customerStatus) {
        this.customerStatus = customerStatus;
    }

    public MasterStaticTypeDTO getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(MasterStaticTypeDTO ownershipType) {
        this.ownershipType = ownershipType;
    }

    public MasterStaticTypeDTO getIndustryType() {
        return industryType;
    }

    public void setIndustryType(MasterStaticTypeDTO industryType) {
        this.industryType = industryType;
    }

    public MasterStaticTypeDTO getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(MasterStaticTypeDTO customerCategory) {
        this.customerCategory = customerCategory;
    }

    public MasterStaticTypeDTO getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(MasterStaticTypeDTO paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public MasterStaticTypeDTO getInvoiceFrequency() {
        return invoiceFrequency;
    }

    public void setInvoiceFrequency(MasterStaticTypeDTO invoiceFrequency) {
        this.invoiceFrequency = invoiceFrequency;
    }

    public MasterStaticTypeDTO getGstTreatment() {
        return gstTreatment;
    }

    public void setGstTreatment(MasterStaticTypeDTO gstTreatment) {
        this.gstTreatment = gstTreatment;
    }

    public UserDTO getOutstandingPerson() {
        return outstandingPerson;
    }

    public void setOutstandingPerson(UserDTO outstandingPerson) {
        this.outstandingPerson = outstandingPerson;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public TenantDTO getTenat() {
        return tenat;
    }

    public void setTenat(TenantDTO tenat) {
        this.tenat = tenat;
    }

    public Set<MasterCategoryDTO> getMasterCategories() {
        return masterCategories;
    }

    public void setMasterCategories(Set<MasterCategoryDTO> masterCategories) {
        this.masterCategories = masterCategories;
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
            ", description='" + getDescription() + "'" +
            ", companyCity='" + getCompanyCity() + "'" +
            ", companyArea='" + getCompanyArea() + "'" +
            ", website='" + getWebsite() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", currencyType='" + getCurrencyType() + "'" +
            ", maxInvoiceAmount=" + getMaxInvoiceAmount() +
            ", gstNo='" + getGstNo() + "'" +
            ", panNo='" + getPanNo() + "'" +
            ", serviceTaxNo='" + getServiceTaxNo() + "'" +
            ", tanNo='" + getTanNo() + "'" +
            ", customFieldData='" + getCustomFieldData() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", accountNo='" + getAccountNo() + "'" +
            ", gstStateName='" + getGstStateName() + "'" +
            ", gstStateCode='" + getGstStateCode() + "'" +
            ", isSubmitSampleWithoutPO='" + getIsSubmitSampleWithoutPO() + "'" +
            ", isBlock='" + getIsBlock() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", accountManagement='" + getAccountManagement() + "'" +
            ", revenuePotential=" + getRevenuePotential() +
            ", samplePotential=" + getSamplePotential() +
            ", remarks='" + getRemarks() + "'" +
            ", totalPipeline=" + getTotalPipeline() +
            ", type='" + getType() + "'" +
            ", company=" + getCompany() +
            ", user=" + getUser() +
            ", customerType=" + getCustomerType() +
            ", customerStatus=" + getCustomerStatus() +
            ", ownershipType=" + getOwnershipType() +
            ", industryType=" + getIndustryType() +
            ", customerCategory=" + getCustomerCategory() +
            ", paymentTerms=" + getPaymentTerms() +
            ", invoiceFrequency=" + getInvoiceFrequency() +
            ", gstTreatment=" + getGstTreatment() +
            ", outstandingPerson=" + getOutstandingPerson() +
            ", department=" + getDepartment() +
            ", tenat=" + getTenat() +
            ", masterCategories=" + getMasterCategories() +
            "}";
    }
}
