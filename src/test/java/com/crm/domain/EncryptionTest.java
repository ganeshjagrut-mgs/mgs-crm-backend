package com.crm.domain;

import static com.crm.domain.EncryptionTestSamples.*;
import static com.crm.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EncryptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Encryption.class);
        Encryption encryption1 = getEncryptionSample1();
        Encryption encryption2 = new Encryption();
        assertThat(encryption1).isNotEqualTo(encryption2);

        encryption2.setId(encryption1.getId());
        assertThat(encryption1).isEqualTo(encryption2);

        encryption2 = getEncryptionSample2();
        assertThat(encryption1).isNotEqualTo(encryption2);
    }

    @Test
    void tenantTest() throws Exception {
        Encryption encryption = getEncryptionRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        encryption.setTenant(tenantBack);
        assertThat(encryption.getTenant()).isEqualTo(tenantBack);

        encryption.tenant(null);
        assertThat(encryption.getTenant()).isNull();
    }
}
