package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentTextDTO.class);
        DocumentTextDTO documentTextDTO1 = new DocumentTextDTO();
        documentTextDTO1.setId(1L);
        DocumentTextDTO documentTextDTO2 = new DocumentTextDTO();
        assertThat(documentTextDTO1).isNotEqualTo(documentTextDTO2);
        documentTextDTO2.setId(documentTextDTO1.getId());
        assertThat(documentTextDTO1).isEqualTo(documentTextDTO2);
        documentTextDTO2.setId(2L);
        assertThat(documentTextDTO1).isNotEqualTo(documentTextDTO2);
        documentTextDTO1.setId(null);
        assertThat(documentTextDTO1).isNotEqualTo(documentTextDTO2);
    }
}
