package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.repository.ContactRepository;
import ch.united.fastadmin.repository.search.ContactSearchRepository;
import ch.united.fastadmin.service.ContactService;
import ch.united.fastadmin.service.dto.ContactDTO;
import ch.united.fastadmin.service.mapper.ContactMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contact}.
 */
@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    private final ContactSearchRepository contactSearchRepository;

    public ContactServiceImpl(
        ContactRepository contactRepository,
        ContactMapper contactMapper,
        ContactSearchRepository contactSearchRepository
    ) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.contactSearchRepository = contactSearchRepository;
    }

    @Override
    public ContactDTO save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);
        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);
        ContactDTO result = contactMapper.toDto(contact);
        contactSearchRepository.save(contact);
        return result;
    }

    @Override
    public Optional<ContactDTO> partialUpdate(ContactDTO contactDTO) {
        log.debug("Request to partially update Contact : {}", contactDTO);

        return contactRepository
            .findById(contactDTO.getId())
            .map(
                existingContact -> {
                    contactMapper.partialUpdate(existingContact, contactDTO);

                    return existingContact;
                }
            )
            .map(contactRepository::save)
            .map(
                savedContact -> {
                    contactSearchRepository.save(savedContact);

                    return savedContact;
                }
            )
            .map(contactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        return contactRepository.findAll(pageable).map(contactMapper::toDto);
    }

    public Page<ContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contactRepository.findAllWithEagerRelationships(pageable).map(contactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDTO> findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findOneWithEagerRelationships(id).map(contactMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.deleteById(id);
        contactSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Contacts for query {}", query);
        return contactSearchRepository.search(queryStringQuery(query), pageable).map(contactMapper::toDto);
    }
}
