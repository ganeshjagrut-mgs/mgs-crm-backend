package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterStaticGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterStaticGroupDTO.class);
        MasterStaticGroupDTO masterStaticGroupDTO1 = new MasterStaticGroupDTO();
        masterStaticGroupDTO1.setId(1L);
        MasterStaticGroupDTO masterStaticGroupDTO2 = new MasterStaticGroupDTO();
        assertThat(masterStaticGroupDTO1).isNotEqualTo(masterStaticGroupDTO2);
        masterStaticGroupDTO2.setId(masterStaticGroupDTO1.getId());
        assertThat(masterStaticGroupDTO1).isEqualTo(masterStaticGroupDTO2);
        masterStaticGroupDTO2.setId(2L);
        assertThat(masterStaticGroupDTO1).isNotEqualTo(masterStaticGroupDTO2);
        masterStaticGroupDTO1.setId(null);
        assertThat(masterStaticGroupDTO1).isNotEqualTo(masterStaticGroupDTO2);
    }
}
