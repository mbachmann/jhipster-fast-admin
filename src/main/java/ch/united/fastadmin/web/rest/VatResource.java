package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.VatRepository;
import ch.united.fastadmin.service.VatService;
import ch.united.fastadmin.service.dto.VatDTO;
import ch.united.fastadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.Vat}.
 */
@RestController
@RequestMapping("/api")
public class VatResource {

    private final Logger log = LoggerFactory.getLogger(VatResource.class);

    private static final String ENTITY_NAME = "vat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VatService vatService;

    private final VatRepository vatRepository;

    public VatResource(VatService vatService, VatRepository vatRepository) {
        this.vatService = vatService;
        this.vatRepository = vatRepository;
    }

    /**
     * {@code POST  /vats} : Create a new vat.
     *
     * @param vatDTO the vatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vatDTO, or with status {@code 400 (Bad Request)} if the vat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vats")
    public ResponseEntity<VatDTO> createVat(@RequestBody VatDTO vatDTO) throws URISyntaxException {
        log.debug("REST request to save Vat : {}", vatDTO);
        if (vatDTO.getId() != null) {
            throw new BadRequestAlertException("A new vat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VatDTO result = vatService.save(vatDTO);
        return ResponseEntity
            .created(new URI("/api/vats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vats/:id} : Updates an existing vat.
     *
     * @param id the id of the vatDTO to save.
     * @param vatDTO the vatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vatDTO,
     * or with status {@code 400 (Bad Request)} if the vatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vats/{id}")
    public ResponseEntity<VatDTO> updateVat(@PathVariable(value = "id", required = false) final Long id, @RequestBody VatDTO vatDTO)
        throws URISyntaxException {
        log.debug("REST request to update Vat : {}, {}", id, vatDTO);
        if (vatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VatDTO result = vatService.save(vatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vats/:id} : Partial updates given fields of an existing vat, field will ignore if it is null
     *
     * @param id the id of the vatDTO to save.
     * @param vatDTO the vatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vatDTO,
     * or with status {@code 400 (Bad Request)} if the vatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vats/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VatDTO> partialUpdateVat(@PathVariable(value = "id", required = false) final Long id, @RequestBody VatDTO vatDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Vat partially : {}, {}", id, vatDTO);
        if (vatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VatDTO> result = vatService.partialUpdate(vatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vats} : get all the vats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vats in body.
     */
    @GetMapping("/vats")
    public ResponseEntity<List<VatDTO>> getAllVats(Pageable pageable) {
        log.debug("REST request to get a page of Vats");
        Page<VatDTO> page = vatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vats/:id} : get the "id" vat.
     *
     * @param id the id of the vatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vats/{id}")
    public ResponseEntity<VatDTO> getVat(@PathVariable Long id) {
        log.debug("REST request to get Vat : {}", id);
        Optional<VatDTO> vatDTO = vatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vatDTO);
    }

    /**
     * {@code DELETE  /vats/:id} : delete the "id" vat.
     *
     * @param id the id of the vatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vats/{id}")
    public ResponseEntity<Void> deleteVat(@PathVariable Long id) {
        log.debug("REST request to delete Vat : {}", id);
        vatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
