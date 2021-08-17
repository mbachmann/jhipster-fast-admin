package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ResourcePermissionRepository;
import ch.united.fastadmin.service.ResourcePermissionService;
import ch.united.fastadmin.service.dto.ResourcePermissionDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ResourcePermission}.
 */
@RestController
@RequestMapping("/api")
public class ResourcePermissionResource {

    private final Logger log = LoggerFactory.getLogger(ResourcePermissionResource.class);

    private static final String ENTITY_NAME = "resourcePermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourcePermissionService resourcePermissionService;

    private final ResourcePermissionRepository resourcePermissionRepository;

    public ResourcePermissionResource(
        ResourcePermissionService resourcePermissionService,
        ResourcePermissionRepository resourcePermissionRepository
    ) {
        this.resourcePermissionService = resourcePermissionService;
        this.resourcePermissionRepository = resourcePermissionRepository;
    }

    /**
     * {@code POST  /resource-permissions} : Create a new resourcePermission.
     *
     * @param resourcePermissionDTO the resourcePermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourcePermissionDTO, or with status {@code 400 (Bad Request)} if the resourcePermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-permissions")
    public ResponseEntity<ResourcePermissionDTO> createResourcePermission(@Valid @RequestBody ResourcePermissionDTO resourcePermissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourcePermission : {}", resourcePermissionDTO);
        if (resourcePermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourcePermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourcePermissionDTO result = resourcePermissionService.save(resourcePermissionDTO);
        return ResponseEntity
            .created(new URI("/api/resource-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-permissions/:id} : Updates an existing resourcePermission.
     *
     * @param id the id of the resourcePermissionDTO to save.
     * @param resourcePermissionDTO the resourcePermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourcePermissionDTO,
     * or with status {@code 400 (Bad Request)} if the resourcePermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourcePermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-permissions/{id}")
    public ResponseEntity<ResourcePermissionDTO> updateResourcePermission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourcePermissionDTO resourcePermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourcePermission : {}, {}", id, resourcePermissionDTO);
        if (resourcePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourcePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourcePermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourcePermissionDTO result = resourcePermissionService.save(resourcePermissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourcePermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resource-permissions/:id} : Partial updates given fields of an existing resourcePermission, field will ignore if it is null
     *
     * @param id the id of the resourcePermissionDTO to save.
     * @param resourcePermissionDTO the resourcePermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourcePermissionDTO,
     * or with status {@code 400 (Bad Request)} if the resourcePermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourcePermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourcePermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resource-permissions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResourcePermissionDTO> partialUpdateResourcePermission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourcePermissionDTO resourcePermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourcePermission partially : {}, {}", id, resourcePermissionDTO);
        if (resourcePermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourcePermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourcePermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourcePermissionDTO> result = resourcePermissionService.partialUpdate(resourcePermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourcePermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-permissions} : get all the resourcePermissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourcePermissions in body.
     */
    @GetMapping("/resource-permissions")
    public List<ResourcePermissionDTO> getAllResourcePermissions() {
        log.debug("REST request to get all ResourcePermissions");
        return resourcePermissionService.findAll();
    }

    /**
     * {@code GET  /resource-permissions/:id} : get the "id" resourcePermission.
     *
     * @param id the id of the resourcePermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourcePermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-permissions/{id}")
    public ResponseEntity<ResourcePermissionDTO> getResourcePermission(@PathVariable Long id) {
        log.debug("REST request to get ResourcePermission : {}", id);
        Optional<ResourcePermissionDTO> resourcePermissionDTO = resourcePermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourcePermissionDTO);
    }

    /**
     * {@code DELETE  /resource-permissions/:id} : delete the "id" resourcePermission.
     *
     * @param id the id of the resourcePermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-permissions/{id}")
    public ResponseEntity<Void> deleteResourcePermission(@PathVariable Long id) {
        log.debug("REST request to delete ResourcePermission : {}", id);
        resourcePermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
