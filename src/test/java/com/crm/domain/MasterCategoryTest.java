package com.crm.domain;

import static com.crm.domain.CustomerTestSamples.*;
import static com.crm.domain.MasterCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MasterCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterCategory.class);
        MasterCategory masterCategory1 = getMasterCategorySample1();
        MasterCategory masterCategory2 = new MasterCategory();
        assertThat(masterCategory1).isNotEqualTo(masterCategory2);

        masterCategory2.setId(masterCategory1.getId());
        assertThat(masterCategory1).isEqualTo(masterCategory2);

        masterCategory2 = getMasterCategorySample2();
        assertThat(masterCategory1).isNotEqualTo(masterCategory2);
    }

    @Test
    void customersTest() throws Exception {
        MasterCategory masterCategory = getMasterCategoryRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        masterCategory.addCustomers(customerBack);
        assertThat(masterCategory.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getMasterCategories()).containsOnly(masterCategory);

        masterCategory.removeCustomers(customerBack);
        assertThat(masterCategory.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getMasterCategories()).doesNotContain(masterCategory);

        masterCategory.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(masterCategory.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getMasterCategories()).containsOnly(masterCategory);

        masterCategory.setCustomers(new HashSet<>());
        assertThat(masterCategory.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getMasterCategories()).doesNotContain(masterCategory);
    }
}
