package com.mgs.domain;

import static com.mgs.domain.AuditLogTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditLog.class);
        AuditLog auditLog1 = getAuditLogSample1();
        AuditLog auditLog2 = new AuditLog();
        assertThat(auditLog1).isNotEqualTo(auditLog2);

        auditLog2.setId(auditLog1.getId());
        assertThat(auditLog1).isEqualTo(auditLog2);

        auditLog2 = getAuditLogSample2();
        assertThat(auditLog1).isNotEqualTo(auditLog2);
    }

    @Test
    void tenantTest() {
        AuditLog auditLog = getAuditLogRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        auditLog.setTenant(tenantBack);
        assertThat(auditLog.getTenant()).isEqualTo(tenantBack);

        auditLog.tenant(null);
        assertThat(auditLog.getTenant()).isNull();
    }

    @Test
    void performedByTest() {
        AuditLog auditLog = getAuditLogRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        auditLog.setPerformedBy(userBack);
        assertThat(auditLog.getPerformedBy()).isEqualTo(userBack);

        auditLog.performedBy(null);
        assertThat(auditLog.getPerformedBy()).isNull();
    }
}
