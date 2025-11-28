package com.mgs.service.mapper;

import static com.mgs.domain.UserDepartmentAsserts.*;
import static com.mgs.domain.UserDepartmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDepartmentMapperTest {

    private UserDepartmentMapper userDepartmentMapper;

    @BeforeEach
    void setUp() {
        userDepartmentMapper = new UserDepartmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserDepartmentSample1();
        var actual = userDepartmentMapper.toEntity(userDepartmentMapper.toDto(expected));
        assertUserDepartmentAllPropertiesEquals(expected, actual);
    }
}
