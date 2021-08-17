package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DocumentPositionRepository;
import ch.united.fastadmin.service.DocumentPositionService;
import ch.united.fastadmin.service.dto.DocumentPositionDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.DocumentPosition}.
 */
@RestController
@RequestMapping("/api")
public class DocumentPositionResource {

    private final Logger log = LoggerFactory.getLogger(DocumentPositionResource.class);

    private static final String ENTITY_NAME = "documentPosition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentPositionService documentPositionService;

    private final DocumentPositionRepository documentPositionRepository;

    public DocumentPositionResource(
        DocumentPositionService documentPositionService,
        DocumentPositionRepository documentPositionRepository
    ) {
        this.documentPositionService = documentPositionService;
        this.documentPositionRepository = documentPositionRepository;
    }

    /**
     * {@code POST  /document-positions} : Create a new documentPosition.
     *
     * @param documentPositionDTO the documentPositionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentPositionDTO, or with status {@code 400 (Bad Request)} if the documentPosition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-positions")
    public ResponseEntity<DocumentPositionDTO> createDocumentPosition(@RequestBody DocumentPositionDTO documentPositionDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentPosition : {}", documentPositionDTO);
        if (documentPositionDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentPosition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentPositionDTO result = documentPositionService.save(documentPositionDTO);
        return ResponseEntity
            .created(new URI("/api/document-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-positions/:id} : Updates an existing documentPosition.
     *
     * @param id the id of the documentPositionDTO to save.
     * @param documentPositionDTO the documentPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentPositionDTO,
     * or with status {@code 400 (Bad Request)} if the documentPositionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-positions/{id}")
    public ResponseEntity<DocumentPositionDTO> updateDocumentPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentPositionDTO documentPositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentPosition : {}, {}", id, documentPositionDTO);
        if (documentPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentPositionDTO result = documentPositionService.save(documentPositionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-positions/:id} : Partial updates given fields of an existing documentPosition, field will ignore if it is null
     *
     * @param id the id of the documentPositionDTO to save.
     * @param documentPositionDTO the documentPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentPositionDTO,
     * or with status {@code 400 (Bad Request)} if the documentPositionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentPositionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-positions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentPositionDTO> partialUpdateDocumentPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentPositionDTO documentPositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentPosition partially : {}, {}", id, documentPositionDTO);
        if (documentPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentPositionDTO> result = documentPositionService.partialUpdate(documentPositionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentPositionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-positions} : get all the documentPositions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentPositions in body.
     */
    @GetMapping("/document-positions")
    public List<DocumentPositionDTO> getAllDocumentPositions() {
        log.debug("REST request to get all DocumentPositions");
        return documentPositionService.findAll();
    }

    /**
     * {@code GET  /document-positions/:id} : get the "id" documentPosition.
     *
     * @param id the id of the documentPositionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentPositionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-positions/{id}")
    public ResponseEntity<DocumentPositionDTO> getDocumentPosition(@PathVariable Long id) {
        log.debug("REST request to get DocumentPosition : {}", id);
        Optional<DocumentPositionDTO> documentPositionDTO = documentPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentPositionDTO);
    }

    /**
     * {@code DELETE  /document-positions/:id} : delete the "id" documentPosition.
     *
     * @param id the id of the documentPositionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-positions/{id}")
    public ResponseEntity<Void> deleteDocumentPosition(@PathVariable Long id) {
        log.debug("REST request to delete DocumentPosition : {}", id);
        documentPositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
