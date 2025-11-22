package com.crm.domain;

import static com.crm.domain.MasterStaticTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterStaticTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterStaticType.class);
        MasterStaticType masterStaticType1 = getMasterStaticTypeSample1();
        MasterStaticType masterStaticType2 = new MasterStaticType();
        assertThat(masterStaticType1).isNotEqualTo(masterStaticType2);

        masterStaticType2.setId(masterStaticType1.getId());
        assertThat(masterStaticType1).isEqualTo(masterStaticType2);

        masterStaticType2 = getMasterStaticTypeSample2();
        assertThat(masterStaticType1).isNotEqualTo(masterStaticType2);
    }
}
