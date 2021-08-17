package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentLetterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentLetterDTO.class);
        DocumentLetterDTO documentLetterDTO1 = new DocumentLetterDTO();
        documentLetterDTO1.setId(1L);
        DocumentLetterDTO documentLetterDTO2 = new DocumentLetterDTO();
        assertThat(documentLetterDTO1).isNotEqualTo(documentLetterDTO2);
        documentLetterDTO2.setId(documentLetterDTO1.getId());
        assertThat(documentLetterDTO1).isEqualTo(documentLetterDTO2);
        documentLetterDTO2.setId(2L);
        assertThat(documentLetterDTO1).isNotEqualTo(documentLetterDTO2);
        documentLetterDTO1.setId(null);
        assertThat(documentLetterDTO1).isNotEqualTo(documentLetterDTO2);
    }
}
