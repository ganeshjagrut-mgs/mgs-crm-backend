package com.crm.domain;

import static com.crm.domain.PipelineAuditTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelineAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelineAudit.class);
        PipelineAudit pipelineAudit1 = getPipelineAuditSample1();
        PipelineAudit pipelineAudit2 = new PipelineAudit();
        assertThat(pipelineAudit1).isNotEqualTo(pipelineAudit2);

        pipelineAudit2.setId(pipelineAudit1.getId());
        assertThat(pipelineAudit1).isEqualTo(pipelineAudit2);

        pipelineAudit2 = getPipelineAuditSample2();
        assertThat(pipelineAudit1).isNotEqualTo(pipelineAudit2);
    }
}
