package com.mgs.domain;

import static com.mgs.domain.LeadSourceTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeadSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeadSource.class);
        LeadSource leadSource1 = getLeadSourceSample1();
        LeadSource leadSource2 = new LeadSource();
        assertThat(leadSource1).isNotEqualTo(leadSource2);

        leadSource2.setId(leadSource1.getId());
        assertThat(leadSource1).isEqualTo(leadSource2);

        leadSource2 = getLeadSourceSample2();
        assertThat(leadSource1).isNotEqualTo(leadSource2);
    }

    @Test
    void tenantTest() {
        LeadSource leadSource = getLeadSourceRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        leadSource.setTenant(tenantBack);
        assertThat(leadSource.getTenant()).isEqualTo(tenantBack);

        leadSource.tenant(null);
        assertThat(leadSource.getTenant()).isNull();
    }
}
