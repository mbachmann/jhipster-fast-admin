package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import ch.united.fastadmin.service.mapper.ContactGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactGroup}.
 */
@Service
@Transactional
public class ContactGroupService {

    private final Logger log = LoggerFactory.getLogger(ContactGroupService.class);

    private final ContactGroupRepository contactGroupRepository;

    private final ContactGroupMapper contactGroupMapper;

    public ContactGroupService(ContactGroupRepository contactGroupRepository, ContactGroupMapper contactGroupMapper) {
        this.contactGroupRepository = contactGroupRepository;
        this.contactGroupMapper = contactGroupMapper;
    }

    /**
     * Save a contactGroup.
     *
     * @param contactGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactGroupDTO save(ContactGroupDTO contactGroupDTO) {
        log.debug("Request to save ContactGroup : {}", contactGroupDTO);
        ContactGroup contactGroup = contactGroupMapper.toEntity(contactGroupDTO);
        contactGroup = contactGroupRepository.save(contactGroup);
        return contactGroupMapper.toDto(contactGroup);
    }

    /**
     * Partially update a contactGroup.
     *
     * @param contactGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactGroupDTO> partialUpdate(ContactGroupDTO contactGroupDTO) {
        log.debug("Request to partially update ContactGroup : {}", contactGroupDTO);

        return contactGroupRepository
            .findById(contactGroupDTO.getId())
            .map(
                existingContactGroup -> {
                    contactGroupMapper.partialUpdate(existingContactGroup, contactGroupDTO);

                    return existingContactGroup;
                }
            )
            .map(contactGroupRepository::save)
            .map(contactGroupMapper::toDto);
    }

    /**
     * Get all the contactGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactGroups");
        return contactGroupRepository.findAll(pageable).map(contactGroupMapper::toDto);
    }

    /**
     * Get one contactGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactGroupDTO> findOne(Long id) {
        log.debug("Request to get ContactGroup : {}", id);
        return contactGroupRepository.findById(id).map(contactGroupMapper::toDto);
    }

    /**
     * Delete the contactGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactGroup : {}", id);
        contactGroupRepository.deleteById(id);
    }
}
