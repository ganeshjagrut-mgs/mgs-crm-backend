package com.mgs.domain;

import static com.mgs.domain.TenantBrandingTestSamples.*;
import static com.mgs.domain.TenantProfileTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tenant.class);
        Tenant tenant1 = getTenantSample1();
        Tenant tenant2 = new Tenant();
        assertThat(tenant1).isNotEqualTo(tenant2);

        tenant2.setId(tenant1.getId());
        assertThat(tenant1).isEqualTo(tenant2);

        tenant2 = getTenantSample2();
        assertThat(tenant1).isNotEqualTo(tenant2);
    }

    @Test
    void tenantProfileTest() {
        Tenant tenant = getTenantRandomSampleGenerator();
        TenantProfile tenantProfileBack = getTenantProfileRandomSampleGenerator();

        tenant.setTenantProfile(tenantProfileBack);
        assertThat(tenant.getTenantProfile()).isEqualTo(tenantProfileBack);
        assertThat(tenantProfileBack.getTenant()).isEqualTo(tenant);

        tenant.tenantProfile(null);
        assertThat(tenant.getTenantProfile()).isNull();
        assertThat(tenantProfileBack.getTenant()).isNull();
    }

    @Test
    void tenantBrandingTest() {
        Tenant tenant = getTenantRandomSampleGenerator();
        TenantBranding tenantBrandingBack = getTenantBrandingRandomSampleGenerator();

        tenant.setTenantBranding(tenantBrandingBack);
        assertThat(tenant.getTenantBranding()).isEqualTo(tenantBrandingBack);
        assertThat(tenantBrandingBack.getTenant()).isEqualTo(tenant);

        tenant.tenantBranding(null);
        assertThat(tenant.getTenantBranding()).isNull();
        assertThat(tenantBrandingBack.getTenant()).isNull();
    }
}
