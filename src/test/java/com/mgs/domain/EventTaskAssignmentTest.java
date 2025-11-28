package com.mgs.domain;

import static com.mgs.domain.EventTaskAssignmentTestSamples.*;
import static com.mgs.domain.EventTestSamples.*;
import static com.mgs.domain.TaskTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTaskAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTaskAssignment.class);
        EventTaskAssignment eventTaskAssignment1 = getEventTaskAssignmentSample1();
        EventTaskAssignment eventTaskAssignment2 = new EventTaskAssignment();
        assertThat(eventTaskAssignment1).isNotEqualTo(eventTaskAssignment2);

        eventTaskAssignment2.setId(eventTaskAssignment1.getId());
        assertThat(eventTaskAssignment1).isEqualTo(eventTaskAssignment2);

        eventTaskAssignment2 = getEventTaskAssignmentSample2();
        assertThat(eventTaskAssignment1).isNotEqualTo(eventTaskAssignment2);
    }

    @Test
    void eventTest() {
        EventTaskAssignment eventTaskAssignment = getEventTaskAssignmentRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        eventTaskAssignment.setEvent(eventBack);
        assertThat(eventTaskAssignment.getEvent()).isEqualTo(eventBack);

        eventTaskAssignment.event(null);
        assertThat(eventTaskAssignment.getEvent()).isNull();
    }

    @Test
    void taskTest() {
        EventTaskAssignment eventTaskAssignment = getEventTaskAssignmentRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        eventTaskAssignment.setTask(taskBack);
        assertThat(eventTaskAssignment.getTask()).isEqualTo(taskBack);

        eventTaskAssignment.task(null);
        assertThat(eventTaskAssignment.getTask()).isNull();
    }

    @Test
    void assignedToTest() {
        EventTaskAssignment eventTaskAssignment = getEventTaskAssignmentRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        eventTaskAssignment.setAssignedTo(userBack);
        assertThat(eventTaskAssignment.getAssignedTo()).isEqualTo(userBack);

        eventTaskAssignment.assignedTo(null);
        assertThat(eventTaskAssignment.getAssignedTo()).isNull();
    }
}
