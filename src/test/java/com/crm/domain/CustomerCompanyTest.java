package com.crm.domain;

import static com.crm.domain.CustomerCompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerCompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerCompany.class);
        CustomerCompany customerCompany1 = getCustomerCompanySample1();
        CustomerCompany customerCompany2 = new CustomerCompany();
        assertThat(customerCompany1).isNotEqualTo(customerCompany2);

        customerCompany2.setId(customerCompany1.getId());
        assertThat(customerCompany1).isEqualTo(customerCompany2);

        customerCompany2 = getCustomerCompanySample2();
        assertThat(customerCompany1).isNotEqualTo(customerCompany2);
    }
}
