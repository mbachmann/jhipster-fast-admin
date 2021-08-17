package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.SignatureRepository;
import ch.united.fastadmin.service.SignatureService;
import ch.united.fastadmin.service.dto.SignatureDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.united.fastadmin.domain.Signature}.
 */
@RestController
@RequestMapping("/api")
public class SignatureResource {

    private final Logger log = LoggerFactory.getLogger(SignatureResource.class);

    private static final String ENTITY_NAME = "signature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignatureService signatureService;

    private final SignatureRepository signatureRepository;

    public SignatureResource(SignatureService signatureService, SignatureRepository signatureRepository) {
        this.signatureService = signatureService;
        this.signatureRepository = signatureRepository;
    }

    /**
     * {@code POST  /signatures} : Create a new signature.
     *
     * @param signatureDTO the signatureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signatureDTO, or with status {@code 400 (Bad Request)} if the signature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signatures")
    public ResponseEntity<SignatureDTO> createSignature(@Valid @RequestBody SignatureDTO signatureDTO) throws URISyntaxException {
        log.debug("REST request to save Signature : {}", signatureDTO);
        if (signatureDTO.getId() != null) {
            throw new BadRequestAlertException("A new signature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignatureDTO result = signatureService.save(signatureDTO);
        return ResponseEntity
            .created(new URI("/api/signatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signatures/:id} : Updates an existing signature.
     *
     * @param id the id of the signatureDTO to save.
     * @param signatureDTO the signatureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signatureDTO,
     * or with status {@code 400 (Bad Request)} if the signatureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signatureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signatures/{id}")
    public ResponseEntity<SignatureDTO> updateSignature(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignatureDTO signatureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Signature : {}, {}", id, signatureDTO);
        if (signatureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signatureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SignatureDTO result = signatureService.save(signatureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signatureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /signatures/:id} : Partial updates given fields of an existing signature, field will ignore if it is null
     *
     * @param id the id of the signatureDTO to save.
     * @param signatureDTO the signatureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signatureDTO,
     * or with status {@code 400 (Bad Request)} if the signatureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the signatureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the signatureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/signatures/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SignatureDTO> partialUpdateSignature(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignatureDTO signatureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Signature partially : {}, {}", id, signatureDTO);
        if (signatureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signatureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signatureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignatureDTO> result = signatureService.partialUpdate(signatureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signatureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /signatures} : get all the signatures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signatures in body.
     */
    @GetMapping("/signatures")
    public List<SignatureDTO> getAllSignatures() {
        log.debug("REST request to get all Signatures");
        return signatureService.findAll();
    }

    /**
     * {@code GET  /signatures/:id} : get the "id" signature.
     *
     * @param id the id of the signatureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signatureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signatures/{id}")
    public ResponseEntity<SignatureDTO> getSignature(@PathVariable Long id) {
        log.debug("REST request to get Signature : {}", id);
        Optional<SignatureDTO> signatureDTO = signatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signatureDTO);
    }

    /**
     * {@code DELETE  /signatures/:id} : delete the "id" signature.
     *
     * @param id the id of the signatureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signatures/{id}")
    public ResponseEntity<Void> deleteSignature(@PathVariable Long id) {
        log.debug("REST request to delete Signature : {}", id);
        signatureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
