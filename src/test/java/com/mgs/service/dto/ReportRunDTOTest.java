package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportRunDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportRunDTO.class);
        ReportRunDTO reportRunDTO1 = new ReportRunDTO();
        reportRunDTO1.setId(1L);
        ReportRunDTO reportRunDTO2 = new ReportRunDTO();
        assertThat(reportRunDTO1).isNotEqualTo(reportRunDTO2);
        reportRunDTO2.setId(reportRunDTO1.getId());
        assertThat(reportRunDTO1).isEqualTo(reportRunDTO2);
        reportRunDTO2.setId(2L);
        assertThat(reportRunDTO1).isNotEqualTo(reportRunDTO2);
        reportRunDTO1.setId(null);
        assertThat(reportRunDTO1).isNotEqualTo(reportRunDTO2);
    }
}
