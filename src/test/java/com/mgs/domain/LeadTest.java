package com.mgs.domain;

import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.LeadSourceTestSamples.*;
import static com.mgs.domain.LeadTestSamples.*;
import static com.mgs.domain.PipelineTestSamples.*;
import static com.mgs.domain.SubPipelineTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
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
    void tenantTest() {
        Lead lead = getLeadRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        lead.setTenant(tenantBack);
        assertThat(lead.getTenant()).isEqualTo(tenantBack);

        lead.tenant(null);
        assertThat(lead.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Lead lead = getLeadRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        lead.setCustomer(customerBack);
        assertThat(lead.getCustomer()).isEqualTo(customerBack);

        lead.customer(null);
        assertThat(lead.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Lead lead = getLeadRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        lead.setContact(contactBack);
        assertThat(lead.getContact()).isEqualTo(contactBack);

        lead.contact(null);
        assertThat(lead.getContact()).isNull();
    }

    @Test
    void sourceTest() {
        Lead lead = getLeadRandomSampleGenerator();
        LeadSource leadSourceBack = getLeadSourceRandomSampleGenerator();

        lead.setSource(leadSourceBack);
        assertThat(lead.getSource()).isEqualTo(leadSourceBack);

        lead.source(null);
        assertThat(lead.getSource()).isNull();
    }

    @Test
    void pipelineTest() {
        Lead lead = getLeadRandomSampleGenerator();
        Pipeline pipelineBack = getPipelineRandomSampleGenerator();

        lead.setPipeline(pipelineBack);
        assertThat(lead.getPipeline()).isEqualTo(pipelineBack);

        lead.pipeline(null);
        assertThat(lead.getPipeline()).isNull();
    }

    @Test
    void stageTest() {
        Lead lead = getLeadRandomSampleGenerator();
        SubPipeline subPipelineBack = getSubPipelineRandomSampleGenerator();

        lead.setStage(subPipelineBack);
        assertThat(lead.getStage()).isEqualTo(subPipelineBack);

        lead.stage(null);
        assertThat(lead.getStage()).isNull();
    }

    @Test
    void ownerUserTest() {
        Lead lead = getLeadRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        lead.setOwnerUser(userBack);
        assertThat(lead.getOwnerUser()).isEqualTo(userBack);

        lead.ownerUser(null);
        assertThat(lead.getOwnerUser()).isNull();
    }
}
