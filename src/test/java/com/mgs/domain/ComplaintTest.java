package com.mgs.domain;

import static com.mgs.domain.ComplaintCategoryTestSamples.*;
import static com.mgs.domain.ComplaintTestSamples.*;
import static com.mgs.domain.ContactTestSamples.*;
import static com.mgs.domain.CustomerTestSamples.*;
import static com.mgs.domain.DepartmentTestSamples.*;
import static com.mgs.domain.PipelineTestSamples.*;
import static com.mgs.domain.SubPipelineTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComplaintTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Complaint.class);
        Complaint complaint1 = getComplaintSample1();
        Complaint complaint2 = new Complaint();
        assertThat(complaint1).isNotEqualTo(complaint2);

        complaint2.setId(complaint1.getId());
        assertThat(complaint1).isEqualTo(complaint2);

        complaint2 = getComplaintSample2();
        assertThat(complaint1).isNotEqualTo(complaint2);
    }

    @Test
    void tenantTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        complaint.setTenant(tenantBack);
        assertThat(complaint.getTenant()).isEqualTo(tenantBack);

        complaint.tenant(null);
        assertThat(complaint.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        complaint.setCustomer(customerBack);
        assertThat(complaint.getCustomer()).isEqualTo(customerBack);

        complaint.customer(null);
        assertThat(complaint.getCustomer()).isNull();
    }

    @Test
    void contactTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        complaint.setContact(contactBack);
        assertThat(complaint.getContact()).isEqualTo(contactBack);

        complaint.contact(null);
        assertThat(complaint.getContact()).isNull();
    }

    @Test
    void categoryTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        ComplaintCategory complaintCategoryBack = getComplaintCategoryRandomSampleGenerator();

        complaint.setCategory(complaintCategoryBack);
        assertThat(complaint.getCategory()).isEqualTo(complaintCategoryBack);

        complaint.category(null);
        assertThat(complaint.getCategory()).isNull();
    }

    @Test
    void pipelineTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Pipeline pipelineBack = getPipelineRandomSampleGenerator();

        complaint.setPipeline(pipelineBack);
        assertThat(complaint.getPipeline()).isEqualTo(pipelineBack);

        complaint.pipeline(null);
        assertThat(complaint.getPipeline()).isNull();
    }

    @Test
    void stageTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        SubPipeline subPipelineBack = getSubPipelineRandomSampleGenerator();

        complaint.setStage(subPipelineBack);
        assertThat(complaint.getStage()).isEqualTo(subPipelineBack);

        complaint.stage(null);
        assertThat(complaint.getStage()).isNull();
    }

    @Test
    void assignedDepartmentTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        complaint.setAssignedDepartment(departmentBack);
        assertThat(complaint.getAssignedDepartment()).isEqualTo(departmentBack);

        complaint.assignedDepartment(null);
        assertThat(complaint.getAssignedDepartment()).isNull();
    }

    @Test
    void assignedUserTest() {
        Complaint complaint = getComplaintRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        complaint.setAssignedUser(userBack);
        assertThat(complaint.getAssignedUser()).isEqualTo(userBack);

        complaint.assignedUser(null);
        assertThat(complaint.getAssignedUser()).isNull();
    }
}
