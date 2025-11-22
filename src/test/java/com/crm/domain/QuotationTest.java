package com.crm.domain;

import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static com.crm.domain.QuotationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
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
    void customerTest() throws Exception {
        Quotation quotation = getQuotationRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        quotation.setCustomer(customerBack);
        assertThat(quotation.getCustomer()).isEqualTo(customerBack);

        quotation.customer(null);
        assertThat(quotation.getCustomer()).isNull();
    }

    @Test
    void paymentTermTest() throws Exception {
        Quotation quotation = getQuotationRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        quotation.setPaymentTerm(masterStaticTypeBack);
        assertThat(quotation.getPaymentTerm()).isEqualTo(masterStaticTypeBack);

        quotation.paymentTerm(null);
        assertThat(quotation.getPaymentTerm()).isNull();
    }

    @Test
    void quotationStatusTest() throws Exception {
        Quotation quotation = getQuotationRandomSampleGenerator();
        MasterStaticType masterStaticTypeBack = getMasterStaticTypeRandomSampleGenerator();

        quotation.setQuotationStatus(masterStaticTypeBack);
        assertThat(quotation.getQuotationStatus()).isEqualTo(masterStaticTypeBack);

        quotation.quotationStatus(null);
        assertThat(quotation.getQuotationStatus()).isNull();
    }
}
