package com.mgs.domain;

import static com.mgs.domain.RoleTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserRoleTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRole.class);
        UserRole userRole1 = getUserRoleSample1();
        UserRole userRole2 = new UserRole();
        assertThat(userRole1).isNotEqualTo(userRole2);

        userRole2.setId(userRole1.getId());
        assertThat(userRole1).isEqualTo(userRole2);

        userRole2 = getUserRoleSample2();
        assertThat(userRole1).isNotEqualTo(userRole2);
    }

    @Test
    void tenantTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        userRole.setTenant(tenantBack);
        assertThat(userRole.getTenant()).isEqualTo(tenantBack);

        userRole.tenant(null);
        assertThat(userRole.getTenant()).isNull();
    }

    @Test
    void userTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        userRole.setUser(userBack);
        assertThat(userRole.getUser()).isEqualTo(userBack);

        userRole.user(null);
        assertThat(userRole.getUser()).isNull();
    }

    @Test
    void roleTest() {
        UserRole userRole = getUserRoleRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        userRole.setRole(roleBack);
        assertThat(userRole.getRole()).isEqualTo(roleBack);

        userRole.role(null);
        assertThat(userRole.getRole()).isNull();
    }
}
