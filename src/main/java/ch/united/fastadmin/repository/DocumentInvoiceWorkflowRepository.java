package ch.united.fastadmin.repository;

import ch.united.fastadmin.domain.DocumentInvoiceWorkflow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentInvoiceWorkflow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentInvoiceWorkflowRepository extends JpaRepository<DocumentInvoiceWorkflow, Long> {}
