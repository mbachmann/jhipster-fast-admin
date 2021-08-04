package ch.united.fastadmin.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ch.united.fastadmin.domain.Role;
import ch.united.fastadmin.repository.RoleRepository;
import ch.united.fastadmin.repository.search.RoleSearchRepository;
import ch.united.fastadmin.service.RoleService;
import ch.united.fastadmin.service.dto.RoleDTO;
import ch.united.fastadmin.service.mapper.RoleMapper;
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
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final RoleSearchRepository roleSearchRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, RoleSearchRepository roleSearchRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleSearchRepository = roleSearchRepository;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        RoleDTO result = roleMapper.toDto(role);
        roleSearchRepository.save(role);
        return result;
    }

    @Override
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getId())
            .map(
                existingRole -> {
                    roleMapper.partialUpdate(existingRole, roleDTO);

                    return existingRole;
                }
            )
            .map(roleRepository::save)
            .map(
                savedRole -> {
                    roleSearchRepository.save(savedRole);

                    return savedRole;
                }
            )
            .map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        log.debug("Request to get all Roles");
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findById(id).map(roleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
        roleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> search(String query) {
        log.debug("Request to search Roles for query {}", query);
        return StreamSupport
            .stream(roleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }
}
