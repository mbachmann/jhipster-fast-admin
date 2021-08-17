package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.service.ContactReminderService;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactReminder}.
 */
@RestController
@RequestMapping("/api")
public class ContactReminderResource {

    private final Logger log = LoggerFactory.getLogger(ContactReminderResource.class);

    private static final String ENTITY_NAME = "contactReminder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactReminderService contactReminderService;

    private final ContactReminderRepository contactReminderRepository;

    public ContactReminderResource(ContactReminderService contactReminderService, ContactReminderRepository contactReminderRepository) {
        this.contactReminderService = contactReminderService;
        this.contactReminderRepository = contactReminderRepository;
    }

    /**
     * {@code POST  /contact-reminders} : Create a new contactReminder.
     *
     * @param contactReminderDTO the contactReminderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactReminderDTO, or with status {@code 400 (Bad Request)} if the contactReminder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-reminders")
    public ResponseEntity<ContactReminderDTO> createContactReminder(@Valid @RequestBody ContactReminderDTO contactReminderDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactReminder : {}", contactReminderDTO);
        if (contactReminderDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactReminder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactReminderDTO result = contactReminderService.save(contactReminderDTO);
        return ResponseEntity
            .created(new URI("/api/contact-reminders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-reminders/:id} : Updates an existing contactReminder.
     *
     * @param id the id of the contactReminderDTO to save.
     * @param contactReminderDTO the contactReminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactReminderDTO,
     * or with status {@code 400 (Bad Request)} if the contactReminderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactReminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-reminders/{id}")
    public ResponseEntity<ContactReminderDTO> updateContactReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactReminderDTO contactReminderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactReminder : {}, {}", id, contactReminderDTO);
        if (contactReminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactReminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactReminderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactReminderDTO result = contactReminderService.save(contactReminderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactReminderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-reminders/:id} : Partial updates given fields of an existing contactReminder, field will ignore if it is null
     *
     * @param id the id of the contactReminderDTO to save.
     * @param contactReminderDTO the contactReminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactReminderDTO,
     * or with status {@code 400 (Bad Request)} if the contactReminderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactReminderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactReminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-reminders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactReminderDTO> partialUpdateContactReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactReminderDTO contactReminderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactReminder partially : {}, {}", id, contactReminderDTO);
        if (contactReminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactReminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactReminderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactReminderDTO> result = contactReminderService.partialUpdate(contactReminderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactReminderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-reminders} : get all the contactReminders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactReminders in body.
     */
    @GetMapping("/contact-reminders")
    public ResponseEntity<List<ContactReminderDTO>> getAllContactReminders(Pageable pageable) {
        log.debug("REST request to get a page of ContactReminders");
        Page<ContactReminderDTO> page = contactReminderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-reminders/:id} : get the "id" contactReminder.
     *
     * @param id the id of the contactReminderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactReminderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-reminders/{id}")
    public ResponseEntity<ContactReminderDTO> getContactReminder(@PathVariable Long id) {
        log.debug("REST request to get ContactReminder : {}", id);
        Optional<ContactReminderDTO> contactReminderDTO = contactReminderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactReminderDTO);
    }

    /**
     * {@code DELETE  /contact-reminders/:id} : delete the "id" contactReminder.
     *
     * @param id the id of the contactReminderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-reminders/{id}")
    public ResponseEntity<Void> deleteContactReminder(@PathVariable Long id) {
        log.debug("REST request to delete ContactReminder : {}", id);
        contactReminderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
