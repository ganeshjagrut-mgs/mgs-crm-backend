package com.mgs.service.mapper;

import static com.mgs.domain.TenantSubscriptionAsserts.*;
import static com.mgs.domain.TenantSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantSubscriptionMapperTest {

    private TenantSubscriptionMapper tenantSubscriptionMapper;

    @BeforeEach
    void setUp() {
        tenantSubscriptionMapper = new TenantSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTenantSubscriptionSample1();
        var actual = tenantSubscriptionMapper.toEntity(tenantSubscriptionMapper.toDto(expected));
        assertTenantSubscriptionAllPropertiesEquals(expected, actual);
    }
}
