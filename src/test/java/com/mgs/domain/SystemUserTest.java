package com.mgs.domain;

import static com.mgs.domain.SystemUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemUser.class);
        SystemUser systemUser1 = getSystemUserSample1();
        SystemUser systemUser2 = new SystemUser();
        assertThat(systemUser1).isNotEqualTo(systemUser2);

        systemUser2.setId(systemUser1.getId());
        assertThat(systemUser1).isEqualTo(systemUser2);

        systemUser2 = getSystemUserSample2();
        assertThat(systemUser1).isNotEqualTo(systemUser2);
    }
}
