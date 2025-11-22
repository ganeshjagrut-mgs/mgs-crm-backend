package com.crm.domain;

import static com.crm.domain.AddressTestSamples.*;
import static com.crm.domain.CustomerCompanyTestSamples.*;
import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.DepartmentTestSamples.*;
import static com.crm.domain.MasterCategoryTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void addressesTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        customer.addAddresses(addressBack);
        assertThat(customer.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCustomer()).isEqualTo(customer);

        customer.removeAddresses(addressBack);
        assertThat(customer.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCustomer()).isNull();

        customer.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(customer.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCustomer()).isEqualTo(customer);

        customer.setAddresses(new HashSet<>());
        assertThat(customer.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCustomer()).isNull();
    }

    @Test
    void companyTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        CustomerCompany customerCompanyBack = getCustomerCompanyRandomSampleGenerator();

        customer.setCompany(customerCompanyBack);
        assertThat(customer.getCompany()).isEqualTo(customerCompanyBack);

        customer.company(null);
        assertThat(customer.getCompany()).isNull();
    }

    @Test
    void customerTypeTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setCustomerType(masterStaticTypeBack);
        assertThat(customer.getCustomerType()).isEqualTo(masterStaticTypeBack);

        customer.customerType(null);
        assertThat(customer.getCustomerType()).isNull();
    }

    @Test
    void customerStatusTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setCustomerStatus(masterStaticTypeBack);
        assertThat(customer.getCustomerStatus()).isEqualTo(masterStaticTypeBack);

        customer.customerStatus(null);
        assertThat(customer.getCustomerStatus()).isNull();
    }

    @Test
    void ownershipTypeTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setOwnershipType(masterStaticTypeBack);
        assertThat(customer.getOwnershipType()).isEqualTo(masterStaticTypeBack);

        customer.ownershipType(null);
        assertThat(customer.getOwnershipType()).isNull();
    }

    @Test
    void industryTypeTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setIndustryType(masterStaticTypeBack);
        assertThat(customer.getIndustryType()).isEqualTo(masterStaticTypeBack);

        customer.industryType(null);
        assertThat(customer.getIndustryType()).isNull();
    }

    @Test
    void customerCategoryTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setCustomerCategory(masterStaticTypeBack);
        assertThat(customer.getCustomerCategory()).isEqualTo(masterStaticTypeBack);

        customer.customerCategory(null);
        assertThat(customer.getCustomerCategory()).isNull();
    }

    @Test
    void paymentTermsTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setPaymentTerms(masterStaticTypeBack);
        assertThat(customer.getPaymentTerms()).isEqualTo(masterStaticTypeBack);

        customer.paymentTerms(null);
        assertThat(customer.getPaymentTerms()).isNull();
    }

    @Test
    void invoiceFrequencyTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setInvoiceFrequency(masterStaticTypeBack);
        assertThat(customer.getInvoiceFrequency()).isEqualTo(masterStaticTypeBack);

        customer.invoiceFrequency(null);
        assertThat(customer.getInvoiceFrequency()).isNull();
    }

    @Test
    void gstTreatmentTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        customer.setGstTreatment(masterStaticTypeBack);
        assertThat(customer.getGstTreatment()).isEqualTo(masterStaticTypeBack);

        customer.gstTreatment(null);
        assertThat(customer.getGstTreatment()).isNull();
    }

    @Test
    void departmentTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        customer.setDepartment(departmentBack);
        assertThat(customer.getDepartment()).isEqualTo(departmentBack);

        customer.department(null);
        assertThat(customer.getDepartment()).isNull();
    }

    @Test
    void tenatTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        customer.setTenat(tenantBack);
        assertThat(customer.getTenat()).isEqualTo(tenantBack);

        customer.tenat(null);
        assertThat(customer.getTenat()).isNull();
    }

    @Test
    void masterCategoriesTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterCategory masterCategoryBack = getMasterCategoryRandomSampleGenerator();

        customer.addMasterCategories(masterCategoryBack);
        assertThat(customer.getMasterCategories()).containsOnly(masterCategoryBack);

        customer.removeMasterCategories(masterCategoryBack);
        assertThat(customer.getMasterCategories()).doesNotContain(masterCategoryBack);

        customer.masterCategories(new HashSet<>(Set.of(masterCategoryBack)));
        assertThat(customer.getMasterCategories()).containsOnly(masterCategoryBack);

        customer.setMasterCategories(new HashSet<>());
        assertThat(customer.getMasterCategories()).doesNotContain(masterCategoryBack);
    }
}
