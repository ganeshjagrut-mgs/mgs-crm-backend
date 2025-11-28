package com.mgs.service.mapper;

import static com.mgs.domain.AuditLogAsserts.*;
import static com.mgs.domain.AuditLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditLogMapperTest {

    private AuditLogMapper auditLogMapper;

    @BeforeEach
    void setUp() {
        auditLogMapper = new AuditLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAuditLogSample1();
        var actual = auditLogMapper.toEntity(auditLogMapper.toDto(expected));
        assertAuditLogAllPropertiesEquals(expected, actual);
    }
}
