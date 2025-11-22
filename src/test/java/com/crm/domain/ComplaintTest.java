package com.crm.domain;

import static com.crm.domain.ComplaintTestSamples.*;
import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
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
    void customerNameTest() throws Exception {
        Complaint complaint = getComplaintRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        complaint.setCustomerName(customerBack);
        assertThat(complaint.getCustomerName()).isEqualTo(customerBack);

        complaint.customerName(null);
        assertThat(complaint.getCustomerName()).isNull();
    }

    @Test
    void complaintRelatedToTest() throws Exception {
        Complaint complaint = getComplaintRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        complaint.setComplaintRelatedTo(masterStaticTypeBack);
        assertThat(complaint.getComplaintRelatedTo()).isEqualTo(masterStaticTypeBack);

        complaint.complaintRelatedTo(null);
        assertThat(complaint.getComplaintRelatedTo()).isNull();
    }

    @Test
    void typeOfComplaintTest() throws Exception {
        Complaint complaint = getComplaintRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        complaint.setTypeOfComplaint(masterStaticTypeBack);
        assertThat(complaint.getTypeOfComplaint()).isEqualTo(masterStaticTypeBack);

        complaint.typeOfComplaint(null);
        assertThat(complaint.getTypeOfComplaint()).isNull();
    }
}
