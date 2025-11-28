package com.mgs.service.mapper;

import static com.mgs.domain.ReportRunAsserts.*;
import static com.mgs.domain.ReportRunTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportRunMapperTest {

    private ReportRunMapper reportRunMapper;

    @BeforeEach
    void setUp() {
        reportRunMapper = new ReportRunMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportRunSample1();
        var actual = reportRunMapper.toEntity(reportRunMapper.toDto(expected));
        assertReportRunAllPropertiesEquals(expected, actual);
    }
}
