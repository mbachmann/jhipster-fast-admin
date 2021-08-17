package ch.united.fastadmin.web.rest;

import ch.united.fastadmin.repository.OrderConfirmationRepository;
import ch.united.fastadmin.service.OrderConfirmationQueryService;
import ch.united.fastadmin.service.OrderConfirmationService;
import ch.united.fastadmin.service.criteria.OrderConfirmationCriteria;
import ch.united.fastadmin.service.dto.OrderConfirmationDTO;
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
 * REST controller for managing {@link ch.united.fastadmin.domain.OrderConfirmation}.
 */
@RestController
@RequestMapping("/api")
public class OrderConfirmationResource {

    private final Logger log = LoggerFactory.getLogger(OrderConfirmationResource.class);

    private static final String ENTITY_NAME = "orderConfirmation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderConfirmationService orderConfirmationService;

    private final OrderConfirmationRepository orderConfirmationRepository;

    private final OrderConfirmationQueryService orderConfirmationQueryService;

    public OrderConfirmationResource(
        OrderConfirmationService orderConfirmationService,
        OrderConfirmationRepository orderConfirmationRepository,
        OrderConfirmationQueryService orderConfirmationQueryService
    ) {
        this.orderConfirmationService = orderConfirmationService;
        this.orderConfirmationRepository = orderConfirmationRepository;
        this.orderConfirmationQueryService = orderConfirmationQueryService;
    }

    /**
     * {@code POST  /order-confirmations} : Create a new orderConfirmation.
     *
     * @param orderConfirmationDTO the orderConfirmationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderConfirmationDTO, or with status {@code 400 (Bad Request)} if the orderConfirmation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-confirmations")
    public ResponseEntity<OrderConfirmationDTO> createOrderConfirmation(@Valid @RequestBody OrderConfirmationDTO orderConfirmationDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrderConfirmation : {}", orderConfirmationDTO);
        if (orderConfirmationDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderConfirmation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderConfirmationDTO result = orderConfirmationService.save(orderConfirmationDTO);
        return ResponseEntity
            .created(new URI("/api/order-confirmations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-confirmations/:id} : Updates an existing orderConfirmation.
     *
     * @param id the id of the orderConfirmationDTO to save.
     * @param orderConfirmationDTO the orderConfirmationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderConfirmationDTO,
     * or with status {@code 400 (Bad Request)} if the orderConfirmationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderConfirmationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-confirmations/{id}")
    public ResponseEntity<OrderConfirmationDTO> updateOrderConfirmation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderConfirmationDTO orderConfirmationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderConfirmation : {}, {}", id, orderConfirmationDTO);
        if (orderConfirmationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderConfirmationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderConfirmationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderConfirmationDTO result = orderConfirmationService.save(orderConfirmationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderConfirmationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-confirmations/:id} : Partial updates given fields of an existing orderConfirmation, field will ignore if it is null
     *
     * @param id the id of the orderConfirmationDTO to save.
     * @param orderConfirmationDTO the orderConfirmationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderConfirmationDTO,
     * or with status {@code 400 (Bad Request)} if the orderConfirmationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderConfirmationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderConfirmationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-confirmations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderConfirmationDTO> partialUpdateOrderConfirmation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderConfirmationDTO orderConfirmationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderConfirmation partially : {}, {}", id, orderConfirmationDTO);
        if (orderConfirmationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderConfirmationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderConfirmationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderConfirmationDTO> result = orderConfirmationService.partialUpdate(orderConfirmationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderConfirmationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-confirmations} : get all the orderConfirmations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderConfirmations in body.
     */
    @GetMapping("/order-confirmations")
    public ResponseEntity<List<OrderConfirmationDTO>> getAllOrderConfirmations(OrderConfirmationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderConfirmations by criteria: {}", criteria);
        Page<OrderConfirmationDTO> page = orderConfirmationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-confirmations/count} : count all the orderConfirmations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/order-confirmations/count")
    public ResponseEntity<Long> countOrderConfirmations(OrderConfirmationCriteria criteria) {
        log.debug("REST request to count OrderConfirmations by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderConfirmationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-confirmations/:id} : get the "id" orderConfirmation.
     *
     * @param id the id of the orderConfirmationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderConfirmationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-confirmations/{id}")
    public ResponseEntity<OrderConfirmationDTO> getOrderConfirmation(@PathVariable Long id) {
        log.debug("REST request to get OrderConfirmation : {}", id);
        Optional<OrderConfirmationDTO> orderConfirmationDTO = orderConfirmationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderConfirmationDTO);
    }

    /**
     * {@code DELETE  /order-confirmations/:id} : delete the "id" orderConfirmation.
     *
     * @param id the id of the orderConfirmationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-confirmations/{id}")
    public ResponseEntity<Void> deleteOrderConfirmation(@PathVariable Long id) {
        log.debug("REST request to delete OrderConfirmation : {}", id);
        orderConfirmationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
