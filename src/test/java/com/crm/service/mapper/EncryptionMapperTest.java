package com.crm.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EncryptionMapperTest {

    private EncryptionMapper encryptionMapper;

    @BeforeEach
    public void setUp() {
        encryptionMapper = new EncryptionMapperImpl();
    }
}
