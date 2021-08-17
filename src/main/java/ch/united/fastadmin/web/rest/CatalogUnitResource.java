package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CatalogUnitRepository;
import ch.united.fastadmin.service.CatalogUnitService;
import ch.united.fastadmin.service.dto.CatalogUnitDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.CatalogUnit}.
 */
@RestController
@RequestMapping("/api")
public class CatalogUnitResource {

    private final Logger log = LoggerFactory.getLogger(CatalogUnitResource.class);

    private static final String ENTITY_NAME = "catalogUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogUnitService catalogUnitService;

    private final CatalogUnitRepository catalogUnitRepository;

    public CatalogUnitResource(CatalogUnitService catalogUnitService, CatalogUnitRepository catalogUnitRepository) {
        this.catalogUnitService = catalogUnitService;
        this.catalogUnitRepository = catalogUnitRepository;
    }

    /**
     * {@code POST  /catalog-units} : Create a new catalogUnit.
     *
     * @param catalogUnitDTO the catalogUnitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogUnitDTO, or with status {@code 400 (Bad Request)} if the catalogUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-units")
    public ResponseEntity<CatalogUnitDTO> createCatalogUnit(@Valid @RequestBody CatalogUnitDTO catalogUnitDTO) throws URISyntaxException {
        log.debug("REST request to save CatalogUnit : {}", catalogUnitDTO);
        if (catalogUnitDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogUnitDTO result = catalogUnitService.save(catalogUnitDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-units/:id} : Updates an existing catalogUnit.
     *
     * @param id the id of the catalogUnitDTO to save.
     * @param catalogUnitDTO the catalogUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogUnitDTO,
     * or with status {@code 400 (Bad Request)} if the catalogUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-units/{id}")
    public ResponseEntity<CatalogUnitDTO> updateCatalogUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogUnitDTO catalogUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogUnit : {}, {}", id, catalogUnitDTO);
        if (catalogUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogUnitDTO result = catalogUnitService.save(catalogUnitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogUnitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-units/:id} : Partial updates given fields of an existing catalogUnit, field will ignore if it is null
     *
     * @param id the id of the catalogUnitDTO to save.
     * @param catalogUnitDTO the catalogUnitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogUnitDTO,
     * or with status {@code 400 (Bad Request)} if the catalogUnitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogUnitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogUnitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CatalogUnitDTO> partialUpdateCatalogUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogUnitDTO catalogUnitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogUnit partially : {}, {}", id, catalogUnitDTO);
        if (catalogUnitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogUnitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogUnitDTO> result = catalogUnitService.partialUpdate(catalogUnitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogUnitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-units} : get all the catalogUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogUnits in body.
     */
    @GetMapping("/catalog-units")
    public ResponseEntity<List<CatalogUnitDTO>> getAllCatalogUnits(Pageable pageable) {
        log.debug("REST request to get a page of CatalogUnits");
        Page<CatalogUnitDTO> page = catalogUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-units/:id} : get the "id" catalogUnit.
     *
     * @param id the id of the catalogUnitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-units/{id}")
    public ResponseEntity<CatalogUnitDTO> getCatalogUnit(@PathVariable Long id) {
        log.debug("REST request to get CatalogUnit : {}", id);
        Optional<CatalogUnitDTO> catalogUnitDTO = catalogUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogUnitDTO);
    }

    /**
     * {@code DELETE  /catalog-units/:id} : delete the "id" catalogUnit.
     *
     * @param id the id of the catalogUnitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-units/{id}")
    public ResponseEntity<Void> deleteCatalogUnit(@PathVariable Long id) {
        log.debug("REST request to delete CatalogUnit : {}", id);
        catalogUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
