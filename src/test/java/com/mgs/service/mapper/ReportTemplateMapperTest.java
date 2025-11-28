package com.mgs.service.mapper;

import static com.mgs.domain.ReportTemplateAsserts.*;
import static com.mgs.domain.ReportTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportTemplateMapperTest {

    private ReportTemplateMapper reportTemplateMapper;

    @BeforeEach
    void setUp() {
        reportTemplateMapper = new ReportTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportTemplateSample1();
        var actual = reportTemplateMapper.toEntity(reportTemplateMapper.toDto(expected));
        assertReportTemplateAllPropertiesEquals(expected, actual);
    }
}
