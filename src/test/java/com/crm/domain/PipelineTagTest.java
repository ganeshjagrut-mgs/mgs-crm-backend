package com.crm.domain;

import static com.crm.domain.PipelineTagTestSamples.*;
import static com.crm.domain.PipelineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelineTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelineTag.class);
        PipelineTag pipelineTag1 = getPipelineTagSample1();
        PipelineTag pipelineTag2 = new PipelineTag();
        assertThat(pipelineTag1).isNotEqualTo(pipelineTag2);

        pipelineTag2.setId(pipelineTag1.getId());
        assertThat(pipelineTag1).isEqualTo(pipelineTag2);

        pipelineTag2 = getPipelineTagSample2();
        assertThat(pipelineTag1).isNotEqualTo(pipelineTag2);
    }

    @Test
    void pipelineTest() throws Exception {
        PipelineTag pipelineTag = getPipelineTagRandomSampleGenerator();
        Pipeline pipelineBack = getPipelineRandomSampleGenerator();

        pipelineTag.setPipeline(pipelineBack);
        assertThat(pipelineTag.getPipeline()).isEqualTo(pipelineBack);

        pipelineTag.pipeline(null);
        assertThat(pipelineTag.getPipeline()).isNull();
    }
}
