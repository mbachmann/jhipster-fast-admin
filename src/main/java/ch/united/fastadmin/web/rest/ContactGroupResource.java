package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.service.ContactGroupService;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactGroup}.
 */
@RestController
@RequestMapping("/api")
public class ContactGroupResource {

    private final Logger log = LoggerFactory.getLogger(ContactGroupResource.class);

    private static final String ENTITY_NAME = "contactGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactGroupService contactGroupService;

    private final ContactGroupRepository contactGroupRepository;

    public ContactGroupResource(ContactGroupService contactGroupService, ContactGroupRepository contactGroupRepository) {
        this.contactGroupService = contactGroupService;
        this.contactGroupRepository = contactGroupRepository;
    }

    /**
     * {@code POST  /contact-groups} : Create a new contactGroup.
     *
     * @param contactGroupDTO the contactGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactGroupDTO, or with status {@code 400 (Bad Request)} if the contactGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-groups")
    public ResponseEntity<ContactGroupDTO> createContactGroup(@Valid @RequestBody ContactGroupDTO contactGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactGroup : {}", contactGroupDTO);
        if (contactGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactGroupDTO result = contactGroupService.save(contactGroupDTO);
        return ResponseEntity
            .created(new URI("/api/contact-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-groups/:id} : Updates an existing contactGroup.
     *
     * @param id the id of the contactGroupDTO to save.
     * @param contactGroupDTO the contactGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactGroupDTO,
     * or with status {@code 400 (Bad Request)} if the contactGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-groups/{id}")
    public ResponseEntity<ContactGroupDTO> updateContactGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactGroupDTO contactGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactGroup : {}, {}", id, contactGroupDTO);
        if (contactGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactGroupDTO result = contactGroupService.save(contactGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-groups/:id} : Partial updates given fields of an existing contactGroup, field will ignore if it is null
     *
     * @param id the id of the contactGroupDTO to save.
     * @param contactGroupDTO the contactGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactGroupDTO,
     * or with status {@code 400 (Bad Request)} if the contactGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactGroupDTO> partialUpdateContactGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactGroupDTO contactGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactGroup partially : {}, {}", id, contactGroupDTO);
        if (contactGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactGroupDTO> result = contactGroupService.partialUpdate(contactGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-groups} : get all the contactGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactGroups in body.
     */
    @GetMapping("/contact-groups")
    public ResponseEntity<List<ContactGroupDTO>> getAllContactGroups(Pageable pageable) {
        log.debug("REST request to get a page of ContactGroups");
        Page<ContactGroupDTO> page = contactGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-groups/:id} : get the "id" contactGroup.
     *
     * @param id the id of the contactGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-groups/{id}")
    public ResponseEntity<ContactGroupDTO> getContactGroup(@PathVariable Long id) {
        log.debug("REST request to get ContactGroup : {}", id);
        Optional<ContactGroupDTO> contactGroupDTO = contactGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactGroupDTO);
    }

    /**
     * {@code DELETE  /contact-groups/:id} : delete the "id" contactGroup.
     *
     * @param id the id of the contactGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-groups/{id}")
    public ResponseEntity<Void> deleteContactGroup(@PathVariable Long id) {
        log.debug("REST request to delete ContactGroup : {}", id);
        contactGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
