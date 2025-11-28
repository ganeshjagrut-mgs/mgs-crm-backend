package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantSubscriptionDTO.class);
        TenantSubscriptionDTO tenantSubscriptionDTO1 = new TenantSubscriptionDTO();
        tenantSubscriptionDTO1.setId(1L);
        TenantSubscriptionDTO tenantSubscriptionDTO2 = new TenantSubscriptionDTO();
        assertThat(tenantSubscriptionDTO1).isNotEqualTo(tenantSubscriptionDTO2);
        tenantSubscriptionDTO2.setId(tenantSubscriptionDTO1.getId());
        assertThat(tenantSubscriptionDTO1).isEqualTo(tenantSubscriptionDTO2);
        tenantSubscriptionDTO2.setId(2L);
        assertThat(tenantSubscriptionDTO1).isNotEqualTo(tenantSubscriptionDTO2);
        tenantSubscriptionDTO1.setId(null);
        assertThat(tenantSubscriptionDTO1).isNotEqualTo(tenantSubscriptionDTO2);
    }
}
