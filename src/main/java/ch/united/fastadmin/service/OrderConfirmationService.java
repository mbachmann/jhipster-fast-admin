package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.OrderConfirmation;
import ch.united.fastadmin.repository.OrderConfirmationRepository;
import ch.united.fastadmin.service.dto.OrderConfirmationDTO;
import ch.united.fastadmin.service.mapper.OrderConfirmationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderConfirmation}.
 */
@Service
@Transactional
public class OrderConfirmationService {

    private final Logger log = LoggerFactory.getLogger(OrderConfirmationService.class);

    private final OrderConfirmationRepository orderConfirmationRepository;

    private final OrderConfirmationMapper orderConfirmationMapper;

    public OrderConfirmationService(
        OrderConfirmationRepository orderConfirmationRepository,
        OrderConfirmationMapper orderConfirmationMapper
    ) {
        this.orderConfirmationRepository = orderConfirmationRepository;
        this.orderConfirmationMapper = orderConfirmationMapper;
    }

    /**
     * Save a orderConfirmation.
     *
     * @param orderConfirmationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderConfirmationDTO save(OrderConfirmationDTO orderConfirmationDTO) {
        log.debug("Request to save OrderConfirmation : {}", orderConfirmationDTO);
        OrderConfirmation orderConfirmation = orderConfirmationMapper.toEntity(orderConfirmationDTO);
        orderConfirmation = orderConfirmationRepository.save(orderConfirmation);
        return orderConfirmationMapper.toDto(orderConfirmation);
    }

    /**
     * Partially update a orderConfirmation.
     *
     * @param orderConfirmationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderConfirmationDTO> partialUpdate(OrderConfirmationDTO orderConfirmationDTO) {
        log.debug("Request to partially update OrderConfirmation : {}", orderConfirmationDTO);

        return orderConfirmationRepository
            .findById(orderConfirmationDTO.getId())
            .map(
                existingOrderConfirmation -> {
                    orderConfirmationMapper.partialUpdate(existingOrderConfirmation, orderConfirmationDTO);

                    return existingOrderConfirmation;
                }
            )
            .map(orderConfirmationRepository::save)
            .map(orderConfirmationMapper::toDto);
    }

    /**
     * Get all the orderConfirmations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderConfirmationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderConfirmations");
        return orderConfirmationRepository.findAll(pageable).map(orderConfirmationMapper::toDto);
    }

    /**
     * Get one orderConfirmation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderConfirmationDTO> findOne(Long id) {
        log.debug("Request to get OrderConfirmation : {}", id);
        return orderConfirmationRepository.findById(id).map(orderConfirmationMapper::toDto);
    }

    /**
     * Delete the orderConfirmation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderConfirmation : {}", id);
        orderConfirmationRepository.deleteById(id);
    }
}
