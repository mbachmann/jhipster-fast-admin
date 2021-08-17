package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentFreeTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFreeTextDTO.class);
        DocumentFreeTextDTO documentFreeTextDTO1 = new DocumentFreeTextDTO();
        documentFreeTextDTO1.setId(1L);
        DocumentFreeTextDTO documentFreeTextDTO2 = new DocumentFreeTextDTO();
        assertThat(documentFreeTextDTO1).isNotEqualTo(documentFreeTextDTO2);
        documentFreeTextDTO2.setId(documentFreeTextDTO1.getId());
        assertThat(documentFreeTextDTO1).isEqualTo(documentFreeTextDTO2);
        documentFreeTextDTO2.setId(2L);
        assertThat(documentFreeTextDTO1).isNotEqualTo(documentFreeTextDTO2);
        documentFreeTextDTO1.setId(null);
        assertThat(documentFreeTextDTO1).isNotEqualTo(documentFreeTextDTO2);
    }
}
