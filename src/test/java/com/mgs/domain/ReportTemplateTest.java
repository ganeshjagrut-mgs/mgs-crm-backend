package com.mgs.domain;

import static com.mgs.domain.ReportTemplateTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplate.class);
        ReportTemplate reportTemplate1 = getReportTemplateSample1();
        ReportTemplate reportTemplate2 = new ReportTemplate();
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);

        reportTemplate2.setId(reportTemplate1.getId());
        assertThat(reportTemplate1).isEqualTo(reportTemplate2);

        reportTemplate2 = getReportTemplateSample2();
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);
    }

    @Test
    void tenantTest() {
        ReportTemplate reportTemplate = getReportTemplateRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        reportTemplate.setTenant(tenantBack);
        assertThat(reportTemplate.getTenant()).isEqualTo(tenantBack);

        reportTemplate.tenant(null);
        assertThat(reportTemplate.getTenant()).isNull();
    }
}
