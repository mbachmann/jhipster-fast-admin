package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ContactReminder;
import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import ch.united.fastadmin.service.mapper.ContactReminderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactReminder}.
 */
@Service
@Transactional
public class ContactReminderService {

    private final Logger log = LoggerFactory.getLogger(ContactReminderService.class);

    private final ContactReminderRepository contactReminderRepository;

    private final ContactReminderMapper contactReminderMapper;

    public ContactReminderService(ContactReminderRepository contactReminderRepository, ContactReminderMapper contactReminderMapper) {
        this.contactReminderRepository = contactReminderRepository;
        this.contactReminderMapper = contactReminderMapper;
    }

    /**
     * Save a contactReminder.
     *
     * @param contactReminderDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactReminderDTO save(ContactReminderDTO contactReminderDTO) {
        log.debug("Request to save ContactReminder : {}", contactReminderDTO);
        ContactReminder contactReminder = contactReminderMapper.toEntity(contactReminderDTO);
        contactReminder = contactReminderRepository.save(contactReminder);
        return contactReminderMapper.toDto(contactReminder);
    }

    /**
     * Partially update a contactReminder.
     *
     * @param contactReminderDTO the entity to update partially.
     * @return the persisted entity.
     */
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
            .map(contactReminderMapper::toDto);
    }

    /**
     * Get all the contactReminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactReminderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactReminders");
        return contactReminderRepository.findAll(pageable).map(contactReminderMapper::toDto);
    }

    /**
     * Get one contactReminder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactReminderDTO> findOne(Long id) {
        log.debug("Request to get ContactReminder : {}", id);
        return contactReminderRepository.findById(id).map(contactReminderMapper::toDto);
    }

    /**
     * Delete the contactReminder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactReminder : {}", id);
        contactReminderRepository.deleteById(id);
    }
}
