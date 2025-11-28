package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventNotificationDTO.class);
        EventNotificationDTO eventNotificationDTO1 = new EventNotificationDTO();
        eventNotificationDTO1.setId(1L);
        EventNotificationDTO eventNotificationDTO2 = new EventNotificationDTO();
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
        eventNotificationDTO2.setId(eventNotificationDTO1.getId());
        assertThat(eventNotificationDTO1).isEqualTo(eventNotificationDTO2);
        eventNotificationDTO2.setId(2L);
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
        eventNotificationDTO1.setId(null);
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
    }
}
