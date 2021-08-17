package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DocumentInvoiceWorkflowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentInvoiceWorkflow} and its DTO {@link DocumentInvoiceWorkflowDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentInvoiceWorkflowMapper extends EntityMapper<DocumentInvoiceWorkflowDTO, DocumentInvoiceWorkflow> {}
