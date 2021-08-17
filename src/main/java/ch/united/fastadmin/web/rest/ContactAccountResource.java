package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ContactAccountRepository;
import ch.united.fastadmin.service.ContactAccountService;
import ch.united.fastadmin.service.dto.ContactAccountDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactAccount}.
 */
@RestController
@RequestMapping("/api")
public class ContactAccountResource {

    private final Logger log = LoggerFactory.getLogger(ContactAccountResource.class);

    private static final String ENTITY_NAME = "contactAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactAccountService contactAccountService;

    private final ContactAccountRepository contactAccountRepository;

    public ContactAccountResource(ContactAccountService contactAccountService, ContactAccountRepository contactAccountRepository) {
        this.contactAccountService = contactAccountService;
        this.contactAccountRepository = contactAccountRepository;
    }

    /**
     * {@code POST  /contact-accounts} : Create a new contactAccount.
     *
     * @param contactAccountDTO the contactAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactAccountDTO, or with status {@code 400 (Bad Request)} if the contactAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-accounts")
    public ResponseEntity<ContactAccountDTO> createContactAccount(@Valid @RequestBody ContactAccountDTO contactAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactAccount : {}", contactAccountDTO);
        if (contactAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactAccountDTO result = contactAccountService.save(contactAccountDTO);
        return ResponseEntity
            .created(new URI("/api/contact-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-accounts/:id} : Updates an existing contactAccount.
     *
     * @param id the id of the contactAccountDTO to save.
     * @param contactAccountDTO the contactAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactAccountDTO,
     * or with status {@code 400 (Bad Request)} if the contactAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-accounts/{id}")
    public ResponseEntity<ContactAccountDTO> updateContactAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactAccountDTO contactAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactAccount : {}, {}", id, contactAccountDTO);
        if (contactAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactAccountDTO result = contactAccountService.save(contactAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-accounts/:id} : Partial updates given fields of an existing contactAccount, field will ignore if it is null
     *
     * @param id the id of the contactAccountDTO to save.
     * @param contactAccountDTO the contactAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactAccountDTO,
     * or with status {@code 400 (Bad Request)} if the contactAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactAccountDTO> partialUpdateContactAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactAccountDTO contactAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactAccount partially : {}, {}", id, contactAccountDTO);
        if (contactAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactAccountDTO> result = contactAccountService.partialUpdate(contactAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-accounts} : get all the contactAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactAccounts in body.
     */
    @GetMapping("/contact-accounts")
    public ResponseEntity<List<ContactAccountDTO>> getAllContactAccounts(Pageable pageable) {
        log.debug("REST request to get a page of ContactAccounts");
        Page<ContactAccountDTO> page = contactAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-accounts/:id} : get the "id" contactAccount.
     *
     * @param id the id of the contactAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-accounts/{id}")
    public ResponseEntity<ContactAccountDTO> getContactAccount(@PathVariable Long id) {
        log.debug("REST request to get ContactAccount : {}", id);
        Optional<ContactAccountDTO> contactAccountDTO = contactAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactAccountDTO);
    }

    /**
     * {@code DELETE  /contact-accounts/:id} : delete the "id" contactAccount.
     *
     * @param id the id of the contactAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-accounts/{id}")
    public ResponseEntity<Void> deleteContactAccount(@PathVariable Long id) {
        log.debug("REST request to delete ContactAccount : {}", id);
        contactAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
