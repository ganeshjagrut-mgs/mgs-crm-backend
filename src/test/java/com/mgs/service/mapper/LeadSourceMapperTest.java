package com.mgs.service.mapper;

import static com.mgs.domain.LeadSourceAsserts.*;
import static com.mgs.domain.LeadSourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeadSourceMapperTest {

    private LeadSourceMapper leadSourceMapper;

    @BeforeEach
    void setUp() {
        leadSourceMapper = new LeadSourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLeadSourceSample1();
        var actual = leadSourceMapper.toEntity(leadSourceMapper.toDto(expected));
        assertLeadSourceAllPropertiesEquals(expected, actual);
    }
}
