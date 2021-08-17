package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DocumentInvoiceWorkflowRepository;
import ch.united.fastadmin.service.DocumentInvoiceWorkflowService;
import ch.united.fastadmin.service.dto.DocumentInvoiceWorkflowDTO;
import ch.united.fastadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.united.fastadmin.domain.DocumentInvoiceWorkflow}.
 */
@RestController
@RequestMapping("/api")
public class DocumentInvoiceWorkflowResource {

    private final Logger log = LoggerFactory.getLogger(DocumentInvoiceWorkflowResource.class);

    private static final String ENTITY_NAME = "documentInvoiceWorkflow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentInvoiceWorkflowService documentInvoiceWorkflowService;

    private final DocumentInvoiceWorkflowRepository documentInvoiceWorkflowRepository;

    public DocumentInvoiceWorkflowResource(
        DocumentInvoiceWorkflowService documentInvoiceWorkflowService,
        DocumentInvoiceWorkflowRepository documentInvoiceWorkflowRepository
    ) {
        this.documentInvoiceWorkflowService = documentInvoiceWorkflowService;
        this.documentInvoiceWorkflowRepository = documentInvoiceWorkflowRepository;
    }

    /**
     * {@code POST  /document-invoice-workflows} : Create a new documentInvoiceWorkflow.
     *
     * @param documentInvoiceWorkflowDTO the documentInvoiceWorkflowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentInvoiceWorkflowDTO, or with status {@code 400 (Bad Request)} if the documentInvoiceWorkflow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-invoice-workflows")
    public ResponseEntity<DocumentInvoiceWorkflowDTO> createDocumentInvoiceWorkflow(
        @RequestBody DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DocumentInvoiceWorkflow : {}", documentInvoiceWorkflowDTO);
        if (documentInvoiceWorkflowDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentInvoiceWorkflow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentInvoiceWorkflowDTO result = documentInvoiceWorkflowService.save(documentInvoiceWorkflowDTO);
        return ResponseEntity
            .created(new URI("/api/document-invoice-workflows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-invoice-workflows/:id} : Updates an existing documentInvoiceWorkflow.
     *
     * @param id the id of the documentInvoiceWorkflowDTO to save.
     * @param documentInvoiceWorkflowDTO the documentInvoiceWorkflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentInvoiceWorkflowDTO,
     * or with status {@code 400 (Bad Request)} if the documentInvoiceWorkflowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentInvoiceWorkflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-invoice-workflows/{id}")
    public ResponseEntity<DocumentInvoiceWorkflowDTO> updateDocumentInvoiceWorkflow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentInvoiceWorkflow : {}, {}", id, documentInvoiceWorkflowDTO);
        if (documentInvoiceWorkflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentInvoiceWorkflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentInvoiceWorkflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentInvoiceWorkflowDTO result = documentInvoiceWorkflowService.save(documentInvoiceWorkflowDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentInvoiceWorkflowDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-invoice-workflows/:id} : Partial updates given fields of an existing documentInvoiceWorkflow, field will ignore if it is null
     *
     * @param id the id of the documentInvoiceWorkflowDTO to save.
     * @param documentInvoiceWorkflowDTO the documentInvoiceWorkflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentInvoiceWorkflowDTO,
     * or with status {@code 400 (Bad Request)} if the documentInvoiceWorkflowDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentInvoiceWorkflowDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentInvoiceWorkflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-invoice-workflows/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentInvoiceWorkflowDTO> partialUpdateDocumentInvoiceWorkflow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentInvoiceWorkflow partially : {}, {}", id, documentInvoiceWorkflowDTO);
        if (documentInvoiceWorkflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentInvoiceWorkflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentInvoiceWorkflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentInvoiceWorkflowDTO> result = documentInvoiceWorkflowService.partialUpdate(documentInvoiceWorkflowDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentInvoiceWorkflowDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-invoice-workflows} : get all the documentInvoiceWorkflows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentInvoiceWorkflows in body.
     */
    @GetMapping("/document-invoice-workflows")
    public List<DocumentInvoiceWorkflowDTO> getAllDocumentInvoiceWorkflows() {
        log.debug("REST request to get all DocumentInvoiceWorkflows");
        return documentInvoiceWorkflowService.findAll();
    }

    /**
     * {@code GET  /document-invoice-workflows/:id} : get the "id" documentInvoiceWorkflow.
     *
     * @param id the id of the documentInvoiceWorkflowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentInvoiceWorkflowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-invoice-workflows/{id}")
    public ResponseEntity<DocumentInvoiceWorkflowDTO> getDocumentInvoiceWorkflow(@PathVariable Long id) {
        log.debug("REST request to get DocumentInvoiceWorkflow : {}", id);
        Optional<DocumentInvoiceWorkflowDTO> documentInvoiceWorkflowDTO = documentInvoiceWorkflowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentInvoiceWorkflowDTO);
    }

    /**
     * {@code DELETE  /document-invoice-workflows/:id} : delete the "id" documentInvoiceWorkflow.
     *
     * @param id the id of the documentInvoiceWorkflowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-invoice-workflows/{id}")
    public ResponseEntity<Void> deleteDocumentInvoiceWorkflow(@PathVariable Long id) {
        log.debug("REST request to delete DocumentInvoiceWorkflow : {}", id);
        documentInvoiceWorkflowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
