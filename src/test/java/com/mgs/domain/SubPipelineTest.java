package com.mgs.domain;

import static com.mgs.domain.PipelineTestSamples.*;
import static com.mgs.domain.SubPipelineTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        SubPipeline subPipeline = getSubPipelineRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        subPipeline.setTenant(tenantBack);
        assertThat(subPipeline.getTenant()).isEqualTo(tenantBack);

        subPipeline.tenant(null);
        assertThat(subPipeline.getTenant()).isNull();
    }

    @Test
    void pipelineTest() {
        SubPipeline subPipeline = getSubPipelineRandomSampleGenerator();
        Pipeline pipelineBack = getPipelineRandomSampleGenerator();

        subPipeline.setPipeline(pipelineBack);
        assertThat(subPipeline.getPipeline()).isEqualTo(pipelineBack);

        subPipeline.pipeline(null);
        assertThat(subPipeline.getPipeline()).isNull();
    }
}
