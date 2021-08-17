package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DocumentTextRepository;
import ch.united.fastadmin.service.DocumentTextService;
import ch.united.fastadmin.service.dto.DocumentTextDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.DocumentText}.
 */
@RestController
@RequestMapping("/api")
public class DocumentTextResource {

    private final Logger log = LoggerFactory.getLogger(DocumentTextResource.class);

    private static final String ENTITY_NAME = "documentText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTextService documentTextService;

    private final DocumentTextRepository documentTextRepository;

    public DocumentTextResource(DocumentTextService documentTextService, DocumentTextRepository documentTextRepository) {
        this.documentTextService = documentTextService;
        this.documentTextRepository = documentTextRepository;
    }

    /**
     * {@code POST  /document-texts} : Create a new documentText.
     *
     * @param documentTextDTO the documentTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentTextDTO, or with status {@code 400 (Bad Request)} if the documentText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-texts")
    public ResponseEntity<DocumentTextDTO> createDocumentText(@RequestBody DocumentTextDTO documentTextDTO) throws URISyntaxException {
        log.debug("REST request to save DocumentText : {}", documentTextDTO);
        if (documentTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentTextDTO result = documentTextService.save(documentTextDTO);
        return ResponseEntity
            .created(new URI("/api/document-texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-texts/:id} : Updates an existing documentText.
     *
     * @param id the id of the documentTextDTO to save.
     * @param documentTextDTO the documentTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTextDTO,
     * or with status {@code 400 (Bad Request)} if the documentTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-texts/{id}")
    public ResponseEntity<DocumentTextDTO> updateDocumentText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentTextDTO documentTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentText : {}, {}", id, documentTextDTO);
        if (documentTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentTextDTO result = documentTextService.save(documentTextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-texts/:id} : Partial updates given fields of an existing documentText, field will ignore if it is null
     *
     * @param id the id of the documentTextDTO to save.
     * @param documentTextDTO the documentTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentTextDTO,
     * or with status {@code 400 (Bad Request)} if the documentTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-texts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentTextDTO> partialUpdateDocumentText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentTextDTO documentTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentText partially : {}, {}", id, documentTextDTO);
        if (documentTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentTextDTO> result = documentTextService.partialUpdate(documentTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-texts} : get all the documentTexts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTexts in body.
     */
    @GetMapping("/document-texts")
    public List<DocumentTextDTO> getAllDocumentTexts() {
        log.debug("REST request to get all DocumentTexts");
        return documentTextService.findAll();
    }

    /**
     * {@code GET  /document-texts/:id} : get the "id" documentText.
     *
     * @param id the id of the documentTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-texts/{id}")
    public ResponseEntity<DocumentTextDTO> getDocumentText(@PathVariable Long id) {
        log.debug("REST request to get DocumentText : {}", id);
        Optional<DocumentTextDTO> documentTextDTO = documentTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentTextDTO);
    }

    /**
     * {@code DELETE  /document-texts/:id} : delete the "id" documentText.
     *
     * @param id the id of the documentTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-texts/{id}")
    public ResponseEntity<Void> deleteDocumentText(@PathVariable Long id) {
        log.debug("REST request to delete DocumentText : {}", id);
        documentTextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
