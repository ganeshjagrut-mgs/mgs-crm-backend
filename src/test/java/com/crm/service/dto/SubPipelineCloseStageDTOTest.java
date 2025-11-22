package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubPipelineCloseStageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipelineCloseStageDTO.class);
        SubPipelineCloseStageDTO subPipelineCloseStageDTO1 = new SubPipelineCloseStageDTO();
        subPipelineCloseStageDTO1.setId(1L);
        SubPipelineCloseStageDTO subPipelineCloseStageDTO2 = new SubPipelineCloseStageDTO();
        assertThat(subPipelineCloseStageDTO1).isNotEqualTo(subPipelineCloseStageDTO2);
        subPipelineCloseStageDTO2.setId(subPipelineCloseStageDTO1.getId());
        assertThat(subPipelineCloseStageDTO1).isEqualTo(subPipelineCloseStageDTO2);
        subPipelineCloseStageDTO2.setId(2L);
        assertThat(subPipelineCloseStageDTO1).isNotEqualTo(subPipelineCloseStageDTO2);
        subPipelineCloseStageDTO1.setId(null);
        assertThat(subPipelineCloseStageDTO1).isNotEqualTo(subPipelineCloseStageDTO2);
    }
}
