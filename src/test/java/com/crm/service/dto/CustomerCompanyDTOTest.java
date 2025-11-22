package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerCompanyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerCompanyDTO.class);
        CustomerCompanyDTO customerCompanyDTO1 = new CustomerCompanyDTO();
        customerCompanyDTO1.setId(1L);
        CustomerCompanyDTO customerCompanyDTO2 = new CustomerCompanyDTO();
        assertThat(customerCompanyDTO1).isNotEqualTo(customerCompanyDTO2);
        customerCompanyDTO2.setId(customerCompanyDTO1.getId());
        assertThat(customerCompanyDTO1).isEqualTo(customerCompanyDTO2);
        customerCompanyDTO2.setId(2L);
        assertThat(customerCompanyDTO1).isNotEqualTo(customerCompanyDTO2);
        customerCompanyDTO1.setId(null);
        assertThat(customerCompanyDTO1).isNotEqualTo(customerCompanyDTO2);
    }
}
