package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeadSourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeadSourceDTO.class);
        LeadSourceDTO leadSourceDTO1 = new LeadSourceDTO();
        leadSourceDTO1.setId(1L);
        LeadSourceDTO leadSourceDTO2 = new LeadSourceDTO();
        assertThat(leadSourceDTO1).isNotEqualTo(leadSourceDTO2);
        leadSourceDTO2.setId(leadSourceDTO1.getId());
        assertThat(leadSourceDTO1).isEqualTo(leadSourceDTO2);
        leadSourceDTO2.setId(2L);
        assertThat(leadSourceDTO1).isNotEqualTo(leadSourceDTO2);
        leadSourceDTO1.setId(null);
        assertThat(leadSourceDTO1).isNotEqualTo(leadSourceDTO2);
    }
}
