package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ReportingActivityRepository;
import ch.united.fastadmin.service.ReportingActivityService;
import ch.united.fastadmin.service.dto.ReportingActivityDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ReportingActivity}.
 */
@RestController
@RequestMapping("/api")
public class ReportingActivityResource {

    private final Logger log = LoggerFactory.getLogger(ReportingActivityResource.class);

    private static final String ENTITY_NAME = "reportingActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingActivityService reportingActivityService;

    private final ReportingActivityRepository reportingActivityRepository;

    public ReportingActivityResource(
        ReportingActivityService reportingActivityService,
        ReportingActivityRepository reportingActivityRepository
    ) {
        this.reportingActivityService = reportingActivityService;
        this.reportingActivityRepository = reportingActivityRepository;
    }

    /**
     * {@code POST  /reporting-activities} : Create a new reportingActivity.
     *
     * @param reportingActivityDTO the reportingActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingActivityDTO, or with status {@code 400 (Bad Request)} if the reportingActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reporting-activities")
    public ResponseEntity<ReportingActivityDTO> createReportingActivity(@Valid @RequestBody ReportingActivityDTO reportingActivityDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportingActivity : {}", reportingActivityDTO);
        if (reportingActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportingActivityDTO result = reportingActivityService.save(reportingActivityDTO);
        return ResponseEntity
            .created(new URI("/api/reporting-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reporting-activities/:id} : Updates an existing reportingActivity.
     *
     * @param id the id of the reportingActivityDTO to save.
     * @param reportingActivityDTO the reportingActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingActivityDTO,
     * or with status {@code 400 (Bad Request)} if the reportingActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reporting-activities/{id}")
    public ResponseEntity<ReportingActivityDTO> updateReportingActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingActivityDTO reportingActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportingActivity : {}, {}", id, reportingActivityDTO);
        if (reportingActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportingActivityDTO result = reportingActivityService.save(reportingActivityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reporting-activities/:id} : Partial updates given fields of an existing reportingActivity, field will ignore if it is null
     *
     * @param id the id of the reportingActivityDTO to save.
     * @param reportingActivityDTO the reportingActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingActivityDTO,
     * or with status {@code 400 (Bad Request)} if the reportingActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reporting-activities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReportingActivityDTO> partialUpdateReportingActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingActivityDTO reportingActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportingActivity partially : {}, {}", id, reportingActivityDTO);
        if (reportingActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingActivityDTO> result = reportingActivityService.partialUpdate(reportingActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-activities} : get all the reportingActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingActivities in body.
     */
    @GetMapping("/reporting-activities")
    public ResponseEntity<List<ReportingActivityDTO>> getAllReportingActivities(Pageable pageable) {
        log.debug("REST request to get a page of ReportingActivities");
        Page<ReportingActivityDTO> page = reportingActivityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-activities/:id} : get the "id" reportingActivity.
     *
     * @param id the id of the reportingActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reporting-activities/{id}")
    public ResponseEntity<ReportingActivityDTO> getReportingActivity(@PathVariable Long id) {
        log.debug("REST request to get ReportingActivity : {}", id);
        Optional<ReportingActivityDTO> reportingActivityDTO = reportingActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingActivityDTO);
    }

    /**
     * {@code DELETE  /reporting-activities/:id} : delete the "id" reportingActivity.
     *
     * @param id the id of the reportingActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reporting-activities/{id}")
    public ResponseEntity<Void> deleteReportingActivity(@PathVariable Long id) {
        log.debug("REST request to delete ReportingActivity : {}", id);
        reportingActivityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
