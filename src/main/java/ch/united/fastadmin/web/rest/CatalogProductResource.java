package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CatalogProductRepository;
import ch.united.fastadmin.service.CatalogProductService;
import ch.united.fastadmin.service.dto.CatalogProductDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.CatalogProduct}.
 */
@RestController
@RequestMapping("/api")
public class CatalogProductResource {

    private final Logger log = LoggerFactory.getLogger(CatalogProductResource.class);

    private static final String ENTITY_NAME = "catalogProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogProductService catalogProductService;

    private final CatalogProductRepository catalogProductRepository;

    public CatalogProductResource(CatalogProductService catalogProductService, CatalogProductRepository catalogProductRepository) {
        this.catalogProductService = catalogProductService;
        this.catalogProductRepository = catalogProductRepository;
    }

    /**
     * {@code POST  /catalog-products} : Create a new catalogProduct.
     *
     * @param catalogProductDTO the catalogProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogProductDTO, or with status {@code 400 (Bad Request)} if the catalogProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-products")
    public ResponseEntity<CatalogProductDTO> createCatalogProduct(@Valid @RequestBody CatalogProductDTO catalogProductDTO)
        throws URISyntaxException {
        log.debug("REST request to save CatalogProduct : {}", catalogProductDTO);
        if (catalogProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogProductDTO result = catalogProductService.save(catalogProductDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-products/:id} : Updates an existing catalogProduct.
     *
     * @param id the id of the catalogProductDTO to save.
     * @param catalogProductDTO the catalogProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogProductDTO,
     * or with status {@code 400 (Bad Request)} if the catalogProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-products/{id}")
    public ResponseEntity<CatalogProductDTO> updateCatalogProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogProductDTO catalogProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogProduct : {}, {}", id, catalogProductDTO);
        if (catalogProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogProductDTO result = catalogProductService.save(catalogProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-products/:id} : Partial updates given fields of an existing catalogProduct, field will ignore if it is null
     *
     * @param id the id of the catalogProductDTO to save.
     * @param catalogProductDTO the catalogProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogProductDTO,
     * or with status {@code 400 (Bad Request)} if the catalogProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-products/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CatalogProductDTO> partialUpdateCatalogProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogProductDTO catalogProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogProduct partially : {}, {}", id, catalogProductDTO);
        if (catalogProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogProductDTO> result = catalogProductService.partialUpdate(catalogProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-products} : get all the catalogProducts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogProducts in body.
     */
    @GetMapping("/catalog-products")
    public ResponseEntity<List<CatalogProductDTO>> getAllCatalogProducts(Pageable pageable) {
        log.debug("REST request to get a page of CatalogProducts");
        Page<CatalogProductDTO> page = catalogProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-products/:id} : get the "id" catalogProduct.
     *
     * @param id the id of the catalogProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-products/{id}")
    public ResponseEntity<CatalogProductDTO> getCatalogProduct(@PathVariable Long id) {
        log.debug("REST request to get CatalogProduct : {}", id);
        Optional<CatalogProductDTO> catalogProductDTO = catalogProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogProductDTO);
    }

    /**
     * {@code DELETE  /catalog-products/:id} : delete the "id" catalogProduct.
     *
     * @param id the id of the catalogProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-products/{id}")
    public ResponseEntity<Void> deleteCatalogProduct(@PathVariable Long id) {
        log.debug("REST request to delete CatalogProduct : {}", id);
        catalogProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
