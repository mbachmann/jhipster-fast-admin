package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.ApplicationRole;
import ch.united.fastadmin.repository.ApplicationRoleRepository;
import ch.united.fastadmin.service.dto.ApplicationRoleDTO;
import ch.united.fastadmin.service.mapper.ApplicationRoleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationRole}.
 */
@Service
@Transactional
public class ApplicationRoleService {

    private final Logger log = LoggerFactory.getLogger(ApplicationRoleService.class);

    private final ApplicationRoleRepository applicationRoleRepository;

    private final ApplicationRoleMapper applicationRoleMapper;

    public ApplicationRoleService(ApplicationRoleRepository applicationRoleRepository, ApplicationRoleMapper applicationRoleMapper) {
        this.applicationRoleRepository = applicationRoleRepository;
        this.applicationRoleMapper = applicationRoleMapper;
    }

    /**
     * Save a applicationRole.
     *
     * @param applicationRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicationRoleDTO save(ApplicationRoleDTO applicationRoleDTO) {
        log.debug("Request to save ApplicationRole : {}", applicationRoleDTO);
        ApplicationRole applicationRole = applicationRoleMapper.toEntity(applicationRoleDTO);
        applicationRole = applicationRoleRepository.save(applicationRole);
        return applicationRoleMapper.toDto(applicationRole);
    }

    /**
     * Partially update a applicationRole.
     *
     * @param applicationRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationRoleDTO> partialUpdate(ApplicationRoleDTO applicationRoleDTO) {
        log.debug("Request to partially update ApplicationRole : {}", applicationRoleDTO);

        return applicationRoleRepository
            .findById(applicationRoleDTO.getId())
            .map(
                existingApplicationRole -> {
                    applicationRoleMapper.partialUpdate(existingApplicationRole, applicationRoleDTO);

                    return existingApplicationRole;
                }
            )
            .map(applicationRoleRepository::save)
            .map(applicationRoleMapper::toDto);
    }

    /**
     * Get all the applicationRoles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationRoleDTO> findAll() {
        log.debug("Request to get all ApplicationRoles");
        return applicationRoleRepository
            .findAll()
            .stream()
            .map(applicationRoleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationRoleDTO> findOne(Long id) {
        log.debug("Request to get ApplicationRole : {}", id);
        return applicationRoleRepository.findById(id).map(applicationRoleMapper::toDto);
    }

    /**
     * Delete the applicationRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationRole : {}", id);
        applicationRoleRepository.deleteById(id);
    }
}
