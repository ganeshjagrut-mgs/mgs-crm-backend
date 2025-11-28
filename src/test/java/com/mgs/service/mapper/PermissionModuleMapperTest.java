package com.mgs.service.mapper;

import static com.mgs.domain.PermissionModuleAsserts.*;
import static com.mgs.domain.PermissionModuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionModuleMapperTest {

    private PermissionModuleMapper permissionModuleMapper;

    @BeforeEach
    void setUp() {
        permissionModuleMapper = new PermissionModuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissionModuleSample1();
        var actual = permissionModuleMapper.toEntity(permissionModuleMapper.toDto(expected));
        assertPermissionModuleAllPropertiesEquals(expected, actual);
    }
}
