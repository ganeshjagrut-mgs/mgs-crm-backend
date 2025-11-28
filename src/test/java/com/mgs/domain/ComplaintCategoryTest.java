package com.mgs.domain;

import static com.mgs.domain.ComplaintCategoryTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComplaintCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplaintCategory.class);
        ComplaintCategory complaintCategory1 = getComplaintCategorySample1();
        ComplaintCategory complaintCategory2 = new ComplaintCategory();
        assertThat(complaintCategory1).isNotEqualTo(complaintCategory2);

        complaintCategory2.setId(complaintCategory1.getId());
        assertThat(complaintCategory1).isEqualTo(complaintCategory2);

        complaintCategory2 = getComplaintCategorySample2();
        assertThat(complaintCategory1).isNotEqualTo(complaintCategory2);
    }

    @Test
    void tenantTest() {
        ComplaintCategory complaintCategory = getComplaintCategoryRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        complaintCategory.setTenant(tenantBack);
        assertThat(complaintCategory.getTenant()).isEqualTo(tenantBack);

        complaintCategory.tenant(null);
        assertThat(complaintCategory.getTenant()).isNull();
    }
}
