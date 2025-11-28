package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTaskAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTaskAssignmentDTO.class);
        EventTaskAssignmentDTO eventTaskAssignmentDTO1 = new EventTaskAssignmentDTO();
        eventTaskAssignmentDTO1.setId(1L);
        EventTaskAssignmentDTO eventTaskAssignmentDTO2 = new EventTaskAssignmentDTO();
        assertThat(eventTaskAssignmentDTO1).isNotEqualTo(eventTaskAssignmentDTO2);
        eventTaskAssignmentDTO2.setId(eventTaskAssignmentDTO1.getId());
        assertThat(eventTaskAssignmentDTO1).isEqualTo(eventTaskAssignmentDTO2);
        eventTaskAssignmentDTO2.setId(2L);
        assertThat(eventTaskAssignmentDTO1).isNotEqualTo(eventTaskAssignmentDTO2);
        eventTaskAssignmentDTO1.setId(null);
        assertThat(eventTaskAssignmentDTO1).isNotEqualTo(eventTaskAssignmentDTO2);
    }
}
