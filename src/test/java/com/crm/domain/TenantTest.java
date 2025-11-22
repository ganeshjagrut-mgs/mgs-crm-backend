package com.crm.domain;

import static com.crm.domain.AddressTestSamples.*;
import static com.crm.domain.EncryptionTestSamples.*;
import static com.crm.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tenant.class);
        Tenant tenant1 = getTenantSample1();
        Tenant tenant2 = new Tenant();
        assertThat(tenant1).isNotEqualTo(tenant2);

        tenant2.setId(tenant1.getId());
        assertThat(tenant1).isEqualTo(tenant2);

        tenant2 = getTenantSample2();
        assertThat(tenant1).isNotEqualTo(tenant2);
    }

    @Test
    void addressesTest() throws Exception {
        Tenant tenant = getTenantRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        tenant.addAddresses(addressBack);
        assertThat(tenant.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getTenant()).isEqualTo(tenant);

        tenant.removeAddresses(addressBack);
        assertThat(tenant.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getTenant()).isNull();

        tenant.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(tenant.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getTenant()).isEqualTo(tenant);

        tenant.setAddresses(new HashSet<>());
        assertThat(tenant.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getTenant()).isNull();
    }

    @Test
    void encryptionTest() throws Exception {
        Tenant tenant = getTenantRandomSampleGenerator();
        Encryption encryptionBack = getEncryptionRandomSampleGenerator();

        tenant.setEncryption(encryptionBack);
        assertThat(tenant.getEncryption()).isEqualTo(encryptionBack);
        assertThat(encryptionBack.getTenant()).isEqualTo(tenant);

        tenant.encryption(null);
        assertThat(tenant.getEncryption()).isNull();
        assertThat(encryptionBack.getTenant()).isNull();
    }
}
