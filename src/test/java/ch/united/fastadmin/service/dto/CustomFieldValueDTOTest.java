package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomFieldValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomFieldValueDTO.class);
        CustomFieldValueDTO customFieldValueDTO1 = new CustomFieldValueDTO();
        customFieldValueDTO1.setId(1L);
        CustomFieldValueDTO customFieldValueDTO2 = new CustomFieldValueDTO();
        assertThat(customFieldValueDTO1).isNotEqualTo(customFieldValueDTO2);
        customFieldValueDTO2.setId(customFieldValueDTO1.getId());
        assertThat(customFieldValueDTO1).isEqualTo(customFieldValueDTO2);
        customFieldValueDTO2.setId(2L);
        assertThat(customFieldValueDTO1).isNotEqualTo(customFieldValueDTO2);
        customFieldValueDTO1.setId(null);
        assertThat(customFieldValueDTO1).isNotEqualTo(customFieldValueDTO2);
    }
}
