package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterCategoryDTO.class);
        MasterCategoryDTO masterCategoryDTO1 = new MasterCategoryDTO();
        masterCategoryDTO1.setId(1L);
        MasterCategoryDTO masterCategoryDTO2 = new MasterCategoryDTO();
        assertThat(masterCategoryDTO1).isNotEqualTo(masterCategoryDTO2);
        masterCategoryDTO2.setId(masterCategoryDTO1.getId());
        assertThat(masterCategoryDTO1).isEqualTo(masterCategoryDTO2);
        masterCategoryDTO2.setId(2L);
        assertThat(masterCategoryDTO1).isNotEqualTo(masterCategoryDTO2);
        masterCategoryDTO1.setId(null);
        assertThat(masterCategoryDTO1).isNotEqualTo(masterCategoryDTO2);
    }
}
