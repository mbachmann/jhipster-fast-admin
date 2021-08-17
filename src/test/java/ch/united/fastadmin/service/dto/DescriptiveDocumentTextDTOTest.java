package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DescriptiveDocumentTextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DescriptiveDocumentTextDTO.class);
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO1 = new DescriptiveDocumentTextDTO();
        descriptiveDocumentTextDTO1.setId(1L);
        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO2 = new DescriptiveDocumentTextDTO();
        assertThat(descriptiveDocumentTextDTO1).isNotEqualTo(descriptiveDocumentTextDTO2);
        descriptiveDocumentTextDTO2.setId(descriptiveDocumentTextDTO1.getId());
        assertThat(descriptiveDocumentTextDTO1).isEqualTo(descriptiveDocumentTextDTO2);
        descriptiveDocumentTextDTO2.setId(2L);
        assertThat(descriptiveDocumentTextDTO1).isNotEqualTo(descriptiveDocumentTextDTO2);
        descriptiveDocumentTextDTO1.setId(null);
        assertThat(descriptiveDocumentTextDTO1).isNotEqualTo(descriptiveDocumentTextDTO2);
    }
}
