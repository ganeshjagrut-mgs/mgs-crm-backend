package com.crm.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TaskAuditMapperTest {

    private TaskAuditMapper taskAuditMapper;

    @BeforeEach
    public void setUp() {
        taskAuditMapper = new TaskAuditMapperImpl();
    }
}
