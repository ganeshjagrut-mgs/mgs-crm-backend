package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubPipelineOpenStageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipelineOpenStageDTO.class);
        SubPipelineOpenStageDTO subPipelineOpenStageDTO1 = new SubPipelineOpenStageDTO();
        subPipelineOpenStageDTO1.setId(1L);
        SubPipelineOpenStageDTO subPipelineOpenStageDTO2 = new SubPipelineOpenStageDTO();
        assertThat(subPipelineOpenStageDTO1).isNotEqualTo(subPipelineOpenStageDTO2);
        subPipelineOpenStageDTO2.setId(subPipelineOpenStageDTO1.getId());
        assertThat(subPipelineOpenStageDTO1).isEqualTo(subPipelineOpenStageDTO2);
        subPipelineOpenStageDTO2.setId(2L);
        assertThat(subPipelineOpenStageDTO1).isNotEqualTo(subPipelineOpenStageDTO2);
        subPipelineOpenStageDTO1.setId(null);
        assertThat(subPipelineOpenStageDTO1).isNotEqualTo(subPipelineOpenStageDTO2);
    }
}
