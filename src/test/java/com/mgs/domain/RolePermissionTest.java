package com.mgs.domain;

import static com.mgs.domain.PermissionModuleTestSamples.*;
import static com.mgs.domain.RolePermissionTestSamples.*;
import static com.mgs.domain.RoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RolePermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolePermission.class);
        RolePermission rolePermission1 = getRolePermissionSample1();
        RolePermission rolePermission2 = new RolePermission();
        assertThat(rolePermission1).isNotEqualTo(rolePermission2);

        rolePermission2.setId(rolePermission1.getId());
        assertThat(rolePermission1).isEqualTo(rolePermission2);

        rolePermission2 = getRolePermissionSample2();
        assertThat(rolePermission1).isNotEqualTo(rolePermission2);
    }

    @Test
    void roleTest() {
        RolePermission rolePermission = getRolePermissionRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        rolePermission.setRole(roleBack);
        assertThat(rolePermission.getRole()).isEqualTo(roleBack);

        rolePermission.role(null);
        assertThat(rolePermission.getRole()).isNull();
    }

    @Test
    void moduleTest() {
        RolePermission rolePermission = getRolePermissionRandomSampleGenerator();
        PermissionModule permissionModuleBack = getPermissionModuleRandomSampleGenerator();

        rolePermission.setModule(permissionModuleBack);
        assertThat(rolePermission.getModule()).isEqualTo(permissionModuleBack);

        rolePermission.module(null);
        assertThat(rolePermission.getModule()).isNull();
    }
}
