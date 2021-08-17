package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CatalogServiceRepository;
import ch.united.fastadmin.service.CatalogServiceQueryService;
import ch.united.fastadmin.service.CatalogServiceService;
import ch.united.fastadmin.service.criteria.CatalogServiceCriteria;
import ch.united.fastadmin.service.dto.CatalogServiceDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.CatalogService}.
 */
@RestController
@RequestMapping("/api")
public class CatalogServiceResource {

    private final Logger log = LoggerFactory.getLogger(CatalogServiceResource.class);

    private static final String ENTITY_NAME = "catalogService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogServiceService catalogServiceService;

    private final CatalogServiceRepository catalogServiceRepository;

    private final CatalogServiceQueryService catalogServiceQueryService;

    public CatalogServiceResource(
        CatalogServiceService catalogServiceService,
        CatalogServiceRepository catalogServiceRepository,
        CatalogServiceQueryService catalogServiceQueryService
    ) {
        this.catalogServiceService = catalogServiceService;
        this.catalogServiceRepository = catalogServiceRepository;
        this.catalogServiceQueryService = catalogServiceQueryService;
    }

    /**
     * {@code POST  /catalog-services} : Create a new catalogService.
     *
     * @param catalogServiceDTO the catalogServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogServiceDTO, or with status {@code 400 (Bad Request)} if the catalogService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-services")
    public ResponseEntity<CatalogServiceDTO> createCatalogService(@Valid @RequestBody CatalogServiceDTO catalogServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save CatalogService : {}", catalogServiceDTO);
        if (catalogServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogServiceDTO result = catalogServiceService.save(catalogServiceDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-services/:id} : Updates an existing catalogService.
     *
     * @param id the id of the catalogServiceDTO to save.
     * @param catalogServiceDTO the catalogServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogServiceDTO,
     * or with status {@code 400 (Bad Request)} if the catalogServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-services/{id}")
    public ResponseEntity<CatalogServiceDTO> updateCatalogService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogServiceDTO catalogServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogService : {}, {}", id, catalogServiceDTO);
        if (catalogServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogServiceDTO result = catalogServiceService.save(catalogServiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-services/:id} : Partial updates given fields of an existing catalogService, field will ignore if it is null
     *
     * @param id the id of the catalogServiceDTO to save.
     * @param catalogServiceDTO the catalogServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogServiceDTO,
     * or with status {@code 400 (Bad Request)} if the catalogServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-services/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CatalogServiceDTO> partialUpdateCatalogService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogServiceDTO catalogServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogService partially : {}, {}", id, catalogServiceDTO);
        if (catalogServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogServiceDTO> result = catalogServiceService.partialUpdate(catalogServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-services} : get all the catalogServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogServices in body.
     */
    @GetMapping("/catalog-services")
    public ResponseEntity<List<CatalogServiceDTO>> getAllCatalogServices(CatalogServiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CatalogServices by criteria: {}", criteria);
        Page<CatalogServiceDTO> page = catalogServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-services/count} : count all the catalogServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/catalog-services/count")
    public ResponseEntity<Long> countCatalogServices(CatalogServiceCriteria criteria) {
        log.debug("REST request to count CatalogServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalog-services/:id} : get the "id" catalogService.
     *
     * @param id the id of the catalogServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-services/{id}")
    public ResponseEntity<CatalogServiceDTO> getCatalogService(@PathVariable Long id) {
        log.debug("REST request to get CatalogService : {}", id);
        Optional<CatalogServiceDTO> catalogServiceDTO = catalogServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogServiceDTO);
    }

    /**
     * {@code DELETE  /catalog-services/:id} : delete the "id" catalogService.
     *
     * @param id the id of the catalogServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-services/{id}")
    public ResponseEntity<Void> deleteCatalogService(@PathVariable Long id) {
        log.debug("REST request to delete CatalogService : {}", id);
        catalogServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
