package com.mgs.domain;

import static com.mgs.domain.AddressTestSamples.*;
import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = getContactSample1();
        Contact contact2 = new Contact();
        assertThat(contact1).isNotEqualTo(contact2);

        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);

        contact2 = getContactSample2();
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    void tenantTest() {
        Contact contact = getContactRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        contact.setTenant(tenantBack);
        assertThat(contact.getTenant()).isEqualTo(tenantBack);

        contact.tenant(null);
        assertThat(contact.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Contact contact = getContactRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        contact.setCustomer(customerBack);
        assertThat(contact.getCustomer()).isEqualTo(customerBack);

        contact.customer(null);
        assertThat(contact.getCustomer()).isNull();
    }

    @Test
    void addressTest() {
        Contact contact = getContactRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        contact.setAddress(addressBack);
        assertThat(contact.getAddress()).isEqualTo(addressBack);

        contact.address(null);
        assertThat(contact.getAddress()).isNull();
    }

    @Test
    void ownerUserTest() {
        Contact contact = getContactRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        contact.setOwnerUser(userBack);
        assertThat(contact.getOwnerUser()).isEqualTo(userBack);

        contact.ownerUser(null);
        assertThat(contact.getOwnerUser()).isNull();
    }
}
