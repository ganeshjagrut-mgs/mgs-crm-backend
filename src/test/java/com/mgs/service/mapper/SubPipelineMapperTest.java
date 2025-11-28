package com.mgs.service.mapper;

import static com.mgs.domain.SubPipelineAsserts.*;
import static com.mgs.domain.SubPipelineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubPipelineMapperTest {

    private SubPipelineMapper subPipelineMapper;

    @BeforeEach
    void setUp() {
        subPipelineMapper = new SubPipelineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubPipelineSample1();
        var actual = subPipelineMapper.toEntity(subPipelineMapper.toDto(expected));
        assertSubPipelineAllPropertiesEquals(expected, actual);
    }
}
