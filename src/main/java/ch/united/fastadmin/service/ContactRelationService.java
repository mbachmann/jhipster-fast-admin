package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ContactRelation;
import ch.united.fastadmin.repository.ContactRelationRepository;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import ch.united.fastadmin.service.mapper.ContactRelationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactRelation}.
 */
@Service
@Transactional
public class ContactRelationService {

    private final Logger log = LoggerFactory.getLogger(ContactRelationService.class);

    private final ContactRelationRepository contactRelationRepository;

    private final ContactRelationMapper contactRelationMapper;

    public ContactRelationService(ContactRelationRepository contactRelationRepository, ContactRelationMapper contactRelationMapper) {
        this.contactRelationRepository = contactRelationRepository;
        this.contactRelationMapper = contactRelationMapper;
    }

    /**
     * Save a contactRelation.
     *
     * @param contactRelationDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactRelationDTO save(ContactRelationDTO contactRelationDTO) {
        log.debug("Request to save ContactRelation : {}", contactRelationDTO);
        ContactRelation contactRelation = contactRelationMapper.toEntity(contactRelationDTO);
        contactRelation = contactRelationRepository.save(contactRelation);
        return contactRelationMapper.toDto(contactRelation);
    }

    /**
     * Partially update a contactRelation.
     *
     * @param contactRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactRelationDTO> partialUpdate(ContactRelationDTO contactRelationDTO) {
        log.debug("Request to partially update ContactRelation : {}", contactRelationDTO);

        return contactRelationRepository
            .findById(contactRelationDTO.getId())
            .map(
                existingContactRelation -> {
                    contactRelationMapper.partialUpdate(existingContactRelation, contactRelationDTO);

                    return existingContactRelation;
                }
            )
            .map(contactRelationRepository::save)
            .map(contactRelationMapper::toDto);
    }

    /**
     * Get all the contactRelations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactRelationDTO> findAll() {
        log.debug("Request to get all ContactRelations");
        return contactRelationRepository
            .findAll()
            .stream()
            .map(contactRelationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contactRelation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactRelationDTO> findOne(Long id) {
        log.debug("Request to get ContactRelation : {}", id);
        return contactRelationRepository.findById(id).map(contactRelationMapper::toDto);
    }

    /**
     * Delete the contactRelation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactRelation : {}", id);
        contactRelationRepository.deleteById(id);
    }
}
