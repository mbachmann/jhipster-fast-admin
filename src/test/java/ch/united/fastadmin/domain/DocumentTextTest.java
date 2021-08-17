package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentText.class);
        DocumentText documentText1 = new DocumentText();
        documentText1.setId(1L);
        DocumentText documentText2 = new DocumentText();
        documentText2.setId(documentText1.getId());
        assertThat(documentText1).isEqualTo(documentText2);
        documentText2.setId(2L);
        assertThat(documentText1).isNotEqualTo(documentText2);
        documentText1.setId(null);
        assertThat(documentText1).isNotEqualTo(documentText2);
    }
}
