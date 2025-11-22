package com.crm.domain;

import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.LeadTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lead.class);
        Lead lead1 = getLeadSample1();
        Lead lead2 = new Lead();
        assertThat(lead1).isNotEqualTo(lead2);

        lead2.setId(lead1.getId());
        assertThat(lead1).isEqualTo(lead2);

        lead2 = getLeadSample2();
        assertThat(lead1).isNotEqualTo(lead2);
    }

    @Test
    void customerTest() throws Exception {
        Lead lead = getLeadRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        lead.setCustomer(customerBack);
        assertThat(lead.getCustomer()).isEqualTo(customerBack);

        lead.customer(null);
        assertThat(lead.getCustomer()).isNull();
    }

    @Test
    void leadSourceTest() throws Exception {
        Lead lead = getLeadRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        lead.setLeadSource(masterStaticTypeBack);
        assertThat(lead.getLeadSource()).isEqualTo(masterStaticTypeBack);

        lead.leadSource(null);
        assertThat(lead.getLeadSource()).isNull();
    }

    @Test
    void industryTypeTest() throws Exception {
        Lead lead = getLeadRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        lead.setIndustryType(masterStaticTypeBack);
        assertThat(lead.getIndustryType()).isEqualTo(masterStaticTypeBack);

        lead.industryType(null);
        assertThat(lead.getIndustryType()).isNull();
    }

    @Test
    void leadStatusTest() throws Exception {
        Lead lead = getLeadRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        lead.setLeadStatus(masterStaticTypeBack);
        assertThat(lead.getLeadStatus()).isEqualTo(masterStaticTypeBack);

        lead.leadStatus(null);
        assertThat(lead.getLeadStatus()).isNull();
    }
}
