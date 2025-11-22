package com.crm.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TenantMapperTest {

    private TenantMapper tenantMapper;

    @BeforeEach
    public void setUp() {
        tenantMapper = new TenantMapperImpl();
    }
}
