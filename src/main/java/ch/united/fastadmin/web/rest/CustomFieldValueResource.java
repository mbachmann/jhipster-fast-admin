package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.CustomFieldValueRepository;
import ch.united.fastadmin.service.CustomFieldValueService;
import ch.united.fastadmin.service.dto.CustomFieldValueDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.CustomFieldValue}.
 */
@RestController
@RequestMapping("/api")
public class CustomFieldValueResource {

    private final Logger log = LoggerFactory.getLogger(CustomFieldValueResource.class);

    private static final String ENTITY_NAME = "customFieldValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomFieldValueService customFieldValueService;

    private final CustomFieldValueRepository customFieldValueRepository;

    public CustomFieldValueResource(
        CustomFieldValueService customFieldValueService,
        CustomFieldValueRepository customFieldValueRepository
    ) {
        this.customFieldValueService = customFieldValueService;
        this.customFieldValueRepository = customFieldValueRepository;
    }

    /**
     * {@code POST  /custom-field-values} : Create a new customFieldValue.
     *
     * @param customFieldValueDTO the customFieldValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customFieldValueDTO, or with status {@code 400 (Bad Request)} if the customFieldValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/custom-field-values")
    public ResponseEntity<CustomFieldValueDTO> createCustomFieldValue(@Valid @RequestBody CustomFieldValueDTO customFieldValueDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomFieldValue : {}", customFieldValueDTO);
        if (customFieldValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new customFieldValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomFieldValueDTO result = customFieldValueService.save(customFieldValueDTO);
        return ResponseEntity
            .created(new URI("/api/custom-field-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /custom-field-values/:id} : Updates an existing customFieldValue.
     *
     * @param id the id of the customFieldValueDTO to save.
     * @param customFieldValueDTO the customFieldValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customFieldValueDTO,
     * or with status {@code 400 (Bad Request)} if the customFieldValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customFieldValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/custom-field-values/{id}")
    public ResponseEntity<CustomFieldValueDTO> updateCustomFieldValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomFieldValueDTO customFieldValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomFieldValue : {}, {}", id, customFieldValueDTO);
        if (customFieldValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customFieldValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customFieldValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomFieldValueDTO result = customFieldValueService.save(customFieldValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customFieldValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /custom-field-values/:id} : Partial updates given fields of an existing customFieldValue, field will ignore if it is null
     *
     * @param id the id of the customFieldValueDTO to save.
     * @param customFieldValueDTO the customFieldValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customFieldValueDTO,
     * or with status {@code 400 (Bad Request)} if the customFieldValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customFieldValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customFieldValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/custom-field-values/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CustomFieldValueDTO> partialUpdateCustomFieldValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomFieldValueDTO customFieldValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomFieldValue partially : {}, {}", id, customFieldValueDTO);
        if (customFieldValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customFieldValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customFieldValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomFieldValueDTO> result = customFieldValueService.partialUpdate(customFieldValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customFieldValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /custom-field-values} : get all the customFieldValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customFieldValues in body.
     */
    @GetMapping("/custom-field-values")
    public List<CustomFieldValueDTO> getAllCustomFieldValues() {
        log.debug("REST request to get all CustomFieldValues");
        return customFieldValueService.findAll();
    }

    /**
     * {@code GET  /custom-field-values/:id} : get the "id" customFieldValue.
     *
     * @param id the id of the customFieldValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customFieldValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/custom-field-values/{id}")
    public ResponseEntity<CustomFieldValueDTO> getCustomFieldValue(@PathVariable Long id) {
        log.debug("REST request to get CustomFieldValue : {}", id);
        Optional<CustomFieldValueDTO> customFieldValueDTO = customFieldValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customFieldValueDTO);
    }

    /**
     * {@code DELETE  /custom-field-values/:id} : delete the "id" customFieldValue.
     *
     * @param id the id of the customFieldValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/custom-field-values/{id}")
    public ResponseEntity<Void> deleteCustomFieldValue(@PathVariable Long id) {
        log.debug("REST request to delete CustomFieldValue : {}", id);
        customFieldValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
