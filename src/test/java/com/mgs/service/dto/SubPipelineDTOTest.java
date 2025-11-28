package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubPipelineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipelineDTO.class);
        SubPipelineDTO subPipelineDTO1 = new SubPipelineDTO();
        subPipelineDTO1.setId(1L);
        SubPipelineDTO subPipelineDTO2 = new SubPipelineDTO();
        assertThat(subPipelineDTO1).isNotEqualTo(subPipelineDTO2);
        subPipelineDTO2.setId(subPipelineDTO1.getId());
        assertThat(subPipelineDTO1).isEqualTo(subPipelineDTO2);
        subPipelineDTO2.setId(2L);
        assertThat(subPipelineDTO1).isNotEqualTo(subPipelineDTO2);
        subPipelineDTO1.setId(null);
        assertThat(subPipelineDTO1).isNotEqualTo(subPipelineDTO2);
    }
}
