package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.OwnerDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.Owner}.
 */
public interface OwnerService {
    /**
     * Save a owner.
     *
     * @param ownerDTO the entity to save.
     * @return the persisted entity.
     */
    OwnerDTO save(OwnerDTO ownerDTO);

    /**
     * Partially updates a owner.
     *
     * @param ownerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO);

    /**
     * Get all the owners.
     *
     * @return the list of entities.
     */
    List<OwnerDTO> findAll();

    /**
     * Get the "id" owner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnerDTO> findOne(Long id);

    /**
     * Delete the "id" owner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the owner corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<OwnerDTO> search(String query);
}
