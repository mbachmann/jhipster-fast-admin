package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.repository.search.ContactAddressSearchRepository;
import ch.united.fastadmin.service.ContactAddressService;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import ch.united.fastadmin.service.mapper.ContactAddressMapper;
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
 * Service Implementation for managing {@link ContactAddress}.
 */
@Service
@Transactional
public class ContactAddressServiceImpl implements ContactAddressService {

    private final Logger log = LoggerFactory.getLogger(ContactAddressServiceImpl.class);

    private final ContactAddressRepository contactAddressRepository;

    private final ContactAddressMapper contactAddressMapper;

    private final ContactAddressSearchRepository contactAddressSearchRepository;

    public ContactAddressServiceImpl(
        ContactAddressRepository contactAddressRepository,
        ContactAddressMapper contactAddressMapper,
        ContactAddressSearchRepository contactAddressSearchRepository
    ) {
        this.contactAddressRepository = contactAddressRepository;
        this.contactAddressMapper = contactAddressMapper;
        this.contactAddressSearchRepository = contactAddressSearchRepository;
    }

    @Override
    public ContactAddressDTO save(ContactAddressDTO contactAddressDTO) {
        log.debug("Request to save ContactAddress : {}", contactAddressDTO);
        ContactAddress contactAddress = contactAddressMapper.toEntity(contactAddressDTO);
        contactAddress = contactAddressRepository.save(contactAddress);
        ContactAddressDTO result = contactAddressMapper.toDto(contactAddress);
        contactAddressSearchRepository.save(contactAddress);
        return result;
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ContactAddressDTO> findAll() {
        log.debug("Request to get all ContactAddresses");
        return contactAddressRepository
            .findAll()
            .stream()
            .map(contactAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactAddressDTO> findOne(Long id) {
        log.debug("Request to get ContactAddress : {}", id);
        return contactAddressRepository.findById(id).map(contactAddressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactAddress : {}", id);
        contactAddressRepository.deleteById(id);
        contactAddressSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactAddressDTO> search(String query) {
        log.debug("Request to search ContactAddresses for query {}", query);
        return StreamSupport
            .stream(contactAddressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contactAddressMapper::toDto)
            .collect(Collectors.toList());
    }
}
