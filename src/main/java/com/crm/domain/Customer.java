package com.crm.domain;

import com.crm.domain.enumeration.AccountManagement;
import com.crm.domain.enumeration.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "company_city")
    private String companyCity;

    @Column(name = "company_area")
    private String companyArea;

    @Column(name = "website")
    private String website;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "currency_type")
    private String currencyType;

    @Column(name = "max_invoice_amount")
    private Double maxInvoiceAmount;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "pan_no")
    private String panNo;

    @Column(name = "service_tax_no")
    private String serviceTaxNo;

    @Column(name = "tan_no")
    private String tanNo;

    @Lob
    @Column(name = "custom_field_data")
    private String customFieldData;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "gst_state_name")
    private String gstStateName;

    @Column(name = "gst_state_code")
    private String gstStateCode;

    @Column(name = "is_submit_sample_without_po")
    private Boolean isSubmitSampleWithoutPO;

    @Column(name = "is_block")
    private Boolean isBlock;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_management")
    private AccountManagement accountManagement;

    @Column(name = "revenue_potential")
    private Double revenuePotential;

    @Column(name = "sample_potential")
    private Double samplePotential;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    @Column(name = "total_pipeline")
    private Integer totalPipeline;

    @Column(name = "type")
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "city", "state", "country", "customer", "tenant" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerCompany company;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType customerType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType customerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType ownershipType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType industryType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType customerCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType paymentTerms;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType invoiceFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType gstTreatment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User outstandingPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "addresses", "users", "encryption" }, allowSetters = true)
    private Tenant tenat;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_customer__master_categories",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "master_categories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customers" }, allowSetters = true)
    private Set<MasterCategory> masterCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Customer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyCity() {
        return this.companyCity;
    }

    public Customer companyCity(String companyCity) {
        this.setCompanyCity(companyCity);
        return this;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyArea() {
        return this.companyArea;
    }

    public Customer companyArea(String companyArea) {
        this.setCompanyArea(companyArea);
        return this;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
    }

    public String getWebsite() {
        return this.website;
    }

    public Customer website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Customer customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCurrencyType() {
        return this.currencyType;
    }

    public Customer currencyType(String currencyType) {
        this.setCurrencyType(currencyType);
        return this;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Double getMaxInvoiceAmount() {
        return this.maxInvoiceAmount;
    }

    public Customer maxInvoiceAmount(Double maxInvoiceAmount) {
        this.setMaxInvoiceAmount(maxInvoiceAmount);
        return this;
    }

    public void setMaxInvoiceAmount(Double maxInvoiceAmount) {
        this.maxInvoiceAmount = maxInvoiceAmount;
    }

    public String getGstNo() {
        return this.gstNo;
    }

    public Customer gstNo(String gstNo) {
        this.setGstNo(gstNo);
        return this;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPanNo() {
        return this.panNo;
    }

    public Customer panNo(String panNo) {
        this.setPanNo(panNo);
        return this;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getServiceTaxNo() {
        return this.serviceTaxNo;
    }

    public Customer serviceTaxNo(String serviceTaxNo) {
        this.setServiceTaxNo(serviceTaxNo);
        return this;
    }

    public void setServiceTaxNo(String serviceTaxNo) {
        this.serviceTaxNo = serviceTaxNo;
    }

    public String getTanNo() {
        return this.tanNo;
    }

    public Customer tanNo(String tanNo) {
        this.setTanNo(tanNo);
        return this;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getCustomFieldData() {
        return this.customFieldData;
    }

    public Customer customFieldData(String customFieldData) {
        this.setCustomFieldData(customFieldData);
        return this;
    }

    public void setCustomFieldData(String customFieldData) {
        this.customFieldData = customFieldData;
    }

    public UUID getCorrelationId() {
        return this.correlationId;
    }

    public Customer correlationId(UUID correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public Customer accountNo(String accountNo) {
        this.setAccountNo(accountNo);
        return this;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getGstStateName() {
        return this.gstStateName;
    }

    public Customer gstStateName(String gstStateName) {
        this.setGstStateName(gstStateName);
        return this;
    }

    public void setGstStateName(String gstStateName) {
        this.gstStateName = gstStateName;
    }

    public String getGstStateCode() {
        return this.gstStateCode;
    }

    public Customer gstStateCode(String gstStateCode) {
        this.setGstStateCode(gstStateCode);
        return this;
    }

    public void setGstStateCode(String gstStateCode) {
        this.gstStateCode = gstStateCode;
    }

    public Boolean getIsSubmitSampleWithoutPO() {
        return this.isSubmitSampleWithoutPO;
    }

    public Customer isSubmitSampleWithoutPO(Boolean isSubmitSampleWithoutPO) {
        this.setIsSubmitSampleWithoutPO(isSubmitSampleWithoutPO);
        return this;
    }

    public void setIsSubmitSampleWithoutPO(Boolean isSubmitSampleWithoutPO) {
        this.isSubmitSampleWithoutPO = isSubmitSampleWithoutPO;
    }

    public Boolean getIsBlock() {
        return this.isBlock;
    }

    public Customer isBlock(Boolean isBlock) {
        this.setIsBlock(isBlock);
        return this;
    }

    public void setIsBlock(Boolean isBlock) {
        this.isBlock = isBlock;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public Customer accountType(AccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountManagement getAccountManagement() {
        return this.accountManagement;
    }

    public Customer accountManagement(AccountManagement accountManagement) {
        this.setAccountManagement(accountManagement);
        return this;
    }

    public void setAccountManagement(AccountManagement accountManagement) {
        this.accountManagement = accountManagement;
    }

    public Double getRevenuePotential() {
        return this.revenuePotential;
    }

    public Customer revenuePotential(Double revenuePotential) {
        this.setRevenuePotential(revenuePotential);
        return this;
    }

    public void setRevenuePotential(Double revenuePotential) {
        this.revenuePotential = revenuePotential;
    }

    public Double getSamplePotential() {
        return this.samplePotential;
    }

    public Customer samplePotential(Double samplePotential) {
        this.setSamplePotential(samplePotential);
        return this;
    }

    public void setSamplePotential(Double samplePotential) {
        this.samplePotential = samplePotential;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Customer remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getTotalPipeline() {
        return this.totalPipeline;
    }

    public Customer totalPipeline(Integer totalPipeline) {
        this.setTotalPipeline(totalPipeline);
        return this;
    }

    public void setTotalPipeline(Integer totalPipeline) {
        this.totalPipeline = totalPipeline;
    }

    public String getType() {
        return this.type;
    }

    public Customer type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCustomer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCustomer(this));
        }
        this.addresses = addresses;
    }

    public Customer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Customer addAddresses(Address address) {
        this.addresses.add(address);
        address.setCustomer(this);
        return this;
    }

    public Customer removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setCustomer(null);
        return this;
    }

    public CustomerCompany getCompany() {
        return this.company;
    }

    public void setCompany(CustomerCompany customerCompany) {
        this.company = customerCompany;
    }

    public Customer company(CustomerCompany customerCompany) {
        this.setCompany(customerCompany);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer user(User user) {
        this.setUser(user);
        return this;
    }

    public MasterStaticType getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(MasterStaticType masterStaticType) {
        this.customerType = masterStaticType;
    }

    public Customer customerType(MasterStaticType masterStaticType) {
        this.setCustomerType(masterStaticType);
        return this;
    }

    public MasterStaticType getCustomerStatus() {
        return this.customerStatus;
    }

    public void setCustomerStatus(MasterStaticType masterStaticType) {
        this.customerStatus = masterStaticType;
    }

    public Customer customerStatus(MasterStaticType masterStaticType) {
        this.setCustomerStatus(masterStaticType);
        return this;
    }

    public MasterStaticType getOwnershipType() {
        return this.ownershipType;
    }

    public void setOwnershipType(MasterStaticType masterStaticType) {
        this.ownershipType = masterStaticType;
    }

    public Customer ownershipType(MasterStaticType masterStaticType) {
        this.setOwnershipType(masterStaticType);
        return this;
    }

    public MasterStaticType getIndustryType() {
        return this.industryType;
    }

    public void setIndustryType(MasterStaticType masterStaticType) {
        this.industryType = masterStaticType;
    }

    public Customer industryType(MasterStaticType masterStaticType) {
        this.setIndustryType(masterStaticType);
        return this;
    }

    public MasterStaticType getCustomerCategory() {
        return this.customerCategory;
    }

    public void setCustomerCategory(MasterStaticType masterStaticType) {
        this.customerCategory = masterStaticType;
    }

    public Customer customerCategory(MasterStaticType masterStaticType) {
        this.setCustomerCategory(masterStaticType);
        return this;
    }

    public MasterStaticType getPaymentTerms() {
        return this.paymentTerms;
    }

    public void setPaymentTerms(MasterStaticType masterStaticType) {
        this.paymentTerms = masterStaticType;
    }

    public Customer paymentTerms(MasterStaticType masterStaticType) {
        this.setPaymentTerms(masterStaticType);
        return this;
    }

    public MasterStaticType getInvoiceFrequency() {
        return this.invoiceFrequency;
    }

    public void setInvoiceFrequency(MasterStaticType masterStaticType) {
        this.invoiceFrequency = masterStaticType;
    }

    public Customer invoiceFrequency(MasterStaticType masterStaticType) {
        this.setInvoiceFrequency(masterStaticType);
        return this;
    }

    public MasterStaticType getGstTreatment() {
        return this.gstTreatment;
    }

    public void setGstTreatment(MasterStaticType masterStaticType) {
        this.gstTreatment = masterStaticType;
    }

    public Customer gstTreatment(MasterStaticType masterStaticType) {
        this.setGstTreatment(masterStaticType);
        return this;
    }

    public User getOutstandingPerson() {
        return this.outstandingPerson;
    }

    public void setOutstandingPerson(User user) {
        this.outstandingPerson = user;
    }

    public Customer outstandingPerson(User user) {
        this.setOutstandingPerson(user);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Customer department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Tenant getTenat() {
        return this.tenat;
    }

    public void setTenat(Tenant tenant) {
        this.tenat = tenant;
    }

    public Customer tenat(Tenant tenant) {
        this.setTenat(tenant);
        return this;
    }

    public Set<MasterCategory> getMasterCategories() {
        return this.masterCategories;
    }

    public void setMasterCategories(Set<MasterCategory> masterCategories) {
        this.masterCategories = masterCategories;
    }

    public Customer masterCategories(Set<MasterCategory> masterCategories) {
        this.setMasterCategories(masterCategories);
        return this;
    }

    public Customer addMasterCategories(MasterCategory masterCategory) {
        this.masterCategories.add(masterCategory);
        return this;
    }

    public Customer removeMasterCategories(MasterCategory masterCategory) {
        this.masterCategories.remove(masterCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
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
            "}";
    }
}
