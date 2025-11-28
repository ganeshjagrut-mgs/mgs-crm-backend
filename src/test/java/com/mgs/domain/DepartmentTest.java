package com.mgs.domain;

import static com.mgs.domain.DepartmentTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = getDepartmentSample1();
        Department department2 = new Department();
        assertThat(department1).isNotEqualTo(department2);

        department2.setId(department1.getId());
        assertThat(department1).isEqualTo(department2);

        department2 = getDepartmentSample2();
        assertThat(department1).isNotEqualTo(department2);
    }

    @Test
    void tenantTest() {
        Department department = getDepartmentRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        department.setTenant(tenantBack);
        assertThat(department.getTenant()).isEqualTo(tenantBack);

        department.tenant(null);
        assertThat(department.getTenant()).isNull();
    }

    @Test
    void headUserTest() {
        Department department = getDepartmentRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        department.setHeadUser(userBack);
        assertThat(department.getHeadUser()).isEqualTo(userBack);

        department.headUser(null);
        assertThat(department.getHeadUser()).isNull();
    }
}
