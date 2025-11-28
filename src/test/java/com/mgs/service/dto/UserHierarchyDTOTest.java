package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserHierarchyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserHierarchyDTO.class);
        UserHierarchyDTO userHierarchyDTO1 = new UserHierarchyDTO();
        userHierarchyDTO1.setId(1L);
        UserHierarchyDTO userHierarchyDTO2 = new UserHierarchyDTO();
        assertThat(userHierarchyDTO1).isNotEqualTo(userHierarchyDTO2);
        userHierarchyDTO2.setId(userHierarchyDTO1.getId());
        assertThat(userHierarchyDTO1).isEqualTo(userHierarchyDTO2);
        userHierarchyDTO2.setId(2L);
        assertThat(userHierarchyDTO1).isNotEqualTo(userHierarchyDTO2);
        userHierarchyDTO1.setId(null);
        assertThat(userHierarchyDTO1).isNotEqualTo(userHierarchyDTO2);
    }
}
