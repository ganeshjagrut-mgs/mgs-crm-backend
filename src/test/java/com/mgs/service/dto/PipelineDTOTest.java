package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelineDTO.class);
        PipelineDTO pipelineDTO1 = new PipelineDTO();
        pipelineDTO1.setId(1L);
        PipelineDTO pipelineDTO2 = new PipelineDTO();
        assertThat(pipelineDTO1).isNotEqualTo(pipelineDTO2);
        pipelineDTO2.setId(pipelineDTO1.getId());
        assertThat(pipelineDTO1).isEqualTo(pipelineDTO2);
        pipelineDTO2.setId(2L);
        assertThat(pipelineDTO1).isNotEqualTo(pipelineDTO2);
        pipelineDTO1.setId(null);
        assertThat(pipelineDTO1).isNotEqualTo(pipelineDTO2);
    }
}
