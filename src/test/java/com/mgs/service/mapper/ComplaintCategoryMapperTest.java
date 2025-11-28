package com.mgs.service.mapper;

import static com.mgs.domain.ComplaintCategoryAsserts.*;
import static com.mgs.domain.ComplaintCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComplaintCategoryMapperTest {

    private ComplaintCategoryMapper complaintCategoryMapper;

    @BeforeEach
    void setUp() {
        complaintCategoryMapper = new ComplaintCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getComplaintCategorySample1();
        var actual = complaintCategoryMapper.toEntity(complaintCategoryMapper.toDto(expected));
        assertComplaintCategoryAllPropertiesEquals(expected, actual);
    }
}
