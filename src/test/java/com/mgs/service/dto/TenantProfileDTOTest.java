package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantProfileDTO.class);
        TenantProfileDTO tenantProfileDTO1 = new TenantProfileDTO();
        tenantProfileDTO1.setId(1L);
        TenantProfileDTO tenantProfileDTO2 = new TenantProfileDTO();
        assertThat(tenantProfileDTO1).isNotEqualTo(tenantProfileDTO2);
        tenantProfileDTO2.setId(tenantProfileDTO1.getId());
        assertThat(tenantProfileDTO1).isEqualTo(tenantProfileDTO2);
        tenantProfileDTO2.setId(2L);
        assertThat(tenantProfileDTO1).isNotEqualTo(tenantProfileDTO2);
        tenantProfileDTO1.setId(null);
        assertThat(tenantProfileDTO1).isNotEqualTo(tenantProfileDTO2);
    }
}
