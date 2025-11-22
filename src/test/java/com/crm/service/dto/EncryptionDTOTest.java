package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EncryptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EncryptionDTO.class);
        EncryptionDTO encryptionDTO1 = new EncryptionDTO();
        encryptionDTO1.setId(1L);
        EncryptionDTO encryptionDTO2 = new EncryptionDTO();
        assertThat(encryptionDTO1).isNotEqualTo(encryptionDTO2);
        encryptionDTO2.setId(encryptionDTO1.getId());
        assertThat(encryptionDTO1).isEqualTo(encryptionDTO2);
        encryptionDTO2.setId(2L);
        assertThat(encryptionDTO1).isNotEqualTo(encryptionDTO2);
        encryptionDTO1.setId(null);
        assertThat(encryptionDTO1).isNotEqualTo(encryptionDTO2);
    }
}
