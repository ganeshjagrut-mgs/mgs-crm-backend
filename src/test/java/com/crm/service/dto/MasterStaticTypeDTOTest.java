package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterStaticTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterStaticTypeDTO.class);
        MasterStaticTypeDTO masterStaticTypeDTO1 = new MasterStaticTypeDTO();
        masterStaticTypeDTO1.setId(1L);
        MasterStaticTypeDTO masterStaticTypeDTO2 = new MasterStaticTypeDTO();
        assertThat(masterStaticTypeDTO1).isNotEqualTo(masterStaticTypeDTO2);
        masterStaticTypeDTO2.setId(masterStaticTypeDTO1.getId());
        assertThat(masterStaticTypeDTO1).isEqualTo(masterStaticTypeDTO2);
        masterStaticTypeDTO2.setId(2L);
        assertThat(masterStaticTypeDTO1).isNotEqualTo(masterStaticTypeDTO2);
        masterStaticTypeDTO1.setId(null);
        assertThat(masterStaticTypeDTO1).isNotEqualTo(masterStaticTypeDTO2);
    }
}
