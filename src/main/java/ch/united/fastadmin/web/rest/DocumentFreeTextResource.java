package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DocumentFreeTextRepository;
import ch.united.fastadmin.service.DocumentFreeTextService;
import ch.united.fastadmin.service.dto.DocumentFreeTextDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.DocumentFreeText}.
 */
@RestController
@RequestMapping("/api")
public class DocumentFreeTextResource {

    private final Logger log = LoggerFactory.getLogger(DocumentFreeTextResource.class);

    private static final String ENTITY_NAME = "documentFreeText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentFreeTextService documentFreeTextService;

    private final DocumentFreeTextRepository documentFreeTextRepository;

    public DocumentFreeTextResource(
        DocumentFreeTextService documentFreeTextService,
        DocumentFreeTextRepository documentFreeTextRepository
    ) {
        this.documentFreeTextService = documentFreeTextService;
        this.documentFreeTextRepository = documentFreeTextRepository;
    }

    /**
     * {@code POST  /document-free-texts} : Create a new documentFreeText.
     *
     * @param documentFreeTextDTO the documentFreeTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentFreeTextDTO, or with status {@code 400 (Bad Request)} if the documentFreeText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-free-texts")
    public ResponseEntity<DocumentFreeTextDTO> createDocumentFreeText(@RequestBody DocumentFreeTextDTO documentFreeTextDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentFreeText : {}", documentFreeTextDTO);
        if (documentFreeTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentFreeText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentFreeTextDTO result = documentFreeTextService.save(documentFreeTextDTO);
        return ResponseEntity
            .created(new URI("/api/document-free-texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-free-texts/:id} : Updates an existing documentFreeText.
     *
     * @param id the id of the documentFreeTextDTO to save.
     * @param documentFreeTextDTO the documentFreeTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentFreeTextDTO,
     * or with status {@code 400 (Bad Request)} if the documentFreeTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentFreeTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-free-texts/{id}")
    public ResponseEntity<DocumentFreeTextDTO> updateDocumentFreeText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentFreeTextDTO documentFreeTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentFreeText : {}, {}", id, documentFreeTextDTO);
        if (documentFreeTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentFreeTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentFreeTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentFreeTextDTO result = documentFreeTextService.save(documentFreeTextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentFreeTextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-free-texts/:id} : Partial updates given fields of an existing documentFreeText, field will ignore if it is null
     *
     * @param id the id of the documentFreeTextDTO to save.
     * @param documentFreeTextDTO the documentFreeTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentFreeTextDTO,
     * or with status {@code 400 (Bad Request)} if the documentFreeTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentFreeTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentFreeTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-free-texts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentFreeTextDTO> partialUpdateDocumentFreeText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentFreeTextDTO documentFreeTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentFreeText partially : {}, {}", id, documentFreeTextDTO);
        if (documentFreeTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentFreeTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentFreeTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentFreeTextDTO> result = documentFreeTextService.partialUpdate(documentFreeTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentFreeTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-free-texts} : get all the documentFreeTexts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentFreeTexts in body.
     */
    @GetMapping("/document-free-texts")
    public List<DocumentFreeTextDTO> getAllDocumentFreeTexts() {
        log.debug("REST request to get all DocumentFreeTexts");
        return documentFreeTextService.findAll();
    }

    /**
     * {@code GET  /document-free-texts/:id} : get the "id" documentFreeText.
     *
     * @param id the id of the documentFreeTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentFreeTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-free-texts/{id}")
    public ResponseEntity<DocumentFreeTextDTO> getDocumentFreeText(@PathVariable Long id) {
        log.debug("REST request to get DocumentFreeText : {}", id);
        Optional<DocumentFreeTextDTO> documentFreeTextDTO = documentFreeTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentFreeTextDTO);
    }

    /**
     * {@code DELETE  /document-free-texts/:id} : delete the "id" documentFreeText.
     *
     * @param id the id of the documentFreeTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-free-texts/{id}")
    public ResponseEntity<Void> deleteDocumentFreeText(@PathVariable Long id) {
        log.debug("REST request to delete DocumentFreeText : {}", id);
        documentFreeTextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
