package com.mgs.service.mapper;

import static com.mgs.domain.PipelineAsserts.*;
import static com.mgs.domain.PipelineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PipelineMapperTest {

    private PipelineMapper pipelineMapper;

    @BeforeEach
    void setUp() {
        pipelineMapper = new PipelineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPipelineSample1();
        var actual = pipelineMapper.toEntity(pipelineMapper.toDto(expected));
        assertPipelineAllPropertiesEquals(expected, actual);
    }
}
