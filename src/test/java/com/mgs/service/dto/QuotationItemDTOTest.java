package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationItemDTO.class);
        QuotationItemDTO quotationItemDTO1 = new QuotationItemDTO();
        quotationItemDTO1.setId(1L);
        QuotationItemDTO quotationItemDTO2 = new QuotationItemDTO();
        assertThat(quotationItemDTO1).isNotEqualTo(quotationItemDTO2);
        quotationItemDTO2.setId(quotationItemDTO1.getId());
        assertThat(quotationItemDTO1).isEqualTo(quotationItemDTO2);
        quotationItemDTO2.setId(2L);
        assertThat(quotationItemDTO1).isNotEqualTo(quotationItemDTO2);
        quotationItemDTO1.setId(null);
        assertThat(quotationItemDTO1).isNotEqualTo(quotationItemDTO2);
    }
}
