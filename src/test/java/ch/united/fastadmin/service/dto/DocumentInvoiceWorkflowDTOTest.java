package ch.united.fastadmin.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentInvoiceWorkflowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentInvoiceWorkflowDTO.class);
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO1 = new DocumentInvoiceWorkflowDTO();
        documentInvoiceWorkflowDTO1.setId(1L);
        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO2 = new DocumentInvoiceWorkflowDTO();
        assertThat(documentInvoiceWorkflowDTO1).isNotEqualTo(documentInvoiceWorkflowDTO2);
        documentInvoiceWorkflowDTO2.setId(documentInvoiceWorkflowDTO1.getId());
        assertThat(documentInvoiceWorkflowDTO1).isEqualTo(documentInvoiceWorkflowDTO2);
        documentInvoiceWorkflowDTO2.setId(2L);
        assertThat(documentInvoiceWorkflowDTO1).isNotEqualTo(documentInvoiceWorkflowDTO2);
        documentInvoiceWorkflowDTO1.setId(null);
        assertThat(documentInvoiceWorkflowDTO1).isNotEqualTo(documentInvoiceWorkflowDTO2);
    }
}
