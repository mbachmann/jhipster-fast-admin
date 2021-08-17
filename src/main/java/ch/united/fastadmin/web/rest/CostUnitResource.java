package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CostUnitRepository;
import ch.united.fastadmin.service.CostUnitService;
import ch.united.fastadmin.service.dto.CostUnitDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.united.fastadmin.domain.CostUnit}.
 */
@RestController
@RequestMapping("/api")
public class CostUnitResource {

    private final Logger log = LoggerFactory.getLogger(CostUnitResource.class);

    private static final String ENTITY_NAME = "costUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CostUnitService costUnitService;

    private final CostUnitRepository costUnitRepository;

    public CostUnitResource(CostUnitService costUnitService, CostUnitRepository costUnitRepository) {
        this.costUnitService = costUnitService;
        this.costUnitRepository = costUnitRepository;
    }

    /**
     * {@code POST  /cost-units} : Create a new costUnit.
     *
     * @param costUnitDTO the costUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new costUnitDTO, or with status {@code 400 (Bad Request)} if the costUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cost-units")
    public ResponseEntity<CostUnitDTO> createCostUnit(@Valid @RequestBody CostUnitDTO costUnitDTO) throws URISyntaxException {
        log.debug("REST request to save CostUnit : {}", costUnitDTO);
        if (costUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new costUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CostUnitDTO result = costUnitService.save(costUnitDTO);
        return ResponseEntity
            .created(new URI("/api/cost-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cost-units/:id} : Updates an existing costUnit.
     *
     * @param id the id of the costUnitDTO to save.
     * @param costUnitDTO the costUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costUnitDTO,
     * or with status {@code 400 (Bad Request)} if the costUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the costUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cost-units/{id}")
    public ResponseEntity<CostUnitDTO> updateCostUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CostUnitDTO costUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CostUnit : {}, {}", id, costUnitDTO);
        if (costUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, costUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!costUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CostUnitDTO result = costUnitService.save(costUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, costUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cost-units/:id} : Partial updates given fields of an existing costUnit, field will ignore if it is null
     *
     * @param id the id of the costUnitDTO to save.
     * @param costUnitDTO the costUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated costUnitDTO,
     * or with status {@code 400 (Bad Request)} if the costUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the costUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the costUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cost-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CostUnitDTO> partialUpdateCostUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CostUnitDTO costUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CostUnit partially : {}, {}", id, costUnitDTO);
        if (costUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, costUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!costUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CostUnitDTO> result = costUnitService.partialUpdate(costUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, costUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cost-units} : get all the costUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of costUnits in body.
     */
    @GetMapping("/cost-units")
    public List<CostUnitDTO> getAllCostUnits() {
        log.debug("REST request to get all CostUnits");
        return costUnitService.findAll();
    }

    /**
     * {@code GET  /cost-units/:id} : get the "id" costUnit.
     *
     * @param id the id of the costUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the costUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cost-units/{id}")
    public ResponseEntity<CostUnitDTO> getCostUnit(@PathVariable Long id) {
        log.debug("REST request to get CostUnit : {}", id);
        Optional<CostUnitDTO> costUnitDTO = costUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(costUnitDTO);
    }

    /**
     * {@code DELETE  /cost-units/:id} : delete the "id" costUnit.
     *
     * @param id the id of the costUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cost-units/{id}")
    public ResponseEntity<Void> deleteCostUnit(@PathVariable Long id) {
        log.debug("REST request to delete CostUnit : {}", id);
        costUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
