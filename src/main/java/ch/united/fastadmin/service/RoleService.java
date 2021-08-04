package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.Role}.
 */
public interface RoleService {
    /**
     * Save a role.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    RoleDTO save(RoleDTO roleDTO);

    /**
     * Partially updates a role.
     *
     * @param roleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoleDTO> partialUpdate(RoleDTO roleDTO);

    /**
     * Get all the roles.
     *
     * @return the list of entities.
     */
    List<RoleDTO> findAll();

    /**
     * Get the "id" role.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleDTO> findOne(Long id);

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the role corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<RoleDTO> search(String query);
}
