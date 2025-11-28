package com.mgs.domain;

import static com.mgs.domain.TaskTestSamples.*;
import static com.mgs.domain.TaskTypeTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        Task task = getTaskRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        task.setTenant(tenantBack);
        assertThat(task.getTenant()).isEqualTo(tenantBack);

        task.tenant(null);
        assertThat(task.getTenant()).isNull();
    }

    @Test
    void taskTypeTest() {
        Task task = getTaskRandomSampleGenerator();
        TaskType taskTypeBack = getTaskTypeRandomSampleGenerator();

        task.setTaskType(taskTypeBack);
        assertThat(task.getTaskType()).isEqualTo(taskTypeBack);

        task.taskType(null);
        assertThat(task.getTaskType()).isNull();
    }

    @Test
    void assignedUserTest() {
        Task task = getTaskRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        task.setAssignedUser(userBack);
        assertThat(task.getAssignedUser()).isEqualTo(userBack);

        task.assignedUser(null);
        assertThat(task.getAssignedUser()).isNull();
    }

    @Test
    void createdByUserTest() {
        Task task = getTaskRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        task.setCreatedByUser(userBack);
        assertThat(task.getCreatedByUser()).isEqualTo(userBack);

        task.createdByUser(null);
        assertThat(task.getCreatedByUser()).isNull();
    }
}
