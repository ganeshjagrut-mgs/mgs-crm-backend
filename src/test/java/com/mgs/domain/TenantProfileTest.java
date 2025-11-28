package com.mgs.domain;

import static com.mgs.domain.AddressTestSamples.*;
import static com.mgs.domain.TenantProfileTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantProfile.class);
        TenantProfile tenantProfile1 = getTenantProfileSample1();
        TenantProfile tenantProfile2 = new TenantProfile();
        assertThat(tenantProfile1).isNotEqualTo(tenantProfile2);

        tenantProfile2.setId(tenantProfile1.getId());
        assertThat(tenantProfile1).isEqualTo(tenantProfile2);

        tenantProfile2 = getTenantProfileSample2();
        assertThat(tenantProfile1).isNotEqualTo(tenantProfile2);
    }

    @Test
    void tenantTest() {
        TenantProfile tenantProfile = getTenantProfileRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        tenantProfile.setTenant(tenantBack);
        assertThat(tenantProfile.getTenant()).isEqualTo(tenantBack);

        tenantProfile.tenant(null);
        assertThat(tenantProfile.getTenant()).isNull();
    }

    @Test
    void addressTest() {
        TenantProfile tenantProfile = getTenantProfileRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        tenantProfile.setAddress(addressBack);
        assertThat(tenantProfile.getAddress()).isEqualTo(addressBack);

        tenantProfile.address(null);
        assertThat(tenantProfile.getAddress()).isNull();
    }
}
