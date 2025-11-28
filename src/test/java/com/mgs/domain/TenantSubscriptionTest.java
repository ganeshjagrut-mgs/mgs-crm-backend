package com.mgs.domain;

import static com.mgs.domain.PlanTestSamples.*;
import static com.mgs.domain.TenantSubscriptionTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantSubscription.class);
        TenantSubscription tenantSubscription1 = getTenantSubscriptionSample1();
        TenantSubscription tenantSubscription2 = new TenantSubscription();
        assertThat(tenantSubscription1).isNotEqualTo(tenantSubscription2);

        tenantSubscription2.setId(tenantSubscription1.getId());
        assertThat(tenantSubscription1).isEqualTo(tenantSubscription2);

        tenantSubscription2 = getTenantSubscriptionSample2();
        assertThat(tenantSubscription1).isNotEqualTo(tenantSubscription2);
    }

    @Test
    void tenantTest() {
        TenantSubscription tenantSubscription = getTenantSubscriptionRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        tenantSubscription.setTenant(tenantBack);
        assertThat(tenantSubscription.getTenant()).isEqualTo(tenantBack);

        tenantSubscription.tenant(null);
        assertThat(tenantSubscription.getTenant()).isNull();
    }

    @Test
    void planTest() {
        TenantSubscription tenantSubscription = getTenantSubscriptionRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        tenantSubscription.setPlan(planBack);
        assertThat(tenantSubscription.getPlan()).isEqualTo(planBack);

        tenantSubscription.plan(null);
        assertThat(tenantSubscription.getPlan()).isNull();
    }
}
