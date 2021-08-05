package ch.united.fastadmin.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.service.ContactPersonQueryService;
import ch.united.fastadmin.service.ContactPersonService;
import ch.united.fastadmin.service.criteria.ContactPersonCriteria;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactPerson}.
 */
@RestController
@RequestMapping("/api")
public class ContactPersonResource {

    private final Logger log = LoggerFactory.getLogger(ContactPersonResource.class);

    private static final String ENTITY_NAME = "contactPerson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactPersonService contactPersonService;

    private final ContactPersonRepository contactPersonRepository;

    private final ContactPersonQueryService contactPersonQueryService;

    public ContactPersonResource(
        ContactPersonService contactPersonService,
        ContactPersonRepository contactPersonRepository,
        ContactPersonQueryService contactPersonQueryService
    ) {
        this.contactPersonService = contactPersonService;
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonQueryService = contactPersonQueryService;
    }

    /**
     * {@code POST  /contact-people} : Create a new contactPerson.
     *
     * @param contactPersonDTO the contactPersonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactPersonDTO, or with status {@code 400 (Bad Request)} if the contactPerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-people")
    public ResponseEntity<ContactPersonDTO> createContactPerson(@Valid @RequestBody ContactPersonDTO contactPersonDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactPerson : {}", contactPersonDTO);
        if (contactPersonDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactPersonDTO result = contactPersonService.save(contactPersonDTO);
        return ResponseEntity
            .created(new URI("/api/contact-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-people/:id} : Updates an existing contactPerson.
     *
     * @param id the id of the contactPersonDTO to save.
     * @param contactPersonDTO the contactPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPersonDTO,
     * or with status {@code 400 (Bad Request)} if the contactPersonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-people/{id}")
    public ResponseEntity<ContactPersonDTO> updateContactPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactPersonDTO contactPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactPerson : {}, {}", id, contactPersonDTO);
        if (contactPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactPersonDTO result = contactPersonService.save(contactPersonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-people/:id} : Partial updates given fields of an existing contactPerson, field will ignore if it is null
     *
     * @param id the id of the contactPersonDTO to save.
     * @param contactPersonDTO the contactPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPersonDTO,
     * or with status {@code 400 (Bad Request)} if the contactPersonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactPersonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-people/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactPersonDTO> partialUpdateContactPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactPersonDTO contactPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactPerson partially : {}, {}", id, contactPersonDTO);
        if (contactPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactPersonDTO> result = contactPersonService.partialUpdate(contactPersonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPersonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-people} : get all the contactPeople.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactPeople in body.
     */
    @GetMapping("/contact-people")
    public ResponseEntity<List<ContactPersonDTO>> getAllContactPeople(ContactPersonCriteria criteria) {
        log.debug("REST request to get ContactPeople by criteria: {}", criteria);
        List<ContactPersonDTO> entityList = contactPersonQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /contact-people/count} : count all the contactPeople.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contact-people/count")
    public ResponseEntity<Long> countContactPeople(ContactPersonCriteria criteria) {
        log.debug("REST request to count ContactPeople by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactPersonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contact-people/:id} : get the "id" contactPerson.
     *
     * @param id the id of the contactPersonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactPersonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-people/{id}")
    public ResponseEntity<ContactPersonDTO> getContactPerson(@PathVariable Long id) {
        log.debug("REST request to get ContactPerson : {}", id);
        Optional<ContactPersonDTO> contactPersonDTO = contactPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactPersonDTO);
    }

    /**
     * {@code DELETE  /contact-people/:id} : delete the "id" contactPerson.
     *
     * @param id the id of the contactPersonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-people/{id}")
    public ResponseEntity<Void> deleteContactPerson(@PathVariable Long id) {
        log.debug("REST request to delete ContactPerson : {}", id);
        contactPersonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/contact-people?query=:query} : search for the contactPerson corresponding
     * to the query.
     *
     * @param query the query of the contactPerson search.
     * @return the result of the search.
     */
    @GetMapping("/_search/contact-people")
    public List<ContactPersonDTO> searchContactPeople(@RequestParam String query) {
        log.debug("REST request to search ContactPeople for query {}", query);
        return contactPersonService.search(query);
    }
}
