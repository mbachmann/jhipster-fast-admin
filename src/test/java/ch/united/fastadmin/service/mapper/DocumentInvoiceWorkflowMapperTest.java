package ch.united.fastadmin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentInvoiceWorkflowMapperTest {

    private DocumentInvoiceWorkflowMapper documentInvoiceWorkflowMapper;

    @BeforeEach
    public void setUp() {
        documentInvoiceWorkflowMapper = new DocumentInvoiceWorkflowMapperImpl();
    }
}
