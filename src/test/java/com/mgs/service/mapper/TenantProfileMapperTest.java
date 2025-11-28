package com.mgs.service.mapper;

import static com.mgs.domain.TenantProfileAsserts.*;
import static com.mgs.domain.TenantProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantProfileMapperTest {

    private TenantProfileMapper tenantProfileMapper;

    @BeforeEach
    void setUp() {
        tenantProfileMapper = new TenantProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTenantProfileSample1();
        var actual = tenantProfileMapper.toEntity(tenantProfileMapper.toDto(expected));
        assertTenantProfileAllPropertiesEquals(expected, actual);
    }
}
