package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ResourcePermission;
import ch.united.fastadmin.repository.ResourcePermissionRepository;
import ch.united.fastadmin.service.dto.ResourcePermissionDTO;
import ch.united.fastadmin.service.mapper.ResourcePermissionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ResourcePermission}.
 */
@Service
@Transactional
public class ResourcePermissionService {

    private final Logger log = LoggerFactory.getLogger(ResourcePermissionService.class);

    private final ResourcePermissionRepository resourcePermissionRepository;

    private final ResourcePermissionMapper resourcePermissionMapper;

    public ResourcePermissionService(
        ResourcePermissionRepository resourcePermissionRepository,
        ResourcePermissionMapper resourcePermissionMapper
    ) {
        this.resourcePermissionRepository = resourcePermissionRepository;
        this.resourcePermissionMapper = resourcePermissionMapper;
    }

    /**
     * Save a resourcePermission.
     *
     * @param resourcePermissionDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourcePermissionDTO save(ResourcePermissionDTO resourcePermissionDTO) {
        log.debug("Request to save ResourcePermission : {}", resourcePermissionDTO);
        ResourcePermission resourcePermission = resourcePermissionMapper.toEntity(resourcePermissionDTO);
        resourcePermission = resourcePermissionRepository.save(resourcePermission);
        return resourcePermissionMapper.toDto(resourcePermission);
    }

    /**
     * Partially update a resourcePermission.
     *
     * @param resourcePermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourcePermissionDTO> partialUpdate(ResourcePermissionDTO resourcePermissionDTO) {
        log.debug("Request to partially update ResourcePermission : {}", resourcePermissionDTO);

        return resourcePermissionRepository
            .findById(resourcePermissionDTO.getId())
            .map(
                existingResourcePermission -> {
                    resourcePermissionMapper.partialUpdate(existingResourcePermission, resourcePermissionDTO);

                    return existingResourcePermission;
                }
            )
            .map(resourcePermissionRepository::save)
            .map(resourcePermissionMapper::toDto);
    }

    /**
     * Get all the resourcePermissions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResourcePermissionDTO> findAll() {
        log.debug("Request to get all ResourcePermissions");
        return resourcePermissionRepository
            .findAll()
            .stream()
            .map(resourcePermissionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one resourcePermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourcePermissionDTO> findOne(Long id) {
        log.debug("Request to get ResourcePermission : {}", id);
        return resourcePermissionRepository.findById(id).map(resourcePermissionMapper::toDto);
    }

    /**
     * Delete the resourcePermission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourcePermission : {}", id);
        resourcePermissionRepository.deleteById(id);
    }
}
