package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentLetterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentLetter.class);
        DocumentLetter documentLetter1 = new DocumentLetter();
        documentLetter1.setId(1L);
        DocumentLetter documentLetter2 = new DocumentLetter();
        documentLetter2.setId(documentLetter1.getId());
        assertThat(documentLetter1).isEqualTo(documentLetter2);
        documentLetter2.setId(2L);
        assertThat(documentLetter1).isNotEqualTo(documentLetter2);
        documentLetter1.setId(null);
        assertThat(documentLetter1).isNotEqualTo(documentLetter2);
    }
}
