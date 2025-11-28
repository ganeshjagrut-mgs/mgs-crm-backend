package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemUserDTO.class);
        SystemUserDTO systemUserDTO1 = new SystemUserDTO();
        systemUserDTO1.setId(1L);
        SystemUserDTO systemUserDTO2 = new SystemUserDTO();
        assertThat(systemUserDTO1).isNotEqualTo(systemUserDTO2);
        systemUserDTO2.setId(systemUserDTO1.getId());
        assertThat(systemUserDTO1).isEqualTo(systemUserDTO2);
        systemUserDTO2.setId(2L);
        assertThat(systemUserDTO1).isNotEqualTo(systemUserDTO2);
        systemUserDTO1.setId(null);
        assertThat(systemUserDTO1).isNotEqualTo(systemUserDTO2);
    }
}
