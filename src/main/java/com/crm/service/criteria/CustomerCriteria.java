package com.crm.service.criteria;

import com.crm.domain.enumeration.AccountManagement;
import com.crm.domain.enumeration.AccountType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Customer} entity. This class is used
 * in {@link com.crm.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountType
     */
    public static class AccountTypeFilter extends Filter<AccountType> {

        public AccountTypeFilter() {}

        public AccountTypeFilter(AccountTypeFilter filter) {
            super(filter);
        }

        @Override
        public AccountTypeFilter copy() {
            return new AccountTypeFilter(this);
        }
    }

    /**
     * Class for filtering AccountManagement
     */
    public static class AccountManagementFilter extends Filter<AccountManagement> {

        public AccountManagementFilter() {}

        public AccountManagementFilter(AccountManagementFilter filter) {
            super(filter);
        }

        @Override
        public AccountManagementFilter copy() {
            return new AccountManagementFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter companyCity;

    private StringFilter companyArea;

    private StringFilter website;

    private StringFilter customerName;

    private StringFilter currencyType;

    private DoubleFilter maxInvoiceAmount;

    private StringFilter gstNo;

    private StringFilter panNo;

    private StringFilter serviceTaxNo;

    private StringFilter tanNo;

    private UUIDFilter correlationId;

    private StringFilter accountNo;

    private StringFilter gstStateName;

    private StringFilter gstStateCode;

    private BooleanFilter isSubmitSampleWithoutPO;

    private BooleanFilter isBlock;

    private AccountTypeFilter accountType;

    private AccountManagementFilter accountManagement;

    private DoubleFilter revenuePotential;

    private DoubleFilter samplePotential;

    private IntegerFilter totalPipeline;

    private StringFilter type;

    private LongFilter addressesId;

    private LongFilter companyId;

    private LongFilter userId;

    private LongFilter customerTypeId;

    private LongFilter customerStatusId;

    private LongFilter ownershipTypeId;

    private LongFilter industryTypeId;

    private LongFilter customerCategoryId;

    private LongFilter paymentTermsId;

    private LongFilter invoiceFrequencyId;

    private LongFilter gstTreatmentId;

    private LongFilter outstandingPersonId;

    private LongFilter departmentId;

    private LongFilter tenatId;

    private LongFilter masterCategoriesId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.companyCity = other.companyCity == null ? null : other.companyCity.copy();
        this.companyArea = other.companyArea == null ? null : other.companyArea.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.currencyType = other.currencyType == null ? null : other.currencyType.copy();
        this.maxInvoiceAmount = other.maxInvoiceAmount == null ? null : other.maxInvoiceAmount.copy();
        this.gstNo = other.gstNo == null ? null : other.gstNo.copy();
        this.panNo = other.panNo == null ? null : other.panNo.copy();
        this.serviceTaxNo = other.serviceTaxNo == null ? null : other.serviceTaxNo.copy();
        this.tanNo = other.tanNo == null ? null : other.tanNo.copy();
        this.correlationId = other.correlationId == null ? null : other.correlationId.copy();
        this.accountNo = other.accountNo == null ? null : other.accountNo.copy();
        this.gstStateName = other.gstStateName == null ? null : other.gstStateName.copy();
        this.gstStateCode = other.gstStateCode == null ? null : other.gstStateCode.copy();
        this.isSubmitSampleWithoutPO = other.isSubmitSampleWithoutPO == null ? null : other.isSubmitSampleWithoutPO.copy();
        this.isBlock = other.isBlock == null ? null : other.isBlock.copy();
        this.accountType = other.accountType == null ? null : other.accountType.copy();
        this.accountManagement = other.accountManagement == null ? null : other.accountManagement.copy();
        this.revenuePotential = other.revenuePotential == null ? null : other.revenuePotential.copy();
        this.samplePotential = other.samplePotential == null ? null : other.samplePotential.copy();
        this.totalPipeline = other.totalPipeline == null ? null : other.totalPipeline.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.customerTypeId = other.customerTypeId == null ? null : other.customerTypeId.copy();
        this.customerStatusId = other.customerStatusId == null ? null : other.customerStatusId.copy();
        this.ownershipTypeId = other.ownershipTypeId == null ? null : other.ownershipTypeId.copy();
        this.industryTypeId = other.industryTypeId == null ? null : other.industryTypeId.copy();
        this.customerCategoryId = other.customerCategoryId == null ? null : other.customerCategoryId.copy();
        this.paymentTermsId = other.paymentTermsId == null ? null : other.paymentTermsId.copy();
        this.invoiceFrequencyId = other.invoiceFrequencyId == null ? null : other.invoiceFrequencyId.copy();
        this.gstTreatmentId = other.gstTreatmentId == null ? null : other.gstTreatmentId.copy();
        this.outstandingPersonId = other.outstandingPersonId == null ? null : other.outstandingPersonId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.tenatId = other.tenatId == null ? null : other.tenatId.copy();
        this.masterCategoriesId = other.masterCategoriesId == null ? null : other.masterCategoriesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCompanyCity() {
        return companyCity;
    }

    public StringFilter companyCity() {
        if (companyCity == null) {
            companyCity = new StringFilter();
        }
        return companyCity;
    }

    public void setCompanyCity(StringFilter companyCity) {
        this.companyCity = companyCity;
    }

    public StringFilter getCompanyArea() {
        return companyArea;
    }

    public StringFilter companyArea() {
        if (companyArea == null) {
            companyArea = new StringFilter();
        }
        return companyArea;
    }

    public void setCompanyArea(StringFilter companyArea) {
        this.companyArea = companyArea;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public StringFilter customerName() {
        if (customerName == null) {
            customerName = new StringFilter();
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public StringFilter getCurrencyType() {
        return currencyType;
    }

    public StringFilter currencyType() {
        if (currencyType == null) {
            currencyType = new StringFilter();
        }
        return currencyType;
    }

    public void setCurrencyType(StringFilter currencyType) {
        this.currencyType = currencyType;
    }

    public DoubleFilter getMaxInvoiceAmount() {
        return maxInvoiceAmount;
    }

    public DoubleFilter maxInvoiceAmount() {
        if (maxInvoiceAmount == null) {
            maxInvoiceAmount = new DoubleFilter();
        }
        return maxInvoiceAmount;
    }

    public void setMaxInvoiceAmount(DoubleFilter maxInvoiceAmount) {
        this.maxInvoiceAmount = maxInvoiceAmount;
    }

    public StringFilter getGstNo() {
        return gstNo;
    }

    public StringFilter gstNo() {
        if (gstNo == null) {
            gstNo = new StringFilter();
        }
        return gstNo;
    }

    public void setGstNo(StringFilter gstNo) {
        this.gstNo = gstNo;
    }

    public StringFilter getPanNo() {
        return panNo;
    }

    public StringFilter panNo() {
        if (panNo == null) {
            panNo = new StringFilter();
        }
        return panNo;
    }

    public void setPanNo(StringFilter panNo) {
        this.panNo = panNo;
    }

    public StringFilter getServiceTaxNo() {
        return serviceTaxNo;
    }

    public StringFilter serviceTaxNo() {
        if (serviceTaxNo == null) {
            serviceTaxNo = new StringFilter();
        }
        return serviceTaxNo;
    }

    public void setServiceTaxNo(StringFilter serviceTaxNo) {
        this.serviceTaxNo = serviceTaxNo;
    }

    public StringFilter getTanNo() {
        return tanNo;
    }

    public StringFilter tanNo() {
        if (tanNo == null) {
            tanNo = new StringFilter();
        }
        return tanNo;
    }

    public void setTanNo(StringFilter tanNo) {
        this.tanNo = tanNo;
    }

    public UUIDFilter getCorrelationId() {
        return correlationId;
    }

    public UUIDFilter correlationId() {
        if (correlationId == null) {
            correlationId = new UUIDFilter();
        }
        return correlationId;
    }

    public void setCorrelationId(UUIDFilter correlationId) {
        this.correlationId = correlationId;
    }

    public StringFilter getAccountNo() {
        return accountNo;
    }

    public StringFilter accountNo() {
        if (accountNo == null) {
            accountNo = new StringFilter();
        }
        return accountNo;
    }

    public void setAccountNo(StringFilter accountNo) {
        this.accountNo = accountNo;
    }

    public StringFilter getGstStateName() {
        return gstStateName;
    }

    public StringFilter gstStateName() {
        if (gstStateName == null) {
            gstStateName = new StringFilter();
        }
        return gstStateName;
    }

    public void setGstStateName(StringFilter gstStateName) {
        this.gstStateName = gstStateName;
    }

    public StringFilter getGstStateCode() {
        return gstStateCode;
    }

    public StringFilter gstStateCode() {
        if (gstStateCode == null) {
            gstStateCode = new StringFilter();
        }
        return gstStateCode;
    }

    public void setGstStateCode(StringFilter gstStateCode) {
        this.gstStateCode = gstStateCode;
    }

    public BooleanFilter getIsSubmitSampleWithoutPO() {
        return isSubmitSampleWithoutPO;
    }

    public BooleanFilter isSubmitSampleWithoutPO() {
        if (isSubmitSampleWithoutPO == null) {
            isSubmitSampleWithoutPO = new BooleanFilter();
        }
        return isSubmitSampleWithoutPO;
    }

    public void setIsSubmitSampleWithoutPO(BooleanFilter isSubmitSampleWithoutPO) {
        this.isSubmitSampleWithoutPO = isSubmitSampleWithoutPO;
    }

    public BooleanFilter getIsBlock() {
        return isBlock;
    }

    public BooleanFilter isBlock() {
        if (isBlock == null) {
            isBlock = new BooleanFilter();
        }
        return isBlock;
    }

    public void setIsBlock(BooleanFilter isBlock) {
        this.isBlock = isBlock;
    }

    public AccountTypeFilter getAccountType() {
        return accountType;
    }

    public AccountTypeFilter accountType() {
        if (accountType == null) {
            accountType = new AccountTypeFilter();
        }
        return accountType;
    }

    public void setAccountType(AccountTypeFilter accountType) {
        this.accountType = accountType;
    }

    public AccountManagementFilter getAccountManagement() {
        return accountManagement;
    }

    public AccountManagementFilter accountManagement() {
        if (accountManagement == null) {
            accountManagement = new AccountManagementFilter();
        }
        return accountManagement;
    }

    public void setAccountManagement(AccountManagementFilter accountManagement) {
        this.accountManagement = accountManagement;
    }

    public DoubleFilter getRevenuePotential() {
        return revenuePotential;
    }

    public DoubleFilter revenuePotential() {
        if (revenuePotential == null) {
            revenuePotential = new DoubleFilter();
        }
        return revenuePotential;
    }

    public void setRevenuePotential(DoubleFilter revenuePotential) {
        this.revenuePotential = revenuePotential;
    }

    public DoubleFilter getSamplePotential() {
        return samplePotential;
    }

    public DoubleFilter samplePotential() {
        if (samplePotential == null) {
            samplePotential = new DoubleFilter();
        }
        return samplePotential;
    }

    public void setSamplePotential(DoubleFilter samplePotential) {
        this.samplePotential = samplePotential;
    }

    public IntegerFilter getTotalPipeline() {
        return totalPipeline;
    }

    public IntegerFilter totalPipeline() {
        if (totalPipeline == null) {
            totalPipeline = new IntegerFilter();
        }
        return totalPipeline;
    }

    public void setTotalPipeline(IntegerFilter totalPipeline) {
        this.totalPipeline = totalPipeline;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public LongFilter addressesId() {
        if (addressesId == null) {
            addressesId = new LongFilter();
        }
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCustomerTypeId() {
        return customerTypeId;
    }

    public LongFilter customerTypeId() {
        if (customerTypeId == null) {
            customerTypeId = new LongFilter();
        }
        return customerTypeId;
    }

    public void setCustomerTypeId(LongFilter customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public LongFilter getCustomerStatusId() {
        return customerStatusId;
    }

    public LongFilter customerStatusId() {
        if (customerStatusId == null) {
            customerStatusId = new LongFilter();
        }
        return customerStatusId;
    }

    public void setCustomerStatusId(LongFilter customerStatusId) {
        this.customerStatusId = customerStatusId;
    }

    public LongFilter getOwnershipTypeId() {
        return ownershipTypeId;
    }

    public LongFilter ownershipTypeId() {
        if (ownershipTypeId == null) {
            ownershipTypeId = new LongFilter();
        }
        return ownershipTypeId;
    }

    public void setOwnershipTypeId(LongFilter ownershipTypeId) {
        this.ownershipTypeId = ownershipTypeId;
    }

    public LongFilter getIndustryTypeId() {
        return industryTypeId;
    }

    public LongFilter industryTypeId() {
        if (industryTypeId == null) {
            industryTypeId = new LongFilter();
        }
        return industryTypeId;
    }

    public void setIndustryTypeId(LongFilter industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    public LongFilter getCustomerCategoryId() {
        return customerCategoryId;
    }

    public LongFilter customerCategoryId() {
        if (customerCategoryId == null) {
            customerCategoryId = new LongFilter();
        }
        return customerCategoryId;
    }

    public void setCustomerCategoryId(LongFilter customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public LongFilter getPaymentTermsId() {
        return paymentTermsId;
    }

    public LongFilter paymentTermsId() {
        if (paymentTermsId == null) {
            paymentTermsId = new LongFilter();
        }
        return paymentTermsId;
    }

    public void setPaymentTermsId(LongFilter paymentTermsId) {
        this.paymentTermsId = paymentTermsId;
    }

    public LongFilter getInvoiceFrequencyId() {
        return invoiceFrequencyId;
    }

    public LongFilter invoiceFrequencyId() {
        if (invoiceFrequencyId == null) {
            invoiceFrequencyId = new LongFilter();
        }
        return invoiceFrequencyId;
    }

    public void setInvoiceFrequencyId(LongFilter invoiceFrequencyId) {
        this.invoiceFrequencyId = invoiceFrequencyId;
    }

    public LongFilter getGstTreatmentId() {
        return gstTreatmentId;
    }

    public LongFilter gstTreatmentId() {
        if (gstTreatmentId == null) {
            gstTreatmentId = new LongFilter();
        }
        return gstTreatmentId;
    }

    public void setGstTreatmentId(LongFilter gstTreatmentId) {
        this.gstTreatmentId = gstTreatmentId;
    }

    public LongFilter getOutstandingPersonId() {
        return outstandingPersonId;
    }

    public LongFilter outstandingPersonId() {
        if (outstandingPersonId == null) {
            outstandingPersonId = new LongFilter();
        }
        return outstandingPersonId;
    }

    public void setOutstandingPersonId(LongFilter outstandingPersonId) {
        this.outstandingPersonId = outstandingPersonId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getTenatId() {
        return tenatId;
    }

    public LongFilter tenatId() {
        if (tenatId == null) {
            tenatId = new LongFilter();
        }
        return tenatId;
    }

    public void setTenatId(LongFilter tenatId) {
        this.tenatId = tenatId;
    }

    public LongFilter getMasterCategoriesId() {
        return masterCategoriesId;
    }

    public LongFilter masterCategoriesId() {
        if (masterCategoriesId == null) {
            masterCategoriesId = new LongFilter();
        }
        return masterCategoriesId;
    }

    public void setMasterCategoriesId(LongFilter masterCategoriesId) {
        this.masterCategoriesId = masterCategoriesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(companyCity, that.companyCity) &&
            Objects.equals(companyArea, that.companyArea) &&
            Objects.equals(website, that.website) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(currencyType, that.currencyType) &&
            Objects.equals(maxInvoiceAmount, that.maxInvoiceAmount) &&
            Objects.equals(gstNo, that.gstNo) &&
            Objects.equals(panNo, that.panNo) &&
            Objects.equals(serviceTaxNo, that.serviceTaxNo) &&
            Objects.equals(tanNo, that.tanNo) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(accountNo, that.accountNo) &&
            Objects.equals(gstStateName, that.gstStateName) &&
            Objects.equals(gstStateCode, that.gstStateCode) &&
            Objects.equals(isSubmitSampleWithoutPO, that.isSubmitSampleWithoutPO) &&
            Objects.equals(isBlock, that.isBlock) &&
            Objects.equals(accountType, that.accountType) &&
            Objects.equals(accountManagement, that.accountManagement) &&
            Objects.equals(revenuePotential, that.revenuePotential) &&
            Objects.equals(samplePotential, that.samplePotential) &&
            Objects.equals(totalPipeline, that.totalPipeline) &&
            Objects.equals(type, that.type) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(customerTypeId, that.customerTypeId) &&
            Objects.equals(customerStatusId, that.customerStatusId) &&
            Objects.equals(ownershipTypeId, that.ownershipTypeId) &&
            Objects.equals(industryTypeId, that.industryTypeId) &&
            Objects.equals(customerCategoryId, that.customerCategoryId) &&
            Objects.equals(paymentTermsId, that.paymentTermsId) &&
            Objects.equals(invoiceFrequencyId, that.invoiceFrequencyId) &&
            Objects.equals(gstTreatmentId, that.gstTreatmentId) &&
            Objects.equals(outstandingPersonId, that.outstandingPersonId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(tenatId, that.tenatId) &&
            Objects.equals(masterCategoriesId, that.masterCategoriesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            companyCity,
            companyArea,
            website,
            customerName,
            currencyType,
            maxInvoiceAmount,
            gstNo,
            panNo,
            serviceTaxNo,
            tanNo,
            correlationId,
            accountNo,
            gstStateName,
            gstStateCode,
            isSubmitSampleWithoutPO,
            isBlock,
            accountType,
            accountManagement,
            revenuePotential,
            samplePotential,
            totalPipeline,
            type,
            addressesId,
            companyId,
            userId,
            customerTypeId,
            customerStatusId,
            ownershipTypeId,
            industryTypeId,
            customerCategoryId,
            paymentTermsId,
            invoiceFrequencyId,
            gstTreatmentId,
            outstandingPersonId,
            departmentId,
            tenatId,
            masterCategoriesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (companyCity != null ? "companyCity=" + companyCity + ", " : "") +
            (companyArea != null ? "companyArea=" + companyArea + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (customerName != null ? "customerName=" + customerName + ", " : "") +
            (currencyType != null ? "currencyType=" + currencyType + ", " : "") +
            (maxInvoiceAmount != null ? "maxInvoiceAmount=" + maxInvoiceAmount + ", " : "") +
            (gstNo != null ? "gstNo=" + gstNo + ", " : "") +
            (panNo != null ? "panNo=" + panNo + ", " : "") +
            (serviceTaxNo != null ? "serviceTaxNo=" + serviceTaxNo + ", " : "") +
            (tanNo != null ? "tanNo=" + tanNo + ", " : "") +
            (correlationId != null ? "correlationId=" + correlationId + ", " : "") +
            (accountNo != null ? "accountNo=" + accountNo + ", " : "") +
            (gstStateName != null ? "gstStateName=" + gstStateName + ", " : "") +
            (gstStateCode != null ? "gstStateCode=" + gstStateCode + ", " : "") +
            (isSubmitSampleWithoutPO != null ? "isSubmitSampleWithoutPO=" + isSubmitSampleWithoutPO + ", " : "") +
            (isBlock != null ? "isBlock=" + isBlock + ", " : "") +
            (accountType != null ? "accountType=" + accountType + ", " : "") +
            (accountManagement != null ? "accountManagement=" + accountManagement + ", " : "") +
            (revenuePotential != null ? "revenuePotential=" + revenuePotential + ", " : "") +
            (samplePotential != null ? "samplePotential=" + samplePotential + ", " : "") +
            (totalPipeline != null ? "totalPipeline=" + totalPipeline + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (customerTypeId != null ? "customerTypeId=" + customerTypeId + ", " : "") +
            (customerStatusId != null ? "customerStatusId=" + customerStatusId + ", " : "") +
            (ownershipTypeId != null ? "ownershipTypeId=" + ownershipTypeId + ", " : "") +
            (industryTypeId != null ? "industryTypeId=" + industryTypeId + ", " : "") +
            (customerCategoryId != null ? "customerCategoryId=" + customerCategoryId + ", " : "") +
            (paymentTermsId != null ? "paymentTermsId=" + paymentTermsId + ", " : "") +
            (invoiceFrequencyId != null ? "invoiceFrequencyId=" + invoiceFrequencyId + ", " : "") +
            (gstTreatmentId != null ? "gstTreatmentId=" + gstTreatmentId + ", " : "") +
            (outstandingPersonId != null ? "outstandingPersonId=" + outstandingPersonId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (tenatId != null ? "tenatId=" + tenatId + ", " : "") +
            (masterCategoriesId != null ? "masterCategoriesId=" + masterCategoriesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
