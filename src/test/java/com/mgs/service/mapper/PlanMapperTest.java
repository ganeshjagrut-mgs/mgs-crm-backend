package com.mgs.service.mapper;

import static com.mgs.domain.PlanAsserts.*;
import static com.mgs.domain.PlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanMapperTest {

    private PlanMapper planMapper;

    @BeforeEach
    void setUp() {
        planMapper = new PlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanSample1();
        var actual = planMapper.toEntity(planMapper.toDto(expected));
        assertPlanAllPropertiesEquals(expected, actual);
    }
}
