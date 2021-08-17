package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ValueAddedTaxRepository;
import ch.united.fastadmin.service.ValueAddedTaxQueryService;
import ch.united.fastadmin.service.ValueAddedTaxService;
import ch.united.fastadmin.service.criteria.ValueAddedTaxCriteria;
import ch.united.fastadmin.service.dto.ValueAddedTaxDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ValueAddedTax}.
 */
@RestController
@RequestMapping("/api")
public class ValueAddedTaxResource {

    private final Logger log = LoggerFactory.getLogger(ValueAddedTaxResource.class);

    private static final String ENTITY_NAME = "valueAddedTax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueAddedTaxService valueAddedTaxService;

    private final ValueAddedTaxRepository valueAddedTaxRepository;

    private final ValueAddedTaxQueryService valueAddedTaxQueryService;

    public ValueAddedTaxResource(
        ValueAddedTaxService valueAddedTaxService,
        ValueAddedTaxRepository valueAddedTaxRepository,
        ValueAddedTaxQueryService valueAddedTaxQueryService
    ) {
        this.valueAddedTaxService = valueAddedTaxService;
        this.valueAddedTaxRepository = valueAddedTaxRepository;
        this.valueAddedTaxQueryService = valueAddedTaxQueryService;
    }

    /**
     * {@code POST  /value-added-taxes} : Create a new valueAddedTax.
     *
     * @param valueAddedTaxDTO the valueAddedTaxDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valueAddedTaxDTO, or with status {@code 400 (Bad Request)} if the valueAddedTax has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/value-added-taxes")
    public ResponseEntity<ValueAddedTaxDTO> createValueAddedTax(@RequestBody ValueAddedTaxDTO valueAddedTaxDTO) throws URISyntaxException {
        log.debug("REST request to save ValueAddedTax : {}", valueAddedTaxDTO);
        if (valueAddedTaxDTO.getId() != null) {
            throw new BadRequestAlertException("A new valueAddedTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValueAddedTaxDTO result = valueAddedTaxService.save(valueAddedTaxDTO);
        return ResponseEntity
            .created(new URI("/api/value-added-taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /value-added-taxes/:id} : Updates an existing valueAddedTax.
     *
     * @param id the id of the valueAddedTaxDTO to save.
     * @param valueAddedTaxDTO the valueAddedTaxDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueAddedTaxDTO,
     * or with status {@code 400 (Bad Request)} if the valueAddedTaxDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valueAddedTaxDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/value-added-taxes/{id}")
    public ResponseEntity<ValueAddedTaxDTO> updateValueAddedTax(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValueAddedTaxDTO valueAddedTaxDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ValueAddedTax : {}, {}", id, valueAddedTaxDTO);
        if (valueAddedTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valueAddedTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valueAddedTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValueAddedTaxDTO result = valueAddedTaxService.save(valueAddedTaxDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valueAddedTaxDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /value-added-taxes/:id} : Partial updates given fields of an existing valueAddedTax, field will ignore if it is null
     *
     * @param id the id of the valueAddedTaxDTO to save.
     * @param valueAddedTaxDTO the valueAddedTaxDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueAddedTaxDTO,
     * or with status {@code 400 (Bad Request)} if the valueAddedTaxDTO is not valid,
     * or with status {@code 404 (Not Found)} if the valueAddedTaxDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the valueAddedTaxDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/value-added-taxes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ValueAddedTaxDTO> partialUpdateValueAddedTax(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValueAddedTaxDTO valueAddedTaxDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ValueAddedTax partially : {}, {}", id, valueAddedTaxDTO);
        if (valueAddedTaxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valueAddedTaxDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valueAddedTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValueAddedTaxDTO> result = valueAddedTaxService.partialUpdate(valueAddedTaxDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valueAddedTaxDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /value-added-taxes} : get all the valueAddedTaxes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valueAddedTaxes in body.
     */
    @GetMapping("/value-added-taxes")
    public ResponseEntity<List<ValueAddedTaxDTO>> getAllValueAddedTaxes(ValueAddedTaxCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ValueAddedTaxes by criteria: {}", criteria);
        Page<ValueAddedTaxDTO> page = valueAddedTaxQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /value-added-taxes/count} : count all the valueAddedTaxes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/value-added-taxes/count")
    public ResponseEntity<Long> countValueAddedTaxes(ValueAddedTaxCriteria criteria) {
        log.debug("REST request to count ValueAddedTaxes by criteria: {}", criteria);
        return ResponseEntity.ok().body(valueAddedTaxQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /value-added-taxes/:id} : get the "id" valueAddedTax.
     *
     * @param id the id of the valueAddedTaxDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valueAddedTaxDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/value-added-taxes/{id}")
    public ResponseEntity<ValueAddedTaxDTO> getValueAddedTax(@PathVariable Long id) {
        log.debug("REST request to get ValueAddedTax : {}", id);
        Optional<ValueAddedTaxDTO> valueAddedTaxDTO = valueAddedTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valueAddedTaxDTO);
    }

    /**
     * {@code DELETE  /value-added-taxes/:id} : delete the "id" valueAddedTax.
     *
     * @param id the id of the valueAddedTaxDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/value-added-taxes/{id}")
    public ResponseEntity<Void> deleteValueAddedTax(@PathVariable Long id) {
        log.debug("REST request to delete ValueAddedTax : {}", id);
        valueAddedTaxService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
