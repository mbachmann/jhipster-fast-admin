package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.DescriptiveDocumentTextRepository;
import ch.united.fastadmin.service.DescriptiveDocumentTextService;
import ch.united.fastadmin.service.dto.DescriptiveDocumentTextDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.DescriptiveDocumentText}.
 */
@RestController
@RequestMapping("/api")
public class DescriptiveDocumentTextResource {

    private final Logger log = LoggerFactory.getLogger(DescriptiveDocumentTextResource.class);

    private static final String ENTITY_NAME = "descriptiveDocumentText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptiveDocumentTextService descriptiveDocumentTextService;

    private final DescriptiveDocumentTextRepository descriptiveDocumentTextRepository;

    public DescriptiveDocumentTextResource(
        DescriptiveDocumentTextService descriptiveDocumentTextService,
        DescriptiveDocumentTextRepository descriptiveDocumentTextRepository
    ) {
        this.descriptiveDocumentTextService = descriptiveDocumentTextService;
        this.descriptiveDocumentTextRepository = descriptiveDocumentTextRepository;
    }

    /**
     * {@code POST  /descriptive-document-texts} : Create a new descriptiveDocumentText.
     *
     * @param descriptiveDocumentTextDTO the descriptiveDocumentTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptiveDocumentTextDTO, or with status {@code 400 (Bad Request)} if the descriptiveDocumentText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/descriptive-document-texts")
    public ResponseEntity<DescriptiveDocumentTextDTO> createDescriptiveDocumentText(
        @RequestBody DescriptiveDocumentTextDTO descriptiveDocumentTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DescriptiveDocumentText : {}", descriptiveDocumentTextDTO);
        if (descriptiveDocumentTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new descriptiveDocumentText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DescriptiveDocumentTextDTO result = descriptiveDocumentTextService.save(descriptiveDocumentTextDTO);
        return ResponseEntity
            .created(new URI("/api/descriptive-document-texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /descriptive-document-texts/:id} : Updates an existing descriptiveDocumentText.
     *
     * @param id the id of the descriptiveDocumentTextDTO to save.
     * @param descriptiveDocumentTextDTO the descriptiveDocumentTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptiveDocumentTextDTO,
     * or with status {@code 400 (Bad Request)} if the descriptiveDocumentTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptiveDocumentTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/descriptive-document-texts/{id}")
    public ResponseEntity<DescriptiveDocumentTextDTO> updateDescriptiveDocumentText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DescriptiveDocumentTextDTO descriptiveDocumentTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DescriptiveDocumentText : {}, {}", id, descriptiveDocumentTextDTO);
        if (descriptiveDocumentTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptiveDocumentTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptiveDocumentTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DescriptiveDocumentTextDTO result = descriptiveDocumentTextService.save(descriptiveDocumentTextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptiveDocumentTextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /descriptive-document-texts/:id} : Partial updates given fields of an existing descriptiveDocumentText, field will ignore if it is null
     *
     * @param id the id of the descriptiveDocumentTextDTO to save.
     * @param descriptiveDocumentTextDTO the descriptiveDocumentTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptiveDocumentTextDTO,
     * or with status {@code 400 (Bad Request)} if the descriptiveDocumentTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the descriptiveDocumentTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the descriptiveDocumentTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/descriptive-document-texts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DescriptiveDocumentTextDTO> partialUpdateDescriptiveDocumentText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DescriptiveDocumentTextDTO descriptiveDocumentTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DescriptiveDocumentText partially : {}, {}", id, descriptiveDocumentTextDTO);
        if (descriptiveDocumentTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptiveDocumentTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!descriptiveDocumentTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DescriptiveDocumentTextDTO> result = descriptiveDocumentTextService.partialUpdate(descriptiveDocumentTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, descriptiveDocumentTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /descriptive-document-texts} : get all the descriptiveDocumentTexts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptiveDocumentTexts in body.
     */
    @GetMapping("/descriptive-document-texts")
    public List<DescriptiveDocumentTextDTO> getAllDescriptiveDocumentTexts() {
        log.debug("REST request to get all DescriptiveDocumentTexts");
        return descriptiveDocumentTextService.findAll();
    }

    /**
     * {@code GET  /descriptive-document-texts/:id} : get the "id" descriptiveDocumentText.
     *
     * @param id the id of the descriptiveDocumentTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptiveDocumentTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/descriptive-document-texts/{id}")
    public ResponseEntity<DescriptiveDocumentTextDTO> getDescriptiveDocumentText(@PathVariable Long id) {
        log.debug("REST request to get DescriptiveDocumentText : {}", id);
        Optional<DescriptiveDocumentTextDTO> descriptiveDocumentTextDTO = descriptiveDocumentTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptiveDocumentTextDTO);
    }

    /**
     * {@code DELETE  /descriptive-document-texts/:id} : delete the "id" descriptiveDocumentText.
     *
     * @param id the id of the descriptiveDocumentTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/descriptive-document-texts/{id}")
    public ResponseEntity<Void> deleteDescriptiveDocumentText(@PathVariable Long id) {
        log.debug("REST request to delete DescriptiveDocumentText : {}", id);
        descriptiveDocumentTextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
