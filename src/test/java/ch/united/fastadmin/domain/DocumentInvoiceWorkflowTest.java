package ch.united.fastadmin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.united.fastadmin.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentInvoiceWorkflowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentInvoiceWorkflow.class);
        DocumentInvoiceWorkflow documentInvoiceWorkflow1 = new DocumentInvoiceWorkflow();
        documentInvoiceWorkflow1.setId(1L);
        DocumentInvoiceWorkflow documentInvoiceWorkflow2 = new DocumentInvoiceWorkflow();
        documentInvoiceWorkflow2.setId(documentInvoiceWorkflow1.getId());
        assertThat(documentInvoiceWorkflow1).isEqualTo(documentInvoiceWorkflow2);
        documentInvoiceWorkflow2.setId(2L);
        assertThat(documentInvoiceWorkflow1).isNotEqualTo(documentInvoiceWorkflow2);
        documentInvoiceWorkflow1.setId(null);
        assertThat(documentInvoiceWorkflow1).isNotEqualTo(documentInvoiceWorkflow2);
    }
}
