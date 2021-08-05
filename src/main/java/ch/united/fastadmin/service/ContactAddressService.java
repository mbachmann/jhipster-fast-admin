package ch.united.fastadmin.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.repository.search.ContactAddressSearchRepository;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import ch.united.fastadmin.service.mapper.ContactAddressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactAddress}.
 */
@Service
@Transactional
public class ContactAddressService {

    private final Logger log = LoggerFactory.getLogger(ContactAddressService.class);

    private final ContactAddressRepository contactAddressRepository;

    private final ContactAddressMapper contactAddressMapper;

    private final ContactAddressSearchRepository contactAddressSearchRepository;

    public ContactAddressService(
        ContactAddressRepository contactAddressRepository,
        ContactAddressMapper contactAddressMapper,
        ContactAddressSearchRepository contactAddressSearchRepository
    ) {
        this.contactAddressRepository = contactAddressRepository;
        this.contactAddressMapper = contactAddressMapper;
        this.contactAddressSearchRepository = contactAddressSearchRepository;
    }

    /**
     * Save a contactAddress.
     *
     * @param contactAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactAddressDTO save(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to save ContactAddress : {}", contactAddressDTO);
        ContactAddress contactAddress = contactAddressMapper.toEntity(contactAddressDTO);
        contactAddress = contactAddressRepository.save(contactAddress);
        ContactAddressDTO result = contactAddressMapper.toDto(contactAddress);
        contactAddressSearchRepository.save(contactAddress);
        return result;
    }

    /**
     * Partially update a contactAddress.
     *
     * @param contactAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactAddressDTO> partialUpdate(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to partially update ContactAddress : {}", contactAddressDTO);

        return contactAddressRepository
            .findById(contactAddressDTO.getId())
            .map(
                existingContactAddress -> {
                    contactAddressMapper.partialUpdate(existingContactAddress, contactAddressDTO);

                    return existingContactAddress;
                }
            )
            .map(contactAddressRepository::save)
            .map(
                savedContactAddress -> {
                    contactAddressSearchRepository.save(savedContactAddress);

                    return savedContactAddress;
                }
            )
            .map(contactAddressMapper::toDto);
    }

    /**
     * Get all the contactAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactAddresses");
        return contactAddressRepository.findAll(pageable).map(contactAddressMapper::toDto);
    }

    /**
     * Get one contactAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactAddressDTO> findOne(Long id) {
        log.debug("Request to get ContactAddress : {}", id);
        return contactAddressRepository.findById(id).map(contactAddressMapper::toDto);
    }

    /**
     * Delete the contactAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactAddress : {}", id);
        contactAddressRepository.deleteById(id);
        contactAddressSearchRepository.deleteById(id);
    }

    /**
     * Search for the contactAddress corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactAddressDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactAddresses for query {}", query);
        return contactAddressSearchRepository.search(queryStringQuery(query), pageable).map(contactAddressMapper::toDto);
    }
}
