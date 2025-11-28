package com.mgs.domain;

import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = getUserSample1();
        User user2 = new User();
        assertThat(user1).isNotEqualTo(user2);

        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);

        user2 = getUserSample2();
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void tenantTest() {
        User user = getUserRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        user.setTenant(tenantBack);
        assertThat(user.getTenant()).isEqualTo(tenantBack);

        user.tenant(null);
        assertThat(user.getTenant()).isNull();
    }
}
