package com.crm.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CustomerCompanyMapperTest {

    private CustomerCompanyMapper customerCompanyMapper;

    @BeforeEach
    public void setUp() {
        customerCompanyMapper = new CustomerCompanyMapperImpl();
    }
}
