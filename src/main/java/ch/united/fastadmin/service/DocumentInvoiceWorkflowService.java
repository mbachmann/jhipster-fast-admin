package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DocumentInvoiceWorkflow;
import ch.united.fastadmin.repository.DocumentInvoiceWorkflowRepository;
import ch.united.fastadmin.service.dto.DocumentInvoiceWorkflowDTO;
import ch.united.fastadmin.service.mapper.DocumentInvoiceWorkflowMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentInvoiceWorkflow}.
 */
@Service
@Transactional
public class DocumentInvoiceWorkflowService {

    private final Logger log = LoggerFactory.getLogger(DocumentInvoiceWorkflowService.class);

    private final DocumentInvoiceWorkflowRepository documentInvoiceWorkflowRepository;

    private final DocumentInvoiceWorkflowMapper documentInvoiceWorkflowMapper;

    public DocumentInvoiceWorkflowService(
        DocumentInvoiceWorkflowRepository documentInvoiceWorkflowRepository,
        DocumentInvoiceWorkflowMapper documentInvoiceWorkflowMapper
    ) {
        this.documentInvoiceWorkflowRepository = documentInvoiceWorkflowRepository;
        this.documentInvoiceWorkflowMapper = documentInvoiceWorkflowMapper;
    }

    /**
     * Save a documentInvoiceWorkflow.
     *
     * @param documentInvoiceWorkflowDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentInvoiceWorkflowDTO save(DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO) {
        log.debug("Request to save DocumentInvoiceWorkflow : {}", documentInvoiceWorkflowDTO);
        DocumentInvoiceWorkflow documentInvoiceWorkflow = documentInvoiceWorkflowMapper.toEntity(documentInvoiceWorkflowDTO);
        documentInvoiceWorkflow = documentInvoiceWorkflowRepository.save(documentInvoiceWorkflow);
        return documentInvoiceWorkflowMapper.toDto(documentInvoiceWorkflow);
    }

    /**
     * Partially update a documentInvoiceWorkflow.
     *
     * @param documentInvoiceWorkflowDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentInvoiceWorkflowDTO> partialUpdate(DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO) {
        log.debug("Request to partially update DocumentInvoiceWorkflow : {}", documentInvoiceWorkflowDTO);

        return documentInvoiceWorkflowRepository
            .findById(documentInvoiceWorkflowDTO.getId())
            .map(
                existingDocumentInvoiceWorkflow -> {
                    documentInvoiceWorkflowMapper.partialUpdate(existingDocumentInvoiceWorkflow, documentInvoiceWorkflowDTO);

                    return existingDocumentInvoiceWorkflow;
                }
            )
            .map(documentInvoiceWorkflowRepository::save)
            .map(documentInvoiceWorkflowMapper::toDto);
    }

    /**
     * Get all the documentInvoiceWorkflows.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentInvoiceWorkflowDTO> findAll() {
        log.debug("Request to get all DocumentInvoiceWorkflows");
        return documentInvoiceWorkflowRepository
            .findAll()
            .stream()
            .map(documentInvoiceWorkflowMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one documentInvoiceWorkflow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentInvoiceWorkflowDTO> findOne(Long id) {
        log.debug("Request to get DocumentInvoiceWorkflow : {}", id);
        return documentInvoiceWorkflowRepository.findById(id).map(documentInvoiceWorkflowMapper::toDto);
    }

    /**
     * Delete the documentInvoiceWorkflow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentInvoiceWorkflow : {}", id);
        documentInvoiceWorkflowRepository.deleteById(id);
    }
}
