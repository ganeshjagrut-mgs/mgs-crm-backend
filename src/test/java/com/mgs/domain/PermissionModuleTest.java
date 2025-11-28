package com.mgs.domain;

import static com.mgs.domain.PermissionModuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionModuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionModule.class);
        PermissionModule permissionModule1 = getPermissionModuleSample1();
        PermissionModule permissionModule2 = new PermissionModule();
        assertThat(permissionModule1).isNotEqualTo(permissionModule2);

        permissionModule2.setId(permissionModule1.getId());
        assertThat(permissionModule1).isEqualTo(permissionModule2);

        permissionModule2 = getPermissionModuleSample2();
        assertThat(permissionModule1).isNotEqualTo(permissionModule2);
    }
}
