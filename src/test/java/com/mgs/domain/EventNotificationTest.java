package com.mgs.domain;

import static com.mgs.domain.EventNotificationTestSamples.*;
import static com.mgs.domain.EventTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventNotification.class);
        EventNotification eventNotification1 = getEventNotificationSample1();
        EventNotification eventNotification2 = new EventNotification();
        assertThat(eventNotification1).isNotEqualTo(eventNotification2);

        eventNotification2.setId(eventNotification1.getId());
        assertThat(eventNotification1).isEqualTo(eventNotification2);

        eventNotification2 = getEventNotificationSample2();
        assertThat(eventNotification1).isNotEqualTo(eventNotification2);
    }

    @Test
    void eventTest() {
        EventNotification eventNotification = getEventNotificationRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        eventNotification.setEvent(eventBack);
        assertThat(eventNotification.getEvent()).isEqualTo(eventBack);

        eventNotification.event(null);
        assertThat(eventNotification.getEvent()).isNull();
    }

    @Test
    void userTest() {
        EventNotification eventNotification = getEventNotificationRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        eventNotification.setUser(userBack);
        assertThat(eventNotification.getUser()).isEqualTo(userBack);

        eventNotification.user(null);
        assertThat(eventNotification.getUser()).isNull();
    }
}
