package com.crm.domain;

import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.PipelineTagTestSamples.*;
import static com.crm.domain.PipelineTestSamples.*;
import static com.crm.domain.SubPipelineTestSamples.*;
import static com.crm.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PipelineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pipeline.class);
        Pipeline pipeline1 = getPipelineSample1();
        Pipeline pipeline2 = new Pipeline();
        assertThat(pipeline1).isNotEqualTo(pipeline2);

        pipeline2.setId(pipeline1.getId());
        assertThat(pipeline1).isEqualTo(pipeline2);

        pipeline2 = getPipelineSample2();
        assertThat(pipeline1).isNotEqualTo(pipeline2);
    }

    @Test
    void pipelineTagsTest() throws Exception {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        PipelineTag pipelineTagBack = getPipelineTagRandomSampleGenerator();

        pipeline.addPipelineTags(pipelineTagBack);
        assertThat(pipeline.getPipelineTags()).containsOnly(pipelineTagBack);
        assertThat(pipelineTagBack.getPipeline()).isEqualTo(pipeline);

        pipeline.removePipelineTags(pipelineTagBack);
        assertThat(pipeline.getPipelineTags()).doesNotContain(pipelineTagBack);
        assertThat(pipelineTagBack.getPipeline()).isNull();

        pipeline.pipelineTags(new HashSet<>(Set.of(pipelineTagBack)));
        assertThat(pipeline.getPipelineTags()).containsOnly(pipelineTagBack);
        assertThat(pipelineTagBack.getPipeline()).isEqualTo(pipeline);

        pipeline.setPipelineTags(new HashSet<>());
        assertThat(pipeline.getPipelineTags()).doesNotContain(pipelineTagBack);
        assertThat(pipelineTagBack.getPipeline()).isNull();
    }

    @Test
    void tasksTest() throws Exception {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        pipeline.addTasks(taskBack);
        assertThat(pipeline.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getPipeline()).isEqualTo(pipeline);

        pipeline.removeTasks(taskBack);
        assertThat(pipeline.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getPipeline()).isNull();

        pipeline.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(pipeline.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getPipeline()).isEqualTo(pipeline);

        pipeline.setTasks(new HashSet<>());
        assertThat(pipeline.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getPipeline()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        pipeline.setCustomer(customerBack);
        assertThat(pipeline.getCustomer()).isEqualTo(customerBack);

        pipeline.customer(null);
        assertThat(pipeline.getCustomer()).isNull();
    }

    @Test
    void stageOfPipelineTest() throws Exception {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        pipeline.setStageOfPipeline(masterStaticTypeBack);
        assertThat(pipeline.getStageOfPipeline()).isEqualTo(masterStaticTypeBack);

        pipeline.stageOfPipeline(null);
        assertThat(pipeline.getStageOfPipeline()).isNull();
    }

    @Test
    void subPipelineTest() throws Exception {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        SubPipeline subPipelineBack = getSubPipelineRandomSampleGenerator();

        pipeline.setSubPipeline(subPipelineBack);
        assertThat(pipeline.getSubPipeline()).isEqualTo(subPipelineBack);

        pipeline.subPipeline(null);
        assertThat(pipeline.getSubPipeline()).isNull();
    }
}
