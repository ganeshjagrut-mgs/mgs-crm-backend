package com.mgs.service.mapper;

import static com.mgs.domain.TenantBrandingAsserts.*;
import static com.mgs.domain.TenantBrandingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantBrandingMapperTest {

    private TenantBrandingMapper tenantBrandingMapper;

    @BeforeEach
    void setUp() {
        tenantBrandingMapper = new TenantBrandingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTenantBrandingSample1();
        var actual = tenantBrandingMapper.toEntity(tenantBrandingMapper.toDto(expected));
        assertTenantBrandingAllPropertiesEquals(expected, actual);
    }
}
