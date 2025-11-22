package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskAuditDTO.class);
        TaskAuditDTO taskAuditDTO1 = new TaskAuditDTO();
        taskAuditDTO1.setId(1L);
        TaskAuditDTO taskAuditDTO2 = new TaskAuditDTO();
        assertThat(taskAuditDTO1).isNotEqualTo(taskAuditDTO2);
        taskAuditDTO2.setId(taskAuditDTO1.getId());
        assertThat(taskAuditDTO1).isEqualTo(taskAuditDTO2);
        taskAuditDTO2.setId(2L);
        assertThat(taskAuditDTO1).isNotEqualTo(taskAuditDTO2);
        taskAuditDTO1.setId(null);
        assertThat(taskAuditDTO1).isNotEqualTo(taskAuditDTO2);
    }
}
