package com.crm.domain;

import static com.crm.domain.EntityTypeTestSamples.*;
import static com.crm.domain.MasterStaticGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterStaticGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterStaticGroup.class);
        MasterStaticGroup masterStaticGroup1 = getMasterStaticGroupSample1();
        MasterStaticGroup masterStaticGroup2 = new MasterStaticGroup();
        assertThat(masterStaticGroup1).isNotEqualTo(masterStaticGroup2);

        masterStaticGroup2.setId(masterStaticGroup1.getId());
        assertThat(masterStaticGroup1).isEqualTo(masterStaticGroup2);

        masterStaticGroup2 = getMasterStaticGroupSample2();
        assertThat(masterStaticGroup1).isNotEqualTo(masterStaticGroup2);
    }

    @Test
    void entityTypeTest() throws Exception {
        MasterStaticGroup masterStaticGroup = getMasterStaticGroupRandomSampleGenerator();
        EntityType entityTypeBack = getEntityTypeRandomSampleGenerator();

        masterStaticGroup.setEntityType(entityTypeBack);
        assertThat(masterStaticGroup.getEntityType()).isEqualTo(entityTypeBack);

        masterStaticGroup.entityType(null);
        assertThat(masterStaticGroup.getEntityType()).isNull();
    }
}
