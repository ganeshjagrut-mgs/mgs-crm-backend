package com.crm.domain;

import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.SubPipelineOpenStageTestSamples.*;
import static com.crm.domain.SubPipelineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubPipelineOpenStageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipelineOpenStage.class);
        SubPipelineOpenStage subPipelineOpenStage1 = getSubPipelineOpenStageSample1();
        SubPipelineOpenStage subPipelineOpenStage2 = new SubPipelineOpenStage();
        assertThat(subPipelineOpenStage1).isNotEqualTo(subPipelineOpenStage2);

        subPipelineOpenStage2.setId(subPipelineOpenStage1.getId());
        assertThat(subPipelineOpenStage1).isEqualTo(subPipelineOpenStage2);

        subPipelineOpenStage2 = getSubPipelineOpenStageSample2();
        assertThat(subPipelineOpenStage1).isNotEqualTo(subPipelineOpenStage2);
    }

    @Test
    void stageTest() throws Exception {
        SubPipelineOpenStage subPipelineOpenStage = getSubPipelineOpenStageRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        subPipelineOpenStage.setStage(masterStaticTypeBack);
        assertThat(subPipelineOpenStage.getStage()).isEqualTo(masterStaticTypeBack);

        subPipelineOpenStage.stage(null);
        assertThat(subPipelineOpenStage.getStage()).isNull();
    }

    @Test
    void subPipelineTest() throws Exception {
        SubPipelineOpenStage subPipelineOpenStage = getSubPipelineOpenStageRandomSampleGenerator();
        SubPipeline subPipelineBack = getSubPipelineRandomSampleGenerator();

        subPipelineOpenStage.setSubPipeline(subPipelineBack);
        assertThat(subPipelineOpenStage.getSubPipeline()).isEqualTo(subPipelineBack);

        subPipelineOpenStage.subPipeline(null);
        assertThat(subPipelineOpenStage.getSubPipeline()).isNull();
    }
}
