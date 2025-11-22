package com.crm.domain;

import static com.crm.domain.TaskAuditTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskAudit.class);
        TaskAudit taskAudit1 = getTaskAuditSample1();
        TaskAudit taskAudit2 = new TaskAudit();
        assertThat(taskAudit1).isNotEqualTo(taskAudit2);

        taskAudit2.setId(taskAudit1.getId());
        assertThat(taskAudit1).isEqualTo(taskAudit2);

        taskAudit2 = getTaskAuditSample2();
        assertThat(taskAudit1).isNotEqualTo(taskAudit2);
    }
}
