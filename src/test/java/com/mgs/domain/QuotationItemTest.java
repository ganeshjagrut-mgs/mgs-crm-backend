package com.mgs.domain;

import static com.mgs.domain.ProductTestSamples.*;
import static com.mgs.domain.QuotationItemTestSamples.*;
import static com.mgs.domain.QuotationTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationItem.class);
        QuotationItem quotationItem1 = getQuotationItemSample1();
        QuotationItem quotationItem2 = new QuotationItem();
        assertThat(quotationItem1).isNotEqualTo(quotationItem2);

        quotationItem2.setId(quotationItem1.getId());
        assertThat(quotationItem1).isEqualTo(quotationItem2);

        quotationItem2 = getQuotationItemSample2();
        assertThat(quotationItem1).isNotEqualTo(quotationItem2);
    }

    @Test
    void tenantTest() {
        QuotationItem quotationItem = getQuotationItemRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        quotationItem.setTenant(tenantBack);
        assertThat(quotationItem.getTenant()).isEqualTo(tenantBack);

        quotationItem.tenant(null);
        assertThat(quotationItem.getTenant()).isNull();
    }

    @Test
    void quotationTest() {
        QuotationItem quotationItem = getQuotationItemRandomSampleGenerator();
        Quotation quotationBack = getQuotationRandomSampleGenerator();

        quotationItem.setQuotation(quotationBack);
        assertThat(quotationItem.getQuotation()).isEqualTo(quotationBack);

        quotationItem.quotation(null);
        assertThat(quotationItem.getQuotation()).isNull();
    }

    @Test
    void productTest() {
        QuotationItem quotationItem = getQuotationItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        quotationItem.setProduct(productBack);
        assertThat(quotationItem.getProduct()).isEqualTo(productBack);

        quotationItem.product(null);
        assertThat(quotationItem.getProduct()).isNull();
    }
}
