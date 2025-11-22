package com.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntityTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityTypeDTO.class);
        EntityTypeDTO entityTypeDTO1 = new EntityTypeDTO();
        entityTypeDTO1.setId(1L);
        EntityTypeDTO entityTypeDTO2 = new EntityTypeDTO();
        assertThat(entityTypeDTO1).isNotEqualTo(entityTypeDTO2);
        entityTypeDTO2.setId(entityTypeDTO1.getId());
        assertThat(entityTypeDTO1).isEqualTo(entityTypeDTO2);
        entityTypeDTO2.setId(2L);
        assertThat(entityTypeDTO1).isNotEqualTo(entityTypeDTO2);
        entityTypeDTO1.setId(null);
        assertThat(entityTypeDTO1).isNotEqualTo(entityTypeDTO2);
    }
}
