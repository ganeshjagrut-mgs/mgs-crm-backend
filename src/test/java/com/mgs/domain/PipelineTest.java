package com.mgs.domain;

import static com.mgs.domain.PipelineTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        Pipeline pipeline = getPipelineRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        pipeline.setTenant(tenantBack);
        assertThat(pipeline.getTenant()).isEqualTo(tenantBack);

        pipeline.tenant(null);
        assertThat(pipeline.getTenant()).isNull();
    }
}
