package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VatDTO.class);
        VatDTO vatDTO1 = new VatDTO();
        vatDTO1.setId(1L);
        VatDTO vatDTO2 = new VatDTO();
        assertThat(vatDTO1).isNotEqualTo(vatDTO2);
        vatDTO2.setId(vatDTO1.getId());
        assertThat(vatDTO1).isEqualTo(vatDTO2);
        vatDTO2.setId(2L);
        assertThat(vatDTO1).isNotEqualTo(vatDTO2);
        vatDTO1.setId(null);
        assertThat(vatDTO1).isNotEqualTo(vatDTO2);
    }
}
