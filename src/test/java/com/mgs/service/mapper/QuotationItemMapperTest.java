package com.mgs.service.mapper;

import static com.mgs.domain.QuotationItemAsserts.*;
import static com.mgs.domain.QuotationItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuotationItemMapperTest {

    private QuotationItemMapper quotationItemMapper;

    @BeforeEach
    void setUp() {
        quotationItemMapper = new QuotationItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuotationItemSample1();
        var actual = quotationItemMapper.toEntity(quotationItemMapper.toDto(expected));
        assertQuotationItemAllPropertiesEquals(expected, actual);
    }
}
