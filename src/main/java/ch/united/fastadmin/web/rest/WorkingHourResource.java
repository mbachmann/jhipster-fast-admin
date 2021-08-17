package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.WorkingHourRepository;
import ch.united.fastadmin.service.WorkingHourService;
import ch.united.fastadmin.service.dto.WorkingHourDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.WorkingHour}.
 */
@RestController
@RequestMapping("/api")
public class WorkingHourResource {

    private final Logger log = LoggerFactory.getLogger(WorkingHourResource.class);

    private static final String ENTITY_NAME = "workingHour";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkingHourService workingHourService;

    private final WorkingHourRepository workingHourRepository;

    public WorkingHourResource(WorkingHourService workingHourService, WorkingHourRepository workingHourRepository) {
        this.workingHourService = workingHourService;
        this.workingHourRepository = workingHourRepository;
    }

    /**
     * {@code POST  /working-hours} : Create a new workingHour.
     *
     * @param workingHourDTO the workingHourDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workingHourDTO, or with status {@code 400 (Bad Request)} if the workingHour has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/working-hours")
    public ResponseEntity<WorkingHourDTO> createWorkingHour(@Valid @RequestBody WorkingHourDTO workingHourDTO) throws URISyntaxException {
        log.debug("REST request to save WorkingHour : {}", workingHourDTO);
        if (workingHourDTO.getId() != null) {
            throw new BadRequestAlertException("A new workingHour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkingHourDTO result = workingHourService.save(workingHourDTO);
        return ResponseEntity
            .created(new URI("/api/working-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /working-hours/:id} : Updates an existing workingHour.
     *
     * @param id the id of the workingHourDTO to save.
     * @param workingHourDTO the workingHourDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workingHourDTO,
     * or with status {@code 400 (Bad Request)} if the workingHourDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workingHourDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/working-hours/{id}")
    public ResponseEntity<WorkingHourDTO> updateWorkingHour(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkingHourDTO workingHourDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkingHour : {}, {}", id, workingHourDTO);
        if (workingHourDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workingHourDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workingHourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkingHourDTO result = workingHourService.save(workingHourDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workingHourDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /working-hours/:id} : Partial updates given fields of an existing workingHour, field will ignore if it is null
     *
     * @param id the id of the workingHourDTO to save.
     * @param workingHourDTO the workingHourDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workingHourDTO,
     * or with status {@code 400 (Bad Request)} if the workingHourDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workingHourDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workingHourDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/working-hours/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkingHourDTO> partialUpdateWorkingHour(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkingHourDTO workingHourDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkingHour partially : {}, {}", id, workingHourDTO);
        if (workingHourDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workingHourDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workingHourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkingHourDTO> result = workingHourService.partialUpdate(workingHourDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workingHourDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /working-hours} : get all the workingHours.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workingHours in body.
     */
    @GetMapping("/working-hours")
    public ResponseEntity<List<WorkingHourDTO>> getAllWorkingHours(Pageable pageable) {
        log.debug("REST request to get a page of WorkingHours");
        Page<WorkingHourDTO> page = workingHourService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /working-hours/:id} : get the "id" workingHour.
     *
     * @param id the id of the workingHourDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workingHourDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/working-hours/{id}")
    public ResponseEntity<WorkingHourDTO> getWorkingHour(@PathVariable Long id) {
        log.debug("REST request to get WorkingHour : {}", id);
        Optional<WorkingHourDTO> workingHourDTO = workingHourService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workingHourDTO);
    }

    /**
     * {@code DELETE  /working-hours/:id} : delete the "id" workingHour.
     *
     * @param id the id of the workingHourDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/working-hours/{id}")
    public ResponseEntity<Void> deleteWorkingHour(@PathVariable Long id) {
        log.debug("REST request to delete WorkingHour : {}", id);
        workingHourService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
