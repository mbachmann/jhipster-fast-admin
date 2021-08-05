package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.ContactReminder;
import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.repository.search.ContactReminderSearchRepository;
import ch.united.fastadmin.service.ContactReminderService;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import ch.united.fastadmin.service.mapper.ContactReminderMapper;
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
 * Service Implementation for managing {@link ContactReminder}.
 */
@Service
@Transactional
public class ContactReminderServiceImpl implements ContactReminderService {

    private final Logger log = LoggerFactory.getLogger(ContactReminderServiceImpl.class);

    private final ContactReminderRepository contactReminderRepository;

    private final ContactReminderMapper contactReminderMapper;

    private final ContactReminderSearchRepository contactReminderSearchRepository;

    public ContactReminderServiceImpl(
        ContactReminderRepository contactReminderRepository,
        ContactReminderMapper contactReminderMapper,
        ContactReminderSearchRepository contactReminderSearchRepository
    ) {
        this.contactReminderRepository = contactReminderRepository;
        this.contactReminderMapper = contactReminderMapper;
        this.contactReminderSearchRepository = contactReminderSearchRepository;
    }

    @Override
    public ContactReminderDTO save(ContactReminderDTO contactReminderDTO) {
        log.debug("Request to save ContactReminder : {}", contactReminderDTO);
        ContactReminder contactReminder = contactReminderMapper.toEntity(contactReminderDTO);
        contactReminder = contactReminderRepository.save(contactReminder);
        ContactReminderDTO result = contactReminderMapper.toDto(contactReminder);
        contactReminderSearchRepository.save(contactReminder);
        return result;
    }

    @Override
    public Optional<ContactReminderDTO> partialUpdate(ContactReminderDTO contactReminderDTO) {
        log.debug("Request to partially update ContactReminder : {}", contactReminderDTO);

        return contactReminderRepository
            .findById(contactReminderDTO.getId())
            .map(
                existingContactReminder -> {
                    contactReminderMapper.partialUpdate(existingContactReminder, contactReminderDTO);

                    return existingContactReminder;
                }
            )
            .map(contactReminderRepository::save)
            .map(
                savedContactReminder -> {
                    contactReminderSearchRepository.save(savedContactReminder);

                    return savedContactReminder;
                }
            )
            .map(contactReminderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactReminderDTO> findAll() {
        log.debug("Request to get all ContactReminders");
        return contactReminderRepository
            .findAll()
            .stream()
            .map(contactReminderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactReminderDTO> findOne(Long id) {
        log.debug("Request to get ContactReminder : {}", id);
        return contactReminderRepository.findById(id).map(contactReminderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactReminder : {}", id);
        contactReminderRepository.deleteById(id);
        contactReminderSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactReminderDTO> search(String query) {
        log.debug("Request to search ContactReminders for query {}", query);
        return StreamSupport
            .stream(contactReminderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contactReminderMapper::toDto)
            .collect(Collectors.toList());
    }
}
