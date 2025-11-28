package com.mgs.service.mapper;

import static com.mgs.domain.TaskTypeAsserts.*;
import static com.mgs.domain.TaskTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTypeMapperTest {

    private TaskTypeMapper taskTypeMapper;

    @BeforeEach
    void setUp() {
        taskTypeMapper = new TaskTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTaskTypeSample1();
        var actual = taskTypeMapper.toEntity(taskTypeMapper.toDto(expected));
        assertTaskTypeAllPropertiesEquals(expected, actual);
    }
}
