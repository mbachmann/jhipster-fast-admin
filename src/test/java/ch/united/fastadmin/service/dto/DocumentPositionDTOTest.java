package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentPositionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentPositionDTO.class);
        DocumentPositionDTO documentPositionDTO1 = new DocumentPositionDTO();
        documentPositionDTO1.setId(1L);
        DocumentPositionDTO documentPositionDTO2 = new DocumentPositionDTO();
        assertThat(documentPositionDTO1).isNotEqualTo(documentPositionDTO2);
        documentPositionDTO2.setId(documentPositionDTO1.getId());
        assertThat(documentPositionDTO1).isEqualTo(documentPositionDTO2);
        documentPositionDTO2.setId(2L);
        assertThat(documentPositionDTO1).isNotEqualTo(documentPositionDTO2);
        documentPositionDTO1.setId(null);
        assertThat(documentPositionDTO1).isNotEqualTo(documentPositionDTO2);
    }
}
