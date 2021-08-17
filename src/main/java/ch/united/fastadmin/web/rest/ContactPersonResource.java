package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.service.ContactPersonService;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
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

    public ContactPersonResource(ContactPersonService contactPersonService, ContactPersonRepository contactPersonRepository) {
        this.contactPersonService = contactPersonService;
        this.contactPersonRepository = contactPersonRepository;
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
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactPeople in body.
     */
    @GetMapping("/contact-people")
    public ResponseEntity<List<ContactPersonDTO>> getAllContactPeople(Pageable pageable) {
        log.debug("REST request to get a page of ContactPeople");
        Page<ContactPersonDTO> page = contactPersonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
}
