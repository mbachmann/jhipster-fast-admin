package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DocumentLetterRepository;
import ch.united.fastadmin.service.DocumentLetterService;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import ch.united.fastadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.united.fastadmin.domain.DocumentLetter}.
 */
@RestController
@RequestMapping("/api")
public class DocumentLetterResource {

    private final Logger log = LoggerFactory.getLogger(DocumentLetterResource.class);

    private static final String ENTITY_NAME = "documentLetter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentLetterService documentLetterService;

    private final DocumentLetterRepository documentLetterRepository;

    public DocumentLetterResource(DocumentLetterService documentLetterService, DocumentLetterRepository documentLetterRepository) {
        this.documentLetterService = documentLetterService;
        this.documentLetterRepository = documentLetterRepository;
    }

    /**
     * {@code POST  /document-letters} : Create a new documentLetter.
     *
     * @param documentLetterDTO the documentLetterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentLetterDTO, or with status {@code 400 (Bad Request)} if the documentLetter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-letters")
    public ResponseEntity<DocumentLetterDTO> createDocumentLetter(@Valid @RequestBody DocumentLetterDTO documentLetterDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentLetter : {}", documentLetterDTO);
        if (documentLetterDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentLetter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentLetterDTO result = documentLetterService.save(documentLetterDTO);
        return ResponseEntity
            .created(new URI("/api/document-letters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-letters/:id} : Updates an existing documentLetter.
     *
     * @param id the id of the documentLetterDTO to save.
     * @param documentLetterDTO the documentLetterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentLetterDTO,
     * or with status {@code 400 (Bad Request)} if the documentLetterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentLetterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-letters/{id}")
    public ResponseEntity<DocumentLetterDTO> updateDocumentLetter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentLetterDTO documentLetterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentLetter : {}, {}", id, documentLetterDTO);
        if (documentLetterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentLetterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentLetterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentLetterDTO result = documentLetterService.save(documentLetterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentLetterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-letters/:id} : Partial updates given fields of an existing documentLetter, field will ignore if it is null
     *
     * @param id the id of the documentLetterDTO to save.
     * @param documentLetterDTO the documentLetterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentLetterDTO,
     * or with status {@code 400 (Bad Request)} if the documentLetterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentLetterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentLetterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-letters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentLetterDTO> partialUpdateDocumentLetter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentLetterDTO documentLetterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentLetter partially : {}, {}", id, documentLetterDTO);
        if (documentLetterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentLetterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentLetterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentLetterDTO> result = documentLetterService.partialUpdate(documentLetterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentLetterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-letters} : get all the documentLetters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentLetters in body.
     */
    @GetMapping("/document-letters")
    public ResponseEntity<List<DocumentLetterDTO>> getAllDocumentLetters(Pageable pageable) {
        log.debug("REST request to get a page of DocumentLetters");
        Page<DocumentLetterDTO> page = documentLetterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-letters/:id} : get the "id" documentLetter.
     *
     * @param id the id of the documentLetterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentLetterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-letters/{id}")
    public ResponseEntity<DocumentLetterDTO> getDocumentLetter(@PathVariable Long id) {
        log.debug("REST request to get DocumentLetter : {}", id);
        Optional<DocumentLetterDTO> documentLetterDTO = documentLetterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentLetterDTO);
    }

    /**
     * {@code DELETE  /document-letters/:id} : delete the "id" documentLetter.
     *
     * @param id the id of the documentLetterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-letters/{id}")
    public ResponseEntity<Void> deleteDocumentLetter(@PathVariable Long id) {
        log.debug("REST request to delete DocumentLetter : {}", id);
        documentLetterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
