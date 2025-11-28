package com.mgs.domain;

import static com.mgs.domain.AddressTestSamples.*;
import static com.mgs.domain.TenantProfileTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        Address address = getAddressRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        address.setTenant(tenantBack);
        assertThat(address.getTenant()).isEqualTo(tenantBack);

        address.tenant(null);
        assertThat(address.getTenant()).isNull();
    }

    @Test
    void tenantProfileTest() {
        Address address = getAddressRandomSampleGenerator();
        TenantProfile tenantProfileBack = getTenantProfileRandomSampleGenerator();

        address.setTenantProfile(tenantProfileBack);
        assertThat(address.getTenantProfile()).isEqualTo(tenantProfileBack);
        assertThat(tenantProfileBack.getAddress()).isEqualTo(address);

        address.tenantProfile(null);
        assertThat(address.getTenantProfile()).isNull();
        assertThat(tenantProfileBack.getAddress()).isNull();
    }
}
