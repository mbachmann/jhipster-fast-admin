package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentPositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentPosition.class);
        DocumentPosition documentPosition1 = new DocumentPosition();
        documentPosition1.setId(1L);
        DocumentPosition documentPosition2 = new DocumentPosition();
        documentPosition2.setId(documentPosition1.getId());
        assertThat(documentPosition1).isEqualTo(documentPosition2);
        documentPosition2.setId(2L);
        assertThat(documentPosition1).isNotEqualTo(documentPosition2);
        documentPosition1.setId(null);
        assertThat(documentPosition1).isNotEqualTo(documentPosition2);
    }
}
