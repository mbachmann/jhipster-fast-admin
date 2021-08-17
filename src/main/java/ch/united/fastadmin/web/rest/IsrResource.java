package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.IsrRepository;
import ch.united.fastadmin.service.IsrService;
import ch.united.fastadmin.service.dto.IsrDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.Isr}.
 */
@RestController
@RequestMapping("/api")
public class IsrResource {

    private final Logger log = LoggerFactory.getLogger(IsrResource.class);

    private static final String ENTITY_NAME = "isr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsrService isrService;

    private final IsrRepository isrRepository;

    public IsrResource(IsrService isrService, IsrRepository isrRepository) {
        this.isrService = isrService;
        this.isrRepository = isrRepository;
    }

    /**
     * {@code POST  /isrs} : Create a new isr.
     *
     * @param isrDTO the isrDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isrDTO, or with status {@code 400 (Bad Request)} if the isr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/isrs")
    public ResponseEntity<IsrDTO> createIsr(@RequestBody IsrDTO isrDTO) throws URISyntaxException {
        log.debug("REST request to save Isr : {}", isrDTO);
        if (isrDTO.getId() != null) {
            throw new BadRequestAlertException("A new isr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsrDTO result = isrService.save(isrDTO);
        return ResponseEntity
            .created(new URI("/api/isrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /isrs/:id} : Updates an existing isr.
     *
     * @param id the id of the isrDTO to save.
     * @param isrDTO the isrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isrDTO,
     * or with status {@code 400 (Bad Request)} if the isrDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/isrs/{id}")
    public ResponseEntity<IsrDTO> updateIsr(@PathVariable(value = "id", required = false) final Long id, @RequestBody IsrDTO isrDTO)
        throws URISyntaxException {
        log.debug("REST request to update Isr : {}, {}", id, isrDTO);
        if (isrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsrDTO result = isrService.save(isrDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isrDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /isrs/:id} : Partial updates given fields of an existing isr, field will ignore if it is null
     *
     * @param id the id of the isrDTO to save.
     * @param isrDTO the isrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isrDTO,
     * or with status {@code 400 (Bad Request)} if the isrDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isrDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/isrs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IsrDTO> partialUpdateIsr(@PathVariable(value = "id", required = false) final Long id, @RequestBody IsrDTO isrDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Isr partially : {}, {}", id, isrDTO);
        if (isrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsrDTO> result = isrService.partialUpdate(isrDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isrDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /isrs} : get all the isrs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isrs in body.
     */
    @GetMapping("/isrs")
    public List<IsrDTO> getAllIsrs() {
        log.debug("REST request to get all Isrs");
        return isrService.findAll();
    }

    /**
     * {@code GET  /isrs/:id} : get the "id" isr.
     *
     * @param id the id of the isrDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isrDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/isrs/{id}")
    public ResponseEntity<IsrDTO> getIsr(@PathVariable Long id) {
        log.debug("REST request to get Isr : {}", id);
        Optional<IsrDTO> isrDTO = isrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isrDTO);
    }

    /**
     * {@code DELETE  /isrs/:id} : delete the "id" isr.
     *
     * @param id the id of the isrDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/isrs/{id}")
    public ResponseEntity<Void> deleteIsr(@PathVariable Long id) {
        log.debug("REST request to delete Isr : {}", id);
        isrService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
