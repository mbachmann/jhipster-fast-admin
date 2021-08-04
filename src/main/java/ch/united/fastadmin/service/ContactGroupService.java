package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactGroupDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.ContactGroup}.
 */
public interface ContactGroupService {
    /**
     * Save a contactGroup.
     *
     * @param contactGroupDTO the entity to save.
     * @return the persisted entity.
     */
    ContactGroupDTO save(ContactGroupDTO contactGroupDTO);

    /**
     * Partially updates a contactGroup.
     *
     * @param contactGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactGroupDTO> partialUpdate(ContactGroupDTO contactGroupDTO);

    /**
     * Get all the contactGroups.
     *
     * @return the list of entities.
     */
    List<ContactGroupDTO> findAll();

    /**
     * Get the "id" contactGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactGroupDTO> findOne(Long id);

    /**
     * Delete the "id" contactGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactGroup corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ContactGroupDTO> search(String query);
}
