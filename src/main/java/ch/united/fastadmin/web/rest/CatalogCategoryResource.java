package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CatalogCategoryRepository;
import ch.united.fastadmin.service.CatalogCategoryService;
import ch.united.fastadmin.service.dto.CatalogCategoryDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.CatalogCategory}.
 */
@RestController
@RequestMapping("/api")
public class CatalogCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CatalogCategoryResource.class);

    private static final String ENTITY_NAME = "catalogCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogCategoryService catalogCategoryService;

    private final CatalogCategoryRepository catalogCategoryRepository;

    public CatalogCategoryResource(CatalogCategoryService catalogCategoryService, CatalogCategoryRepository catalogCategoryRepository) {
        this.catalogCategoryService = catalogCategoryService;
        this.catalogCategoryRepository = catalogCategoryRepository;
    }

    /**
     * {@code POST  /catalog-categories} : Create a new catalogCategory.
     *
     * @param catalogCategoryDTO the catalogCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogCategoryDTO, or with status {@code 400 (Bad Request)} if the catalogCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-categories")
    public ResponseEntity<CatalogCategoryDTO> createCatalogCategory(@Valid @RequestBody CatalogCategoryDTO catalogCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save CatalogCategory : {}", catalogCategoryDTO);
        if (catalogCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogCategoryDTO result = catalogCategoryService.save(catalogCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-categories/:id} : Updates an existing catalogCategory.
     *
     * @param id the id of the catalogCategoryDTO to save.
     * @param catalogCategoryDTO the catalogCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the catalogCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-categories/{id}")
    public ResponseEntity<CatalogCategoryDTO> updateCatalogCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogCategoryDTO catalogCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogCategory : {}, {}", id, catalogCategoryDTO);
        if (catalogCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogCategoryDTO result = catalogCategoryService.save(catalogCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-categories/:id} : Partial updates given fields of an existing catalogCategory, field will ignore if it is null
     *
     * @param id the id of the catalogCategoryDTO to save.
     * @param catalogCategoryDTO the catalogCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the catalogCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CatalogCategoryDTO> partialUpdateCatalogCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogCategoryDTO catalogCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogCategory partially : {}, {}", id, catalogCategoryDTO);
        if (catalogCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogCategoryDTO> result = catalogCategoryService.partialUpdate(catalogCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-categories} : get all the catalogCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogCategories in body.
     */
    @GetMapping("/catalog-categories")
    public List<CatalogCategoryDTO> getAllCatalogCategories() {
        log.debug("REST request to get all CatalogCategories");
        return catalogCategoryService.findAll();
    }

    /**
     * {@code GET  /catalog-categories/:id} : get the "id" catalogCategory.
     *
     * @param id the id of the catalogCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-categories/{id}")
    public ResponseEntity<CatalogCategoryDTO> getCatalogCategory(@PathVariable Long id) {
        log.debug("REST request to get CatalogCategory : {}", id);
        Optional<CatalogCategoryDTO> catalogCategoryDTO = catalogCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogCategoryDTO);
    }

    /**
     * {@code DELETE  /catalog-categories/:id} : delete the "id" catalogCategory.
     *
     * @param id the id of the catalogCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-categories/{id}")
    public ResponseEntity<Void> deleteCatalogCategory(@PathVariable Long id) {
        log.debug("REST request to delete CatalogCategory : {}", id);
        catalogCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
