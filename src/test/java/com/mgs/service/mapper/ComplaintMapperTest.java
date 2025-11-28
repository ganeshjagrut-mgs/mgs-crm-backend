package com.mgs.service.mapper;

import static com.mgs.domain.ComplaintAsserts.*;
import static com.mgs.domain.ComplaintTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComplaintMapperTest {

    private ComplaintMapper complaintMapper;

    @BeforeEach
    void setUp() {
        complaintMapper = new ComplaintMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getComplaintSample1();
        var actual = complaintMapper.toEntity(complaintMapper.toDto(expected));
        assertComplaintAllPropertiesEquals(expected, actual);
    }
}
