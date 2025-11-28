package com.mgs.domain;

import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.DealTestSamples.*;
import static com.mgs.domain.LeadTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DealTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deal.class);
        Deal deal1 = getDealSample1();
        Deal deal2 = new Deal();
        assertThat(deal1).isNotEqualTo(deal2);

        deal2.setId(deal1.getId());
        assertThat(deal1).isEqualTo(deal2);

        deal2 = getDealSample2();
        assertThat(deal1).isNotEqualTo(deal2);
    }

    @Test
    void tenantTest() {
        Deal deal = getDealRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        deal.setTenant(tenantBack);
        assertThat(deal.getTenant()).isEqualTo(tenantBack);

        deal.tenant(null);
        assertThat(deal.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Deal deal = getDealRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        deal.setCustomer(customerBack);
        assertThat(deal.getCustomer()).isEqualTo(customerBack);

        deal.customer(null);
        assertThat(deal.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Deal deal = getDealRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        deal.setContact(contactBack);
        assertThat(deal.getContact()).isEqualTo(contactBack);

        deal.contact(null);
        assertThat(deal.getContact()).isNull();
    }

    @Test
    void leadTest() {
        Deal deal = getDealRandomSampleGenerator();
        Lead leadBack = getLeadRandomSampleGenerator();

        deal.setLead(leadBack);
        assertThat(deal.getLead()).isEqualTo(leadBack);

        deal.lead(null);
        assertThat(deal.getLead()).isNull();
    }
}
