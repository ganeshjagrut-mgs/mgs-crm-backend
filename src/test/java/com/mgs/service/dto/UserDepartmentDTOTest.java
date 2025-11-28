package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserDepartmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDepartmentDTO.class);
        UserDepartmentDTO userDepartmentDTO1 = new UserDepartmentDTO();
        userDepartmentDTO1.setId(1L);
        UserDepartmentDTO userDepartmentDTO2 = new UserDepartmentDTO();
        assertThat(userDepartmentDTO1).isNotEqualTo(userDepartmentDTO2);
        userDepartmentDTO2.setId(userDepartmentDTO1.getId());
        assertThat(userDepartmentDTO1).isEqualTo(userDepartmentDTO2);
        userDepartmentDTO2.setId(2L);
        assertThat(userDepartmentDTO1).isNotEqualTo(userDepartmentDTO2);
        userDepartmentDTO1.setId(null);
        assertThat(userDepartmentDTO1).isNotEqualTo(userDepartmentDTO2);
    }
}
