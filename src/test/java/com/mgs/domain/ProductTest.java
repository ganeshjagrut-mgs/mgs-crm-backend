package com.mgs.domain;

import static com.mgs.domain.ProductTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void tenantTest() {
        Product product = getProductRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        product.setTenant(tenantBack);
        assertThat(product.getTenant()).isEqualTo(tenantBack);

        product.tenant(null);
        assertThat(product.getTenant()).isNull();
    }
}
