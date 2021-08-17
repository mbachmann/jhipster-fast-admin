package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentFreeTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFreeText.class);
        DocumentFreeText documentFreeText1 = new DocumentFreeText();
        documentFreeText1.setId(1L);
        DocumentFreeText documentFreeText2 = new DocumentFreeText();
        documentFreeText2.setId(documentFreeText1.getId());
        assertThat(documentFreeText1).isEqualTo(documentFreeText2);
        documentFreeText2.setId(2L);
        assertThat(documentFreeText1).isNotEqualTo(documentFreeText2);
        documentFreeText1.setId(null);
        assertThat(documentFreeText1).isNotEqualTo(documentFreeText2);
    }
}
