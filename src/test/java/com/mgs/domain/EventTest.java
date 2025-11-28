package com.mgs.domain;

import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.EventTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void tenantTest() {
        Event event = getEventRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        event.setTenant(tenantBack);
        assertThat(event.getTenant()).isEqualTo(tenantBack);

        event.tenant(null);
        assertThat(event.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Event event = getEventRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        event.setCustomer(customerBack);
        assertThat(event.getCustomer()).isEqualTo(customerBack);

        event.customer(null);
        assertThat(event.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Event event = getEventRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        event.setContact(contactBack);
        assertThat(event.getContact()).isEqualTo(contactBack);

        event.contact(null);
        assertThat(event.getContact()).isNull();
    }
}
