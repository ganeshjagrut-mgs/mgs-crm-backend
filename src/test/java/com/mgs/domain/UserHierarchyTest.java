package com.mgs.domain;

import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserHierarchyTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserHierarchyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserHierarchy.class);
        UserHierarchy userHierarchy1 = getUserHierarchySample1();
        UserHierarchy userHierarchy2 = new UserHierarchy();
        assertThat(userHierarchy1).isNotEqualTo(userHierarchy2);

        userHierarchy2.setId(userHierarchy1.getId());
        assertThat(userHierarchy1).isEqualTo(userHierarchy2);

        userHierarchy2 = getUserHierarchySample2();
        assertThat(userHierarchy1).isNotEqualTo(userHierarchy2);
    }

    @Test
    void tenantTest() {
        UserHierarchy userHierarchy = getUserHierarchyRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        userHierarchy.setTenant(tenantBack);
        assertThat(userHierarchy.getTenant()).isEqualTo(tenantBack);

        userHierarchy.tenant(null);
        assertThat(userHierarchy.getTenant()).isNull();
    }

    @Test
    void parentUserTest() {
        UserHierarchy userHierarchy = getUserHierarchyRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        userHierarchy.setParentUser(userBack);
        assertThat(userHierarchy.getParentUser()).isEqualTo(userBack);

        userHierarchy.parentUser(null);
        assertThat(userHierarchy.getParentUser()).isNull();
    }

    @Test
    void childUserTest() {
        UserHierarchy userHierarchy = getUserHierarchyRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        userHierarchy.setChildUser(userBack);
        assertThat(userHierarchy.getChildUser()).isEqualTo(userBack);

        userHierarchy.childUser(null);
        assertThat(userHierarchy.getChildUser()).isNull();
    }
}
