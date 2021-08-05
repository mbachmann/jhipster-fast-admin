package ch.united.fastadmin.service;

import ch.united.fastadmin.service.dto.PermissionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ch.united.fastadmin.domain.Permission}.
 */
public interface PermissionService {
    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionDTO save(PermissionDTO permissionDTO);

    /**
     * Partially updates a permission.
     *
     * @param permissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDTO);

    /**
     * Get all the permissions.
     *
     * @return the list of entities.
     */
    List<PermissionDTO> findAll();

    /**
     * Get the "id" permission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionDTO> findOne(Long id);

    /**
     * Delete the "id" permission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the permission corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<PermissionDTO> search(String query);
}
