package com.mgs.domain;

import static com.mgs.domain.TaskTypeTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskType.class);
        TaskType taskType1 = getTaskTypeSample1();
        TaskType taskType2 = new TaskType();
        assertThat(taskType1).isNotEqualTo(taskType2);

        taskType2.setId(taskType1.getId());
        assertThat(taskType1).isEqualTo(taskType2);

        taskType2 = getTaskTypeSample2();
        assertThat(taskType1).isNotEqualTo(taskType2);
    }

    @Test
    void tenantTest() {
        TaskType taskType = getTaskTypeRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        taskType.setTenant(tenantBack);
        assertThat(taskType.getTenant()).isEqualTo(tenantBack);

        taskType.tenant(null);
        assertThat(taskType.getTenant()).isNull();
    }
}
