package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionModuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionModuleDTO.class);
        PermissionModuleDTO permissionModuleDTO1 = new PermissionModuleDTO();
        permissionModuleDTO1.setId(1L);
        PermissionModuleDTO permissionModuleDTO2 = new PermissionModuleDTO();
        assertThat(permissionModuleDTO1).isNotEqualTo(permissionModuleDTO2);
        permissionModuleDTO2.setId(permissionModuleDTO1.getId());
        assertThat(permissionModuleDTO1).isEqualTo(permissionModuleDTO2);
        permissionModuleDTO2.setId(2L);
        assertThat(permissionModuleDTO1).isNotEqualTo(permissionModuleDTO2);
        permissionModuleDTO1.setId(null);
        assertThat(permissionModuleDTO1).isNotEqualTo(permissionModuleDTO2);
    }
}
