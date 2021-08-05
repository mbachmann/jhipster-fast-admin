package ch.united.fastadmin.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Permission;
import ch.united.fastadmin.repository.PermissionRepository;
import ch.united.fastadmin.repository.search.PermissionSearchRepository;
import ch.united.fastadmin.service.dto.PermissionDTO;
import ch.united.fastadmin.service.mapper.PermissionMapper;
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
 * Service Implementation for managing {@link Permission}.
 */
@Service
@Transactional
public class PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    private final PermissionSearchRepository permissionSearchRepository;

    public PermissionService(
        PermissionRepository permissionRepository,
        PermissionMapper permissionMapper,
        PermissionSearchRepository permissionSearchRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.permissionSearchRepository = permissionSearchRepository;
    }

    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissionDTO save(PermissionDTO permissionDTO) {
        log.debug("Request to save Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        PermissionDTO result = permissionMapper.toDto(permission);
        permissionSearchRepository.save(permission);
        return result;
    }

    /**
     * Partially update a permission.
     *
     * @param permissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDTO) {
        log.debug("Request to partially update Permission : {}", permissionDTO);

        return permissionRepository
            .findById(permissionDTO.getId())
            .map(
                existingPermission -> {
                    permissionMapper.partialUpdate(existingPermission, permissionDTO);

                    return existingPermission;
                }
            )
            .map(permissionRepository::save)
            .map(
                savedPermission -> {
                    permissionSearchRepository.save(savedPermission);

                    return savedPermission;
                }
            )
            .map(permissionMapper::toDto);
    }

    /**
     * Get all the permissions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findAll() {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll().stream().map(permissionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one permission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    /**
     * Delete the permission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
        permissionSearchRepository.deleteById(id);
    }

    /**
     * Search for the permission corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> search(String query) {
        log.debug("Request to search Permissions for query {}", query);
        return StreamSupport
            .stream(permissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(permissionMapper::toDto)
            .collect(Collectors.toList());
    }
}
