package com.mgs.service.mapper;

import static com.mgs.domain.TenantEncryptionKeyAsserts.*;
import static com.mgs.domain.TenantEncryptionKeyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantEncryptionKeyMapperTest {

    private TenantEncryptionKeyMapper tenantEncryptionKeyMapper;

    @BeforeEach
    void setUp() {
        tenantEncryptionKeyMapper = new TenantEncryptionKeyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTenantEncryptionKeySample1();
        var actual = tenantEncryptionKeyMapper.toEntity(tenantEncryptionKeyMapper.toDto(expected));
        assertTenantEncryptionKeyAllPropertiesEquals(expected, actual);
    }
}
