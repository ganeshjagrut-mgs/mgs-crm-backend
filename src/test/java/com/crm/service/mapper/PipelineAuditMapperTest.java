package com.crm.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PipelineAuditMapperTest {

    private PipelineAuditMapper pipelineAuditMapper;

    @BeforeEach
    public void setUp() {
        pipelineAuditMapper = new PipelineAuditMapperImpl();
    }
}
