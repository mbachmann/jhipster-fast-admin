package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.CustomFieldDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.CustomField}.
 */
public interface CustomFieldService {
    /**
     * Save a customField.
     *
     * @param customFieldDTO the entity to save.
     * @return the persisted entity.
     */
    CustomFieldDTO save(CustomFieldDTO customFieldDTO);

    /**
     * Partially updates a customField.
     *
     * @param customFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomFieldDTO> partialUpdate(CustomFieldDTO customFieldDTO);

    /**
     * Get all the customFields.
     *
     * @return the list of entities.
     */
    List<CustomFieldDTO> findAll();

    /**
     * Get the "id" customField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomFieldDTO> findOne(Long id);

    /**
     * Delete the "id" customField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the customField corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<CustomFieldDTO> search(String query);
}
