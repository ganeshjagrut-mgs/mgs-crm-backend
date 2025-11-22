package com.crm.domain;

import static com.crm.domain.SubPipelineCloseStageTestSamples.*;
import static com.crm.domain.SubPipelineOpenStageTestSamples.*;
import static com.crm.domain.SubPipelineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubPipelineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubPipeline.class);
        SubPipeline subPipeline1 = getSubPipelineSample1();
        SubPipeline subPipeline2 = new SubPipeline();
        assertThat(subPipeline1).isNotEqualTo(subPipeline2);

        subPipeline2.setId(subPipeline1.getId());
        assertThat(subPipeline1).isEqualTo(subPipeline2);

        subPipeline2 = getSubPipelineSample2();
        assertThat(subPipeline1).isNotEqualTo(subPipeline2);
    }

    @Test
    void openStagesTest() throws Exception {
        SubPipeline subPipeline = getSubPipelineRandomSampleGenerator();
        SubPipelineOpenStage subPipelineOpenStageBack = getSubPipelineOpenStageRandomSampleGenerator();

        subPipeline.addOpenStages(subPipelineOpenStageBack);
        assertThat(subPipeline.getOpenStages()).containsOnly(subPipelineOpenStageBack);
        assertThat(subPipelineOpenStageBack.getSubPipeline()).isEqualTo(subPipeline);

        subPipeline.removeOpenStages(subPipelineOpenStageBack);
        assertThat(subPipeline.getOpenStages()).doesNotContain(subPipelineOpenStageBack);
        assertThat(subPipelineOpenStageBack.getSubPipeline()).isNull();

        subPipeline.openStages(new HashSet<>(Set.of(subPipelineOpenStageBack)));
        assertThat(subPipeline.getOpenStages()).containsOnly(subPipelineOpenStageBack);
        assertThat(subPipelineOpenStageBack.getSubPipeline()).isEqualTo(subPipeline);

        subPipeline.setOpenStages(new HashSet<>());
        assertThat(subPipeline.getOpenStages()).doesNotContain(subPipelineOpenStageBack);
        assertThat(subPipelineOpenStageBack.getSubPipeline()).isNull();
    }

    @Test
    void closeStagesTest() throws Exception {
        SubPipeline subPipeline = getSubPipelineRandomSampleGenerator();
        SubPipelineCloseStage subPipelineCloseStageBack = getSubPipelineCloseStageRandomSampleGenerator();

        subPipeline.addCloseStages(subPipelineCloseStageBack);
        assertThat(subPipeline.getCloseStages()).containsOnly(subPipelineCloseStageBack);
        assertThat(subPipelineCloseStageBack.getSubPipeline()).isEqualTo(subPipeline);

        subPipeline.removeCloseStages(subPipelineCloseStageBack);
        assertThat(subPipeline.getCloseStages()).doesNotContain(subPipelineCloseStageBack);
        assertThat(subPipelineCloseStageBack.getSubPipeline()).isNull();

        subPipeline.closeStages(new HashSet<>(Set.of(subPipelineCloseStageBack)));
        assertThat(subPipeline.getCloseStages()).containsOnly(subPipelineCloseStageBack);
        assertThat(subPipelineCloseStageBack.getSubPipeline()).isEqualTo(subPipeline);

        subPipeline.setCloseStages(new HashSet<>());
        assertThat(subPipeline.getCloseStages()).doesNotContain(subPipelineCloseStageBack);
        assertThat(subPipelineCloseStageBack.getSubPipeline()).isNull();
    }
}
