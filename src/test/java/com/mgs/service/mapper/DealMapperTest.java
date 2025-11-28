package com.mgs.service.mapper;

import static com.mgs.domain.DealAsserts.*;
import static com.mgs.domain.DealTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealMapperTest {

    private DealMapper dealMapper;

    @BeforeEach
    void setUp() {
        dealMapper = new DealMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDealSample1();
        var actual = dealMapper.toEntity(dealMapper.toDto(expected));
        assertDealAllPropertiesEquals(expected, actual);
    }
}
