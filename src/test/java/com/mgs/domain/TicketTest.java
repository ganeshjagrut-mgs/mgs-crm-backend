package com.mgs.domain;

import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.TicketTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticket.class);
        Ticket ticket1 = getTicketSample1();
        Ticket ticket2 = new Ticket();
        assertThat(ticket1).isNotEqualTo(ticket2);

        ticket2.setId(ticket1.getId());
        assertThat(ticket1).isEqualTo(ticket2);

        ticket2 = getTicketSample2();
        assertThat(ticket1).isNotEqualTo(ticket2);
    }

    @Test
    void tenantTest() {
        Ticket ticket = getTicketRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        ticket.setTenant(tenantBack);
        assertThat(ticket.getTenant()).isEqualTo(tenantBack);

        ticket.tenant(null);
        assertThat(ticket.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Ticket ticket = getTicketRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        ticket.setCustomer(customerBack);
        assertThat(ticket.getCustomer()).isEqualTo(customerBack);

        ticket.customer(null);
        assertThat(ticket.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Ticket ticket = getTicketRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        ticket.setContact(contactBack);
        assertThat(ticket.getContact()).isEqualTo(contactBack);

        ticket.contact(null);
        assertThat(ticket.getContact()).isNull();
    }

    @Test
    void assignedToTest() {
        Ticket ticket = getTicketRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        ticket.setAssignedTo(userBack);
        assertThat(ticket.getAssignedTo()).isEqualTo(userBack);

        ticket.assignedTo(null);
        assertThat(ticket.getAssignedTo()).isNull();
    }
}
