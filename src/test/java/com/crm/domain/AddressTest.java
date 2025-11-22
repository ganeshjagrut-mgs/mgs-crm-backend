package com.crm.domain;

import static com.crm.domain.AddressTestSamples.*;
import static com.crm.domain.CityTestSamples.*;
import static com.crm.domain.CountryTestSamples.*;
import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.StateTestSamples.*;
import static com.crm.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = getAddressSample1();
        Address address2 = new Address();
        assertThat(address1).isNotEqualTo(address2);

        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);

        address2 = getAddressSample2();
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void cityTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        address.setCity(cityBack);
        assertThat(address.getCity()).isEqualTo(cityBack);

        address.city(null);
        assertThat(address.getCity()).isNull();
    }

    @Test
    void stateTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        address.setState(stateBack);
        assertThat(address.getState()).isEqualTo(stateBack);

        address.state(null);
        assertThat(address.getState()).isNull();
    }

    @Test
    void countryTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        address.setCountry(countryBack);
        assertThat(address.getCountry()).isEqualTo(countryBack);

        address.country(null);
        assertThat(address.getCountry()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        address.setCustomer(customerBack);
        assertThat(address.getCustomer()).isEqualTo(customerBack);

        address.customer(null);
        assertThat(address.getCustomer()).isNull();
    }

    @Test
    void tenantTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        address.setTenant(tenantBack);
        assertThat(address.getTenant()).isEqualTo(tenantBack);

        address.tenant(null);
        assertThat(address.getTenant()).isNull();
    }
}
