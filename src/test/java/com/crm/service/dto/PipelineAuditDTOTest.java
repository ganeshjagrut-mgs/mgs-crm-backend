package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelineAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelineAuditDTO.class);
        PipelineAuditDTO pipelineAuditDTO1 = new PipelineAuditDTO();
        pipelineAuditDTO1.setId(1L);
        PipelineAuditDTO pipelineAuditDTO2 = new PipelineAuditDTO();
        assertThat(pipelineAuditDTO1).isNotEqualTo(pipelineAuditDTO2);
        pipelineAuditDTO2.setId(pipelineAuditDTO1.getId());
        assertThat(pipelineAuditDTO1).isEqualTo(pipelineAuditDTO2);
        pipelineAuditDTO2.setId(2L);
        assertThat(pipelineAuditDTO1).isNotEqualTo(pipelineAuditDTO2);
        pipelineAuditDTO1.setId(null);
        assertThat(pipelineAuditDTO1).isNotEqualTo(pipelineAuditDTO2);
    }
}
