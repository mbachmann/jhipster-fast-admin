package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomFieldDTO.class);
        CustomFieldDTO customFieldDTO1 = new CustomFieldDTO();
        customFieldDTO1.setId(1L);
        CustomFieldDTO customFieldDTO2 = new CustomFieldDTO();
        assertThat(customFieldDTO1).isNotEqualTo(customFieldDTO2);
        customFieldDTO2.setId(customFieldDTO1.getId());
        assertThat(customFieldDTO1).isEqualTo(customFieldDTO2);
        customFieldDTO2.setId(2L);
        assertThat(customFieldDTO1).isNotEqualTo(customFieldDTO2);
        customFieldDTO1.setId(null);
        assertThat(customFieldDTO1).isNotEqualTo(customFieldDTO2);
    }
}
