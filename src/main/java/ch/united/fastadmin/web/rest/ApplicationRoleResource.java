package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ApplicationRoleRepository;
import ch.united.fastadmin.service.ApplicationRoleService;
import ch.united.fastadmin.service.dto.ApplicationRoleDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ApplicationRole}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationRoleResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationRoleResource.class);

    private static final String ENTITY_NAME = "applicationRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationRoleService applicationRoleService;

    private final ApplicationRoleRepository applicationRoleRepository;

    public ApplicationRoleResource(ApplicationRoleService applicationRoleService, ApplicationRoleRepository applicationRoleRepository) {
        this.applicationRoleService = applicationRoleService;
        this.applicationRoleRepository = applicationRoleRepository;
    }

    /**
     * {@code POST  /application-roles} : Create a new applicationRole.
     *
     * @param applicationRoleDTO the applicationRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationRoleDTO, or with status {@code 400 (Bad Request)} if the applicationRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-roles")
    public ResponseEntity<ApplicationRoleDTO> createApplicationRole(@RequestBody ApplicationRoleDTO applicationRoleDTO)
        throws URISyntaxException {
        log.debug("REST request to save ApplicationRole : {}", applicationRoleDTO);
        if (applicationRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationRoleDTO result = applicationRoleService.save(applicationRoleDTO);
        return ResponseEntity
            .created(new URI("/api/application-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-roles/:id} : Updates an existing applicationRole.
     *
     * @param id the id of the applicationRoleDTO to save.
     * @param applicationRoleDTO the applicationRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationRoleDTO,
     * or with status {@code 400 (Bad Request)} if the applicationRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-roles/{id}")
    public ResponseEntity<ApplicationRoleDTO> updateApplicationRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationRoleDTO applicationRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationRole : {}, {}", id, applicationRoleDTO);
        if (applicationRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationRoleDTO result = applicationRoleService.save(applicationRoleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-roles/:id} : Partial updates given fields of an existing applicationRole, field will ignore if it is null
     *
     * @param id the id of the applicationRoleDTO to save.
     * @param applicationRoleDTO the applicationRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationRoleDTO,
     * or with status {@code 400 (Bad Request)} if the applicationRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-roles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApplicationRoleDTO> partialUpdateApplicationRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationRoleDTO applicationRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationRole partially : {}, {}", id, applicationRoleDTO);
        if (applicationRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationRoleDTO> result = applicationRoleService.partialUpdate(applicationRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /application-roles} : get all the applicationRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationRoles in body.
     */
    @GetMapping("/application-roles")
    public List<ApplicationRoleDTO> getAllApplicationRoles() {
        log.debug("REST request to get all ApplicationRoles");
        return applicationRoleService.findAll();
    }

    /**
     * {@code GET  /application-roles/:id} : get the "id" applicationRole.
     *
     * @param id the id of the applicationRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-roles/{id}")
    public ResponseEntity<ApplicationRoleDTO> getApplicationRole(@PathVariable Long id) {
        log.debug("REST request to get ApplicationRole : {}", id);
        Optional<ApplicationRoleDTO> applicationRoleDTO = applicationRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationRoleDTO);
    }

    /**
     * {@code DELETE  /application-roles/:id} : delete the "id" applicationRole.
     *
     * @param id the id of the applicationRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-roles/{id}")
    public ResponseEntity<Void> deleteApplicationRole(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationRole : {}", id);
        applicationRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
