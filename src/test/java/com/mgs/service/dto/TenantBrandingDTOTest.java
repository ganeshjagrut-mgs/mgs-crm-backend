package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantBrandingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantBrandingDTO.class);
        TenantBrandingDTO tenantBrandingDTO1 = new TenantBrandingDTO();
        tenantBrandingDTO1.setId(1L);
        TenantBrandingDTO tenantBrandingDTO2 = new TenantBrandingDTO();
        assertThat(tenantBrandingDTO1).isNotEqualTo(tenantBrandingDTO2);
        tenantBrandingDTO2.setId(tenantBrandingDTO1.getId());
        assertThat(tenantBrandingDTO1).isEqualTo(tenantBrandingDTO2);
        tenantBrandingDTO2.setId(2L);
        assertThat(tenantBrandingDTO1).isNotEqualTo(tenantBrandingDTO2);
        tenantBrandingDTO1.setId(null);
        assertThat(tenantBrandingDTO1).isNotEqualTo(tenantBrandingDTO2);
    }
}
