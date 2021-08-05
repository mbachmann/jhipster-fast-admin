package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.repository.search.ContactPersonSearchRepository;
import ch.united.fastadmin.service.ContactPersonService;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.service.mapper.ContactPersonMapper;
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
 * Service Implementation for managing {@link ContactPerson}.
 */
@Service
@Transactional
public class ContactPersonServiceImpl implements ContactPersonService {

    private final Logger log = LoggerFactory.getLogger(ContactPersonServiceImpl.class);

    private final ContactPersonRepository contactPersonRepository;

    private final ContactPersonMapper contactPersonMapper;

    private final ContactPersonSearchRepository contactPersonSearchRepository;

    public ContactPersonServiceImpl(
        ContactPersonRepository contactPersonRepository,
        ContactPersonMapper contactPersonMapper,
        ContactPersonSearchRepository contactPersonSearchRepository
    ) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
        this.contactPersonSearchRepository = contactPersonSearchRepository;
    }

    @Override
    public ContactPersonDTO save(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to save ContactPerson : {}", contactPersonDTO);
        ContactPerson contactPerson = contactPersonMapper.toEntity(contactPersonDTO);
        contactPerson = contactPersonRepository.save(contactPerson);
        ContactPersonDTO result = contactPersonMapper.toDto(contactPerson);
        contactPersonSearchRepository.save(contactPerson);
        return result;
    }

    @Override
    public Optional<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to partially update ContactPerson : {}", contactPersonDTO);

        return contactPersonRepository
            .findById(contactPersonDTO.getId())
            .map(
                existingContactPerson -> {
                    contactPersonMapper.partialUpdate(existingContactPerson, contactPersonDTO);

                    return existingContactPerson;
                }
            )
            .map(contactPersonRepository::save)
            .map(
                savedContactPerson -> {
                    contactPersonSearchRepository.save(savedContactPerson);

                    return savedContactPerson;
                }
            )
            .map(contactPersonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactPersonDTO> findAll() {
        log.debug("Request to get all ContactPeople");
        return contactPersonRepository.findAll().stream().map(contactPersonMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactPersonDTO> findOne(Long id) {
        log.debug("Request to get ContactPerson : {}", id);
        return contactPersonRepository.findById(id).map(contactPersonMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactPerson : {}", id);
        contactPersonRepository.deleteById(id);
        contactPersonSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactPersonDTO> search(String query) {
        log.debug("Request to search ContactPeople for query {}", query);
        return StreamSupport
            .stream(contactPersonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contactPersonMapper::toDto)
            .collect(Collectors.toList());
    }
}
