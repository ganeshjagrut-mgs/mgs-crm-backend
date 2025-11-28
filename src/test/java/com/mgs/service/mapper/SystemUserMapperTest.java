package com.mgs.service.mapper;

import static com.mgs.domain.SystemUserAsserts.*;
import static com.mgs.domain.SystemUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemUserMapperTest {

    private SystemUserMapper systemUserMapper;

    @BeforeEach
    void setUp() {
        systemUserMapper = new SystemUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemUserSample1();
        var actual = systemUserMapper.toEntity(systemUserMapper.toDto(expected));
        assertSystemUserAllPropertiesEquals(expected, actual);
    }
}
