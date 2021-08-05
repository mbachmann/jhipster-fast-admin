package ch.united.fastadmin.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.repository.ContactRelationRepository;
import ch.united.fastadmin.service.ContactRelationService;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import ch.united.fastadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.united.fastadmin.domain.ContactRelation}.
 */
@RestController
@RequestMapping("/api")
public class ContactRelationResource {

    private final Logger log = LoggerFactory.getLogger(ContactRelationResource.class);

    private static final String ENTITY_NAME = "contactRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactRelationService contactRelationService;

    private final ContactRelationRepository contactRelationRepository;

    public ContactRelationResource(ContactRelationService contactRelationService, ContactRelationRepository contactRelationRepository) {
        this.contactRelationService = contactRelationService;
        this.contactRelationRepository = contactRelationRepository;
    }

    /**
     * {@code POST  /contact-relations} : Create a new contactRelation.
     *
     * @param contactRelationDTO the contactRelationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactRelationDTO, or with status {@code 400 (Bad Request)} if the contactRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-relations")
    public ResponseEntity<ContactRelationDTO> createContactRelation(@RequestBody ContactRelationDTO contactRelationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactRelation : {}", contactRelationDTO);
        if (contactRelationDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactRelationDTO result = contactRelationService.save(contactRelationDTO);
        return ResponseEntity
            .created(new URI("/api/contact-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-relations/:id} : Updates an existing contactRelation.
     *
     * @param id the id of the contactRelationDTO to save.
     * @param contactRelationDTO the contactRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactRelationDTO,
     * or with status {@code 400 (Bad Request)} if the contactRelationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-relations/{id}")
    public ResponseEntity<ContactRelationDTO> updateContactRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactRelationDTO contactRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactRelation : {}, {}", id, contactRelationDTO);
        if (contactRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactRelationDTO result = contactRelationService.save(contactRelationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactRelationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-relations/:id} : Partial updates given fields of an existing contactRelation, field will ignore if it is null
     *
     * @param id the id of the contactRelationDTO to save.
     * @param contactRelationDTO the contactRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactRelationDTO,
     * or with status {@code 400 (Bad Request)} if the contactRelationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactRelationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-relations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContactRelationDTO> partialUpdateContactRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactRelationDTO contactRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactRelation partially : {}, {}", id, contactRelationDTO);
        if (contactRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactRelationDTO> result = contactRelationService.partialUpdate(contactRelationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactRelationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-relations} : get all the contactRelations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactRelations in body.
     */
    @GetMapping("/contact-relations")
    public List<ContactRelationDTO> getAllContactRelations() {
        log.debug("REST request to get all ContactRelations");
        return contactRelationService.findAll();
    }

    /**
     * {@code GET  /contact-relations/:id} : get the "id" contactRelation.
     *
     * @param id the id of the contactRelationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactRelationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-relations/{id}")
    public ResponseEntity<ContactRelationDTO> getContactRelation(@PathVariable Long id) {
        log.debug("REST request to get ContactRelation : {}", id);
        Optional<ContactRelationDTO> contactRelationDTO = contactRelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactRelationDTO);
    }

    /**
     * {@code DELETE  /contact-relations/:id} : delete the "id" contactRelation.
     *
     * @param id the id of the contactRelationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-relations/{id}")
    public ResponseEntity<Void> deleteContactRelation(@PathVariable Long id) {
        log.debug("REST request to delete ContactRelation : {}", id);
        contactRelationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/contact-relations?query=:query} : search for the contactRelation corresponding
     * to the query.
     *
     * @param query the query of the contactRelation search.
     * @return the result of the search.
     */
    @GetMapping("/_search/contact-relations")
    public List<ContactRelationDTO> searchContactRelations(@RequestParam String query) {
        log.debug("REST request to search ContactRelations for query {}", query);
        return contactRelationService.search(query);
    }
}
