package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.Permission;
import ch.united.fastadmin.repository.PermissionRepository;
import ch.united.fastadmin.repository.search.PermissionSearchRepository;
import ch.united.fastadmin.service.criteria.PermissionCriteria;
import ch.united.fastadmin.service.dto.PermissionDTO;
import ch.united.fastadmin.service.mapper.PermissionMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Permission} entities in the database.
 * The main input is a {@link PermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PermissionDTO} or a {@link Page} of {@link PermissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionQueryService extends QueryService<Permission> {

    private final Logger log = LoggerFactory.getLogger(PermissionQueryService.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    private final PermissionSearchRepository permissionSearchRepository;

    public PermissionQueryService(
        PermissionRepository permissionRepository,
        PermissionMapper permissionMapper,
        PermissionSearchRepository permissionSearchRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.permissionSearchRepository = permissionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findByCriteria(PermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionMapper.toDto(permissionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionDTO> findByCriteria(PermissionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionRepository.findAll(specification, page).map(permissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Permission> specification = createSpecification(criteria);
        return permissionRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Permission> createSpecification(PermissionCriteria criteria) {
        Specification<Permission> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Permission_.id));
            }
            if (criteria.getAdd() != null) {
                specification = specification.and(buildSpecification(criteria.getAdd(), Permission_.add));
            }
            if (criteria.getEdit() != null) {
                specification = specification.and(buildSpecification(criteria.getEdit(), Permission_.edit));
            }
            if (criteria.getManage() != null) {
                specification = specification.and(buildSpecification(criteria.getManage(), Permission_.manage));
            }
            if (criteria.getDomainArea() != null) {
                specification = specification.and(buildSpecification(criteria.getDomainArea(), Permission_.domainArea));
            }
            if (criteria.getRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoleId(), root -> root.join(Permission_.role, JoinType.LEFT).get(Role_.id))
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContactId(), root -> root.join(Permission_.contact, JoinType.LEFT).get(Contact_.id))
                    );
            }
        }
        return specification;
    }
}
