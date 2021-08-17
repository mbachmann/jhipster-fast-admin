package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.service.ContactAddressService;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactAddress}.
 */
@RestController
@RequestMapping("/api")
public class ContactAddressResource {

    private final Logger log = LoggerFactory.getLogger(ContactAddressResource.class);

    private static final String ENTITY_NAME = "contactAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactAddressService contactAddressService;

    private final ContactAddressRepository contactAddressRepository;

    public ContactAddressResource(ContactAddressService contactAddressService, ContactAddressRepository contactAddressRepository) {
        this.contactAddressService = contactAddressService;
        this.contactAddressRepository = contactAddressRepository;
    }

    /**
     * {@code POST  /contact-addresses} : Create a new contactAddress.
     *
     * @param contactAddressDTO the contactAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactAddressDTO, or with status {@code 400 (Bad Request)} if the contactAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-addresses")
    public ResponseEntity<ContactAddressDTO> createContactAddress(@Valid @RequestBody ContactAddressDTO contactAddressDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactAddress : {}", contactAddressDTO);
        if (contactAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactAddressDTO result = contactAddressService.save(contactAddressDTO);
        return ResponseEntity
            .created(new URI("/api/contact-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-addresses/:id} : Updates an existing contactAddress.
     *
     * @param id the id of the contactAddressDTO to save.
     * @param contactAddressDTO the contactAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactAddressDTO,
     * or with status {@code 400 (Bad Request)} if the contactAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-addresses/{id}")
    public ResponseEntity<ContactAddressDTO> updateContactAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactAddressDTO contactAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactAddress : {}, {}", id, contactAddressDTO);
        if (contactAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactAddressDTO result = contactAddressService.save(contactAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-addresses/:id} : Partial updates given fields of an existing contactAddress, field will ignore if it is null
     *
     * @param id the id of the contactAddressDTO to save.
     * @param contactAddressDTO the contactAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactAddressDTO,
     * or with status {@code 400 (Bad Request)} if the contactAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactAddressDTO> partialUpdateContactAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactAddressDTO contactAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactAddress partially : {}, {}", id, contactAddressDTO);
        if (contactAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactAddressDTO> result = contactAddressService.partialUpdate(contactAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-addresses} : get all the contactAddresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactAddresses in body.
     */
    @GetMapping("/contact-addresses")
    public ResponseEntity<List<ContactAddressDTO>> getAllContactAddresses(Pageable pageable) {
        log.debug("REST request to get a page of ContactAddresses");
        Page<ContactAddressDTO> page = contactAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-addresses/:id} : get the "id" contactAddress.
     *
     * @param id the id of the contactAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-addresses/{id}")
    public ResponseEntity<ContactAddressDTO> getContactAddress(@PathVariable Long id) {
        log.debug("REST request to get ContactAddress : {}", id);
        Optional<ContactAddressDTO> contactAddressDTO = contactAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactAddressDTO);
    }

    /**
     * {@code DELETE  /contact-addresses/:id} : delete the "id" contactAddress.
     *
     * @param id the id of the contactAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-addresses/{id}")
    public ResponseEntity<Void> deleteContactAddress(@PathVariable Long id) {
        log.debug("REST request to delete ContactAddress : {}", id);
        contactAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
