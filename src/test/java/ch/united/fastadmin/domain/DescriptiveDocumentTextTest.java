package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescriptiveDocumentTextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescriptiveDocumentText.class);
        DescriptiveDocumentText descriptiveDocumentText1 = new DescriptiveDocumentText();
        descriptiveDocumentText1.setId(1L);
        DescriptiveDocumentText descriptiveDocumentText2 = new DescriptiveDocumentText();
        descriptiveDocumentText2.setId(descriptiveDocumentText1.getId());
        assertThat(descriptiveDocumentText1).isEqualTo(descriptiveDocumentText2);
        descriptiveDocumentText2.setId(2L);
        assertThat(descriptiveDocumentText1).isNotEqualTo(descriptiveDocumentText2);
        descriptiveDocumentText1.setId(null);
        assertThat(descriptiveDocumentText1).isNotEqualTo(descriptiveDocumentText2);
    }
}
