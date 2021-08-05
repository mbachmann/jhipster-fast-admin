package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.ContactRelationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.ContactRelation}.
 */
public interface ContactRelationService {
    /**
     * Save a contactRelation.
     *
     * @param contactRelationDTO the entity to save.
     * @return the persisted entity.
     */
    ContactRelationDTO save(ContactRelationDTO contactRelationDTO);

    /**
     * Partially updates a contactRelation.
     *
     * @param contactRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactRelationDTO> partialUpdate(ContactRelationDTO contactRelationDTO);

    /**
     * Get all the contactRelations.
     *
     * @return the list of entities.
     */
    List<ContactRelationDTO> findAll();

    /**
     * Get the "id" contactRelation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactRelationDTO> findOne(Long id);

    /**
     * Delete the "id" contactRelation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contactRelation corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<ContactRelationDTO> search(String query);
}
