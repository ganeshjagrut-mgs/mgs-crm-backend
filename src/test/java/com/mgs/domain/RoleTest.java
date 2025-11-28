package com.mgs.domain;

import static com.mgs.domain.RoleTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void tenantTest() {
        Role role = getRoleRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        role.setTenant(tenantBack);
        assertThat(role.getTenant()).isEqualTo(tenantBack);

        role.tenant(null);
        assertThat(role.getTenant()).isNull();
    }
}
