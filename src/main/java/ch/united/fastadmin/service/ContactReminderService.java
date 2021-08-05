package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactReminderDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.ContactReminder}.
 */
public interface ContactReminderService {
    /**
     * Save a contactReminder.
     *
     * @param contactReminderDTO the entity to save.
     * @return the persisted entity.
     */
    ContactReminderDTO save(ContactReminderDTO contactReminderDTO);

    /**
     * Partially updates a contactReminder.
     *
     * @param contactReminderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactReminderDTO> partialUpdate(ContactReminderDTO contactReminderDTO);

    /**
     * Get all the contactReminders.
     *
     * @return the list of entities.
     */
    List<ContactReminderDTO> findAll();

    /**
     * Get the "id" contactReminder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactReminderDTO> findOne(Long id);

    /**
     * Delete the "id" contactReminder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactReminder corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ContactReminderDTO> search(String query);
}
