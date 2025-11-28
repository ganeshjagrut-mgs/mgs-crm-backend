package com.mgs.service.mapper;

import static com.mgs.domain.EventTaskAssignmentAsserts.*;
import static com.mgs.domain.EventTaskAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventTaskAssignmentMapperTest {

    private EventTaskAssignmentMapper eventTaskAssignmentMapper;

    @BeforeEach
    void setUp() {
        eventTaskAssignmentMapper = new EventTaskAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventTaskAssignmentSample1();
        var actual = eventTaskAssignmentMapper.toEntity(eventTaskAssignmentMapper.toDto(expected));
        assertEventTaskAssignmentAllPropertiesEquals(expected, actual);
    }
}
