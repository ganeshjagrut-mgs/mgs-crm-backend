package com.mgs.domain;

import static com.mgs.domain.AddressTestSamples.*;
import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.DepartmentTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        customer.setTenant(tenantBack);
        assertThat(customer.getTenant()).isEqualTo(tenantBack);

        customer.tenant(null);
        assertThat(customer.getTenant()).isNull();
    }

    @Test
    void departmentTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        customer.setDepartment(departmentBack);
        assertThat(customer.getDepartment()).isEqualTo(departmentBack);

        customer.department(null);
        assertThat(customer.getDepartment()).isNull();
    }

    @Test
    void billingAddressTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        customer.setBillingAddress(addressBack);
        assertThat(customer.getBillingAddress()).isEqualTo(addressBack);

        customer.billingAddress(null);
        assertThat(customer.getBillingAddress()).isNull();
    }

    @Test
    void shippingAddressTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        customer.setShippingAddress(addressBack);
        assertThat(customer.getShippingAddress()).isEqualTo(addressBack);

        customer.shippingAddress(null);
        assertThat(customer.getShippingAddress()).isNull();
    }

    @Test
    void primaryContactTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        customer.setPrimaryContact(contactBack);
        assertThat(customer.getPrimaryContact()).isEqualTo(contactBack);

        customer.primaryContact(null);
        assertThat(customer.getPrimaryContact()).isNull();
    }
}
