package com.mgs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComplaintCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplaintCategoryDTO.class);
        ComplaintCategoryDTO complaintCategoryDTO1 = new ComplaintCategoryDTO();
        complaintCategoryDTO1.setId(1L);
        ComplaintCategoryDTO complaintCategoryDTO2 = new ComplaintCategoryDTO();
        assertThat(complaintCategoryDTO1).isNotEqualTo(complaintCategoryDTO2);
        complaintCategoryDTO2.setId(complaintCategoryDTO1.getId());
        assertThat(complaintCategoryDTO1).isEqualTo(complaintCategoryDTO2);
        complaintCategoryDTO2.setId(2L);
        assertThat(complaintCategoryDTO1).isNotEqualTo(complaintCategoryDTO2);
        complaintCategoryDTO1.setId(null);
        assertThat(complaintCategoryDTO1).isNotEqualTo(complaintCategoryDTO2);
    }
}
