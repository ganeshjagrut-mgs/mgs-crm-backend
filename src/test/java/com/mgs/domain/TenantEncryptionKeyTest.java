package com.mgs.domain;

import static com.mgs.domain.TenantEncryptionKeyTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantEncryptionKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantEncryptionKey.class);
        TenantEncryptionKey tenantEncryptionKey1 = getTenantEncryptionKeySample1();
        TenantEncryptionKey tenantEncryptionKey2 = new TenantEncryptionKey();
        assertThat(tenantEncryptionKey1).isNotEqualTo(tenantEncryptionKey2);

        tenantEncryptionKey2.setId(tenantEncryptionKey1.getId());
        assertThat(tenantEncryptionKey1).isEqualTo(tenantEncryptionKey2);

        tenantEncryptionKey2 = getTenantEncryptionKeySample2();
        assertThat(tenantEncryptionKey1).isNotEqualTo(tenantEncryptionKey2);
    }

    @Test
    void tenantTest() {
        TenantEncryptionKey tenantEncryptionKey = getTenantEncryptionKeyRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        tenantEncryptionKey.setTenant(tenantBack);
        assertThat(tenantEncryptionKey.getTenant()).isEqualTo(tenantBack);

        tenantEncryptionKey.tenant(null);
        assertThat(tenantEncryptionKey.getTenant()).isNull();
    }
}
