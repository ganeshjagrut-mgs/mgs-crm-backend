package com.mgs.service.mapper;

import static com.mgs.domain.UserHierarchyAsserts.*;
import static com.mgs.domain.UserHierarchyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserHierarchyMapperTest {

    private UserHierarchyMapper userHierarchyMapper;

    @BeforeEach
    void setUp() {
        userHierarchyMapper = new UserHierarchyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserHierarchySample1();
        var actual = userHierarchyMapper.toEntity(userHierarchyMapper.toDto(expected));
        assertUserHierarchyAllPropertiesEquals(expected, actual);
    }
}
