package com.crm.domain;

import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.SubPipelineCloseStageTestSamples.*;
import static com.crm.domain.SubPipelineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubPipelineCloseStageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipelineCloseStage.class);
        SubPipelineCloseStage subPipelineCloseStage1 = getSubPipelineCloseStageSample1();
        SubPipelineCloseStage subPipelineCloseStage2 = new SubPipelineCloseStage();
        assertThat(subPipelineCloseStage1).isNotEqualTo(subPipelineCloseStage2);

        subPipelineCloseStage2.setId(subPipelineCloseStage1.getId());
        assertThat(subPipelineCloseStage1).isEqualTo(subPipelineCloseStage2);

        subPipelineCloseStage2 = getSubPipelineCloseStageSample2();
        assertThat(subPipelineCloseStage1).isNotEqualTo(subPipelineCloseStage2);
    }

    @Test
    void stageTest() throws Exception {
        SubPipelineCloseStage subPipelineCloseStage = getSubPipelineCloseStageRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        subPipelineCloseStage.setStage(masterStaticTypeBack);
        assertThat(subPipelineCloseStage.getStage()).isEqualTo(masterStaticTypeBack);

        subPipelineCloseStage.stage(null);
        assertThat(subPipelineCloseStage.getStage()).isNull();
    }

    @Test
    void subPipelineTest() throws Exception {
        SubPipelineCloseStage subPipelineCloseStage = getSubPipelineCloseStageRandomSampleGenerator();
        SubPipeline subPipelineBack = getSubPipelineRandomSampleGenerator();

        subPipelineCloseStage.setSubPipeline(subPipelineBack);
        assertThat(subPipelineCloseStage.getSubPipeline()).isEqualTo(subPipelineBack);

        subPipelineCloseStage.subPipeline(null);
        assertThat(subPipelineCloseStage.getSubPipeline()).isNull();
    }
}
