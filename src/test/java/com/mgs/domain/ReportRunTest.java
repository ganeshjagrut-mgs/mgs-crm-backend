package com.mgs.domain;

import static com.mgs.domain.ReportRunTestSamples.*;
import static com.mgs.domain.ReportTemplateTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportRunTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportRun.class);
        ReportRun reportRun1 = getReportRunSample1();
        ReportRun reportRun2 = new ReportRun();
        assertThat(reportRun1).isNotEqualTo(reportRun2);

        reportRun2.setId(reportRun1.getId());
        assertThat(reportRun1).isEqualTo(reportRun2);

        reportRun2 = getReportRunSample2();
        assertThat(reportRun1).isNotEqualTo(reportRun2);
    }

    @Test
    void tenantTest() {
        ReportRun reportRun = getReportRunRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        reportRun.setTenant(tenantBack);
        assertThat(reportRun.getTenant()).isEqualTo(tenantBack);

        reportRun.tenant(null);
        assertThat(reportRun.getTenant()).isNull();
    }

    @Test
    void templateTest() {
        ReportRun reportRun = getReportRunRandomSampleGenerator();
        ReportTemplate reportTemplateBack = getReportTemplateRandomSampleGenerator();

        reportRun.setTemplate(reportTemplateBack);
        assertThat(reportRun.getTemplate()).isEqualTo(reportTemplateBack);

        reportRun.template(null);
        assertThat(reportRun.getTemplate()).isNull();
    }

    @Test
    void generatedByUserTest() {
        ReportRun reportRun = getReportRunRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        reportRun.setGeneratedByUser(userBack);
        assertThat(reportRun.getGeneratedByUser()).isEqualTo(userBack);

        reportRun.generatedByUser(null);
        assertThat(reportRun.getGeneratedByUser()).isNull();
    }
}
