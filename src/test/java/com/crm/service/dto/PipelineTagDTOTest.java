package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelineTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelineTagDTO.class);
        PipelineTagDTO pipelineTagDTO1 = new PipelineTagDTO();
        pipelineTagDTO1.setId(1L);
        PipelineTagDTO pipelineTagDTO2 = new PipelineTagDTO();
        assertThat(pipelineTagDTO1).isNotEqualTo(pipelineTagDTO2);
        pipelineTagDTO2.setId(pipelineTagDTO1.getId());
        assertThat(pipelineTagDTO1).isEqualTo(pipelineTagDTO2);
        pipelineTagDTO2.setId(2L);
        assertThat(pipelineTagDTO1).isNotEqualTo(pipelineTagDTO2);
        pipelineTagDTO1.setId(null);
        assertThat(pipelineTagDTO1).isNotEqualTo(pipelineTagDTO2);
    }
}
