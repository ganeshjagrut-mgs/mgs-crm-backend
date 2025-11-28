package com.mgs.service.mapper;

import static com.mgs.domain.TenantAsserts.*;
import static com.mgs.domain.TenantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantMapperTest {

    private TenantMapper tenantMapper;

    @BeforeEach
    void setUp() {
        tenantMapper = new TenantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTenantSample1();
        var actual = tenantMapper.toEntity(tenantMapper.toDto(expected));
        assertTenantAllPropertiesEquals(expected, actual);
    }
}
