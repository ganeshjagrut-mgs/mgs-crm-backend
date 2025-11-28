package com.mgs.domain;

import static com.mgs.domain.TenantBrandingTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantBrandingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantBranding.class);
        TenantBranding tenantBranding1 = getTenantBrandingSample1();
        TenantBranding tenantBranding2 = new TenantBranding();
        assertThat(tenantBranding1).isNotEqualTo(tenantBranding2);

        tenantBranding2.setId(tenantBranding1.getId());
        assertThat(tenantBranding1).isEqualTo(tenantBranding2);

        tenantBranding2 = getTenantBrandingSample2();
        assertThat(tenantBranding1).isNotEqualTo(tenantBranding2);
    }

    @Test
    void tenantTest() {
        TenantBranding tenantBranding = getTenantBrandingRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        tenantBranding.setTenant(tenantBack);
        assertThat(tenantBranding.getTenant()).isEqualTo(tenantBack);

        tenantBranding.tenant(null);
        assertThat(tenantBranding.getTenant()).isNull();
    }
}
