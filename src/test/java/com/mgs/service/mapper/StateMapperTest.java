package com.mgs.service.mapper;

import static com.mgs.domain.StateAsserts.*;
import static com.mgs.domain.StateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateMapperTest {

    private StateMapper stateMapper;

    @BeforeEach
    void setUp() {
        stateMapper = new StateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStateSample1();
        var actual = stateMapper.toEntity(stateMapper.toDto(expected));
        assertStateAllPropertiesEquals(expected, actual);
    }
}
