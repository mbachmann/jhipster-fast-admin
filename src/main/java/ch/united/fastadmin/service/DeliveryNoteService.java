package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.DeliveryNote;
import ch.united.fastadmin.repository.DeliveryNoteRepository;
import ch.united.fastadmin.service.dto.DeliveryNoteDTO;
import ch.united.fastadmin.service.mapper.DeliveryNoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryNote}.
 */
@Service
@Transactional
public class DeliveryNoteService {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteService.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteMapper deliveryNoteMapper;

    public DeliveryNoteService(DeliveryNoteRepository deliveryNoteRepository, DeliveryNoteMapper deliveryNoteMapper) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteMapper = deliveryNoteMapper;
    }

    /**
     * Save a deliveryNote.
     *
     * @param deliveryNoteDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryNoteDTO save(DeliveryNoteDTO deliveryNoteDTO) {
        log.debug("Request to save DeliveryNote : {}", deliveryNoteDTO);
        DeliveryNote deliveryNote = deliveryNoteMapper.toEntity(deliveryNoteDTO);
        deliveryNote = deliveryNoteRepository.save(deliveryNote);
        return deliveryNoteMapper.toDto(deliveryNote);
    }

    /**
     * Partially update a deliveryNote.
     *
     * @param deliveryNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryNoteDTO> partialUpdate(DeliveryNoteDTO deliveryNoteDTO) {
        log.debug("Request to partially update DeliveryNote : {}", deliveryNoteDTO);

        return deliveryNoteRepository
            .findById(deliveryNoteDTO.getId())
            .map(
                existingDeliveryNote -> {
                    deliveryNoteMapper.partialUpdate(existingDeliveryNote, deliveryNoteDTO);

                    return existingDeliveryNote;
                }
            )
            .map(deliveryNoteRepository::save)
            .map(deliveryNoteMapper::toDto);
    }

    /**
     * Get all the deliveryNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryNoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryNotes");
        return deliveryNoteRepository.findAll(pageable).map(deliveryNoteMapper::toDto);
    }

    /**
     * Get one deliveryNote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryNoteDTO> findOne(Long id) {
        log.debug("Request to get DeliveryNote : {}", id);
        return deliveryNoteRepository.findById(id).map(deliveryNoteMapper::toDto);
    }

    /**
     * Delete the deliveryNote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryNote : {}", id);
        deliveryNoteRepository.deleteById(id);
    }
}
