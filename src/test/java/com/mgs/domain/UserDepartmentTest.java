package com.mgs.domain;

import static com.mgs.domain.DepartmentTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserDepartmentTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserDepartmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDepartment.class);
        UserDepartment userDepartment1 = getUserDepartmentSample1();
        UserDepartment userDepartment2 = new UserDepartment();
        assertThat(userDepartment1).isNotEqualTo(userDepartment2);

        userDepartment2.setId(userDepartment1.getId());
        assertThat(userDepartment1).isEqualTo(userDepartment2);

        userDepartment2 = getUserDepartmentSample2();
        assertThat(userDepartment1).isNotEqualTo(userDepartment2);
    }

    @Test
    void tenantTest() {
        UserDepartment userDepartment = getUserDepartmentRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        userDepartment.setTenant(tenantBack);
        assertThat(userDepartment.getTenant()).isEqualTo(tenantBack);

        userDepartment.tenant(null);
        assertThat(userDepartment.getTenant()).isNull();
    }

    @Test
    void userTest() {
        UserDepartment userDepartment = getUserDepartmentRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        userDepartment.setUser(userBack);
        assertThat(userDepartment.getUser()).isEqualTo(userBack);

        userDepartment.user(null);
        assertThat(userDepartment.getUser()).isNull();
    }

    @Test
    void departmentTest() {
        UserDepartment userDepartment = getUserDepartmentRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        userDepartment.setDepartment(departmentBack);
        assertThat(userDepartment.getDepartment()).isEqualTo(departmentBack);

        userDepartment.department(null);
        assertThat(userDepartment.getDepartment()).isNull();
    }
}
