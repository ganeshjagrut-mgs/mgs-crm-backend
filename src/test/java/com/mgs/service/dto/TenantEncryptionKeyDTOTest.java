package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantEncryptionKeyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantEncryptionKeyDTO.class);
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO1 = new TenantEncryptionKeyDTO();
        tenantEncryptionKeyDTO1.setId(1L);
        TenantEncryptionKeyDTO tenantEncryptionKeyDTO2 = new TenantEncryptionKeyDTO();
        assertThat(tenantEncryptionKeyDTO1).isNotEqualTo(tenantEncryptionKeyDTO2);
        tenantEncryptionKeyDTO2.setId(tenantEncryptionKeyDTO1.getId());
        assertThat(tenantEncryptionKeyDTO1).isEqualTo(tenantEncryptionKeyDTO2);
        tenantEncryptionKeyDTO2.setId(2L);
        assertThat(tenantEncryptionKeyDTO1).isNotEqualTo(tenantEncryptionKeyDTO2);
        tenantEncryptionKeyDTO1.setId(null);
        assertThat(tenantEncryptionKeyDTO1).isNotEqualTo(tenantEncryptionKeyDTO2);
    }
}
