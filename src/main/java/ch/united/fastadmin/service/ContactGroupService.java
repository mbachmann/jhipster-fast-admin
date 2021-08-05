package ch.united.fastadmin.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.repository.search.ContactGroupSearchRepository;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import ch.united.fastadmin.service.mapper.ContactGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final ContactGroupSearchRepository contactGroupSearchRepository;

    public ContactGroupService(
        ContactGroupRepository contactGroupRepository,
        ContactGroupMapper contactGroupMapper,
        ContactGroupSearchRepository contactGroupSearchRepository
    ) {
        this.contactGroupRepository = contactGroupRepository;
        this.contactGroupMapper = contactGroupMapper;
        this.contactGroupSearchRepository = contactGroupSearchRepository;
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
        ContactGroupDTO result = contactGroupMapper.toDto(contactGroup);
        contactGroupSearchRepository.save(contactGroup);
        return result;
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
            .map(
                savedContactGroup -> {
                    contactGroupSearchRepository.save(savedContactGroup);

                    return savedContactGroup;
                }
            )
            .map(contactGroupMapper::toDto);
    }

    /**
     * Get all the contactGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactGroupDTO> findAll() {
        log.debug("Request to get all ContactGroups");
        return contactGroupRepository.findAll().stream().map(contactGroupMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
        contactGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the contactGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactGroupDTO> search(String query) {
        log.debug("Request to search ContactGroups for query {}", query);
        return StreamSupport
            .stream(contactGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contactGroupMapper::toDto)
            .collect(Collectors.toList());
    }
}
