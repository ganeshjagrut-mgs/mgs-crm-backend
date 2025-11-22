package com.crm.domain;

import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.PipelineTestSamples.*;
import static com.crm.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void customerTest() throws Exception {
        Task task = getTaskRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        task.setCustomer(customerBack);
        assertThat(task.getCustomer()).isEqualTo(customerBack);

        task.customer(null);
        assertThat(task.getCustomer()).isNull();
    }

    @Test
    void relatedToTest() throws Exception {
        Task task = getTaskRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        task.setRelatedTo(masterStaticTypeBack);
        assertThat(task.getRelatedTo()).isEqualTo(masterStaticTypeBack);

        task.relatedTo(null);
        assertThat(task.getRelatedTo()).isNull();
    }

    @Test
    void pipelineTest() throws Exception {
        Task task = getTaskRandomSampleGenerator();
        Pipeline pipelineBack = getPipelineRandomSampleGenerator();

        task.setPipeline(pipelineBack);
        assertThat(task.getPipeline()).isEqualTo(pipelineBack);

        task.pipeline(null);
        assertThat(task.getPipeline()).isNull();
    }
}
