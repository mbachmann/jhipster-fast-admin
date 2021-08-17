package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.FreeTextRepository;
import ch.united.fastadmin.service.FreeTextService;
import ch.united.fastadmin.service.dto.FreeTextDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.FreeText}.
 */
@RestController
@RequestMapping("/api")
public class FreeTextResource {

    private final Logger log = LoggerFactory.getLogger(FreeTextResource.class);

    private static final String ENTITY_NAME = "freeText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FreeTextService freeTextService;

    private final FreeTextRepository freeTextRepository;

    public FreeTextResource(FreeTextService freeTextService, FreeTextRepository freeTextRepository) {
        this.freeTextService = freeTextService;
        this.freeTextRepository = freeTextRepository;
    }

    /**
     * {@code POST  /free-texts} : Create a new freeText.
     *
     * @param freeTextDTO the freeTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new freeTextDTO, or with status {@code 400 (Bad Request)} if the freeText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/free-texts")
    public ResponseEntity<FreeTextDTO> createFreeText(@RequestBody FreeTextDTO freeTextDTO) throws URISyntaxException {
        log.debug("REST request to save FreeText : {}", freeTextDTO);
        if (freeTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new freeText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FreeTextDTO result = freeTextService.save(freeTextDTO);
        return ResponseEntity
            .created(new URI("/api/free-texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /free-texts/:id} : Updates an existing freeText.
     *
     * @param id the id of the freeTextDTO to save.
     * @param freeTextDTO the freeTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated freeTextDTO,
     * or with status {@code 400 (Bad Request)} if the freeTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the freeTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/free-texts/{id}")
    public ResponseEntity<FreeTextDTO> updateFreeText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FreeTextDTO freeTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FreeText : {}, {}", id, freeTextDTO);
        if (freeTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, freeTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!freeTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FreeTextDTO result = freeTextService.save(freeTextDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, freeTextDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /free-texts/:id} : Partial updates given fields of an existing freeText, field will ignore if it is null
     *
     * @param id the id of the freeTextDTO to save.
     * @param freeTextDTO the freeTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated freeTextDTO,
     * or with status {@code 400 (Bad Request)} if the freeTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the freeTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the freeTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/free-texts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FreeTextDTO> partialUpdateFreeText(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FreeTextDTO freeTextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FreeText partially : {}, {}", id, freeTextDTO);
        if (freeTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, freeTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!freeTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FreeTextDTO> result = freeTextService.partialUpdate(freeTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, freeTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /free-texts} : get all the freeTexts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of freeTexts in body.
     */
    @GetMapping("/free-texts")
    public List<FreeTextDTO> getAllFreeTexts() {
        log.debug("REST request to get all FreeTexts");
        return freeTextService.findAll();
    }

    /**
     * {@code GET  /free-texts/:id} : get the "id" freeText.
     *
     * @param id the id of the freeTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the freeTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/free-texts/{id}")
    public ResponseEntity<FreeTextDTO> getFreeText(@PathVariable Long id) {
        log.debug("REST request to get FreeText : {}", id);
        Optional<FreeTextDTO> freeTextDTO = freeTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(freeTextDTO);
    }

    /**
     * {@code DELETE  /free-texts/:id} : delete the "id" freeText.
     *
     * @param id the id of the freeTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/free-texts/{id}")
    public ResponseEntity<Void> deleteFreeText(@PathVariable Long id) {
        log.debug("REST request to delete FreeText : {}", id);
        freeTextService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
