package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Permission;
import ch.united.fastadmin.repository.PermissionRepository;
import ch.united.fastadmin.repository.search.PermissionSearchRepository;
import ch.united.fastadmin.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    private final PermissionSearchRepository permissionSearchRepository;

    public PermissionServiceImpl(
        PermissionRepository permissionRepository,
        PermissionMapper permissionMapper,
        PermissionSearchRepository permissionSearchRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.permissionSearchRepository = permissionSearchRepository;
    }

    @Override
    public PermissionDTO save(PermissionDTO permissionDTO) {
        log.debug("Request to save Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        PermissionDTO result = permissionMapper.toDto(permission);
        permissionSearchRepository.save(permission);
        return result;
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> findAll() {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll().stream().map(permissionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id).map(permissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
        permissionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> search(String query) {
        log.debug("Request to search Permissions for query {}", query);
        return StreamSupport
            .stream(permissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(permissionMapper::toDto)
            .collect(Collectors.toList());
    }
}
