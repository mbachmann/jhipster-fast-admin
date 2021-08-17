package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.EffortRepository;
import ch.united.fastadmin.service.EffortService;
import ch.united.fastadmin.service.dto.EffortDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.Effort}.
 */
@RestController
@RequestMapping("/api")
public class EffortResource {

    private final Logger log = LoggerFactory.getLogger(EffortResource.class);

    private static final String ENTITY_NAME = "effort";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EffortService effortService;

    private final EffortRepository effortRepository;

    public EffortResource(EffortService effortService, EffortRepository effortRepository) {
        this.effortService = effortService;
        this.effortRepository = effortRepository;
    }

    /**
     * {@code POST  /efforts} : Create a new effort.
     *
     * @param effortDTO the effortDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new effortDTO, or with status {@code 400 (Bad Request)} if the effort has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/efforts")
    public ResponseEntity<EffortDTO> createEffort(@Valid @RequestBody EffortDTO effortDTO) throws URISyntaxException {
        log.debug("REST request to save Effort : {}", effortDTO);
        if (effortDTO.getId() != null) {
            throw new BadRequestAlertException("A new effort cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EffortDTO result = effortService.save(effortDTO);
        return ResponseEntity
            .created(new URI("/api/efforts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /efforts/:id} : Updates an existing effort.
     *
     * @param id the id of the effortDTO to save.
     * @param effortDTO the effortDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated effortDTO,
     * or with status {@code 400 (Bad Request)} if the effortDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the effortDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/efforts/{id}")
    public ResponseEntity<EffortDTO> updateEffort(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EffortDTO effortDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Effort : {}, {}", id, effortDTO);
        if (effortDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, effortDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!effortRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EffortDTO result = effortService.save(effortDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, effortDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /efforts/:id} : Partial updates given fields of an existing effort, field will ignore if it is null
     *
     * @param id the id of the effortDTO to save.
     * @param effortDTO the effortDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated effortDTO,
     * or with status {@code 400 (Bad Request)} if the effortDTO is not valid,
     * or with status {@code 404 (Not Found)} if the effortDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the effortDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/efforts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EffortDTO> partialUpdateEffort(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EffortDTO effortDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Effort partially : {}, {}", id, effortDTO);
        if (effortDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, effortDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!effortRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EffortDTO> result = effortService.partialUpdate(effortDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, effortDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /efforts} : get all the efforts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of efforts in body.
     */
    @GetMapping("/efforts")
    public ResponseEntity<List<EffortDTO>> getAllEfforts(Pageable pageable) {
        log.debug("REST request to get a page of Efforts");
        Page<EffortDTO> page = effortService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /efforts/:id} : get the "id" effort.
     *
     * @param id the id of the effortDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the effortDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/efforts/{id}")
    public ResponseEntity<EffortDTO> getEffort(@PathVariable Long id) {
        log.debug("REST request to get Effort : {}", id);
        Optional<EffortDTO> effortDTO = effortService.findOne(id);
        return ResponseUtil.wrapOrNotFound(effortDTO);
    }

    /**
     * {@code DELETE  /efforts/:id} : delete the "id" effort.
     *
     * @param id the id of the effortDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/efforts/{id}")
    public ResponseEntity<Void> deleteEffort(@PathVariable Long id) {
        log.debug("REST request to delete Effort : {}", id);
        effortService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
