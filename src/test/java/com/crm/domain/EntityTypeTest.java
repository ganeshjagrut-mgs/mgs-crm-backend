package com.crm.domain;

import static com.crm.domain.EntityTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntityTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityType.class);
        EntityType entityType1 = getEntityTypeSample1();
        EntityType entityType2 = new EntityType();
        assertThat(entityType1).isNotEqualTo(entityType2);

        entityType2.setId(entityType1.getId());
        assertThat(entityType1).isEqualTo(entityType2);

        entityType2 = getEntityTypeSample2();
        assertThat(entityType1).isNotEqualTo(entityType2);
    }
}
