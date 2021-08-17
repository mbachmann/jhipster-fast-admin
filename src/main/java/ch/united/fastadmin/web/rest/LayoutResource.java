package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.LayoutRepository;
import ch.united.fastadmin.service.LayoutService;
import ch.united.fastadmin.service.dto.LayoutDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.Layout}.
 */
@RestController
@RequestMapping("/api")
public class LayoutResource {

    private final Logger log = LoggerFactory.getLogger(LayoutResource.class);

    private static final String ENTITY_NAME = "layout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayoutService layoutService;

    private final LayoutRepository layoutRepository;

    public LayoutResource(LayoutService layoutService, LayoutRepository layoutRepository) {
        this.layoutService = layoutService;
        this.layoutRepository = layoutRepository;
    }

    /**
     * {@code POST  /layouts} : Create a new layout.
     *
     * @param layoutDTO the layoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layoutDTO, or with status {@code 400 (Bad Request)} if the layout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layouts")
    public ResponseEntity<LayoutDTO> createLayout(@Valid @RequestBody LayoutDTO layoutDTO) throws URISyntaxException {
        log.debug("REST request to save Layout : {}", layoutDTO);
        if (layoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new layout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LayoutDTO result = layoutService.save(layoutDTO);
        return ResponseEntity
            .created(new URI("/api/layouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layouts/:id} : Updates an existing layout.
     *
     * @param id the id of the layoutDTO to save.
     * @param layoutDTO the layoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layoutDTO,
     * or with status {@code 400 (Bad Request)} if the layoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layouts/{id}")
    public ResponseEntity<LayoutDTO> updateLayout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LayoutDTO layoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Layout : {}, {}", id, layoutDTO);
        if (layoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LayoutDTO result = layoutService.save(layoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, layoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /layouts/:id} : Partial updates given fields of an existing layout, field will ignore if it is null
     *
     * @param id the id of the layoutDTO to save.
     * @param layoutDTO the layoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layoutDTO,
     * or with status {@code 400 (Bad Request)} if the layoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the layoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the layoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/layouts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LayoutDTO> partialUpdateLayout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LayoutDTO layoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Layout partially : {}, {}", id, layoutDTO);
        if (layoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, layoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!layoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LayoutDTO> result = layoutService.partialUpdate(layoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, layoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /layouts} : get all the layouts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layouts in body.
     */
    @GetMapping("/layouts")
    public List<LayoutDTO> getAllLayouts() {
        log.debug("REST request to get all Layouts");
        return layoutService.findAll();
    }

    /**
     * {@code GET  /layouts/:id} : get the "id" layout.
     *
     * @param id the id of the layoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layouts/{id}")
    public ResponseEntity<LayoutDTO> getLayout(@PathVariable Long id) {
        log.debug("REST request to get Layout : {}", id);
        Optional<LayoutDTO> layoutDTO = layoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layoutDTO);
    }

    /**
     * {@code DELETE  /layouts/:id} : delete the "id" layout.
     *
     * @param id the id of the layoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layouts/{id}")
    public ResponseEntity<Void> deleteLayout(@PathVariable Long id) {
        log.debug("REST request to delete Layout : {}", id);
        layoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
