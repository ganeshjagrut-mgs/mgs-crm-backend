package com.mgs.domain;

import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.LeadTestSamples.*;
import static com.mgs.domain.QuotationTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quotation.class);
        Quotation quotation1 = getQuotationSample1();
        Quotation quotation2 = new Quotation();
        assertThat(quotation1).isNotEqualTo(quotation2);

        quotation2.setId(quotation1.getId());
        assertThat(quotation1).isEqualTo(quotation2);

        quotation2 = getQuotationSample2();
        assertThat(quotation1).isNotEqualTo(quotation2);
    }

    @Test
    void tenantTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        quotation.setTenant(tenantBack);
        assertThat(quotation.getTenant()).isEqualTo(tenantBack);

        quotation.tenant(null);
        assertThat(quotation.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        quotation.setCustomer(customerBack);
        assertThat(quotation.getCustomer()).isEqualTo(customerBack);

        quotation.customer(null);
        assertThat(quotation.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        quotation.setContact(contactBack);
        assertThat(quotation.getContact()).isEqualTo(contactBack);

        quotation.contact(null);
        assertThat(quotation.getContact()).isNull();
    }

    @Test
    void leadTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Lead leadBack = getLeadRandomSampleGenerator();

        quotation.setLead(leadBack);
        assertThat(quotation.getLead()).isEqualTo(leadBack);

        quotation.lead(null);
        assertThat(quotation.getLead()).isNull();
    }

    @Test
    void createdByUserTest() {
        Quotation quotation = getQuotationRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        quotation.setCreatedByUser(userBack);
        assertThat(quotation.getCreatedByUser()).isEqualTo(userBack);

        quotation.createdByUser(null);
        assertThat(quotation.getCreatedByUser()).isNull();
    }
}
