package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.CustomField;
import ch.united.fastadmin.repository.CustomFieldRepository;
import ch.united.fastadmin.repository.search.CustomFieldSearchRepository;
import ch.united.fastadmin.service.criteria.CustomFieldCriteria;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import ch.united.fastadmin.service.mapper.CustomFieldMapper;
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
 * Service for executing complex queries for {@link CustomField} entities in the database.
 * The main input is a {@link CustomFieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomFieldDTO} or a {@link Page} of {@link CustomFieldDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomFieldQueryService extends QueryService<CustomField> {

    private final Logger log = LoggerFactory.getLogger(CustomFieldQueryService.class);

    private final CustomFieldRepository customFieldRepository;

    private final CustomFieldMapper customFieldMapper;

    private final CustomFieldSearchRepository customFieldSearchRepository;

    public CustomFieldQueryService(
        CustomFieldRepository customFieldRepository,
        CustomFieldMapper customFieldMapper,
        CustomFieldSearchRepository customFieldSearchRepository
    ) {
        this.customFieldRepository = customFieldRepository;
        this.customFieldMapper = customFieldMapper;
        this.customFieldSearchRepository = customFieldSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomFieldDTO> findByCriteria(CustomFieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomField> specification = createSpecification(criteria);
        return customFieldMapper.toDto(customFieldRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomFieldDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomFieldDTO> findByCriteria(CustomFieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomField> specification = createSpecification(criteria);
        return customFieldRepository.findAll(specification, page).map(customFieldMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomFieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomField> specification = createSpecification(criteria);
        return customFieldRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomFieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomField> createSpecification(CustomFieldCriteria criteria) {
        Specification<CustomField> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomField_.id));
            }
            if (criteria.getDomainArea() != null) {
                specification = specification.and(buildSpecification(criteria.getDomainArea(), CustomField_.domainArea));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), CustomField_.key));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CustomField_.name));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), CustomField_.value));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContactId(), root -> root.join(CustomField_.contact, JoinType.LEFT).get(Contact_.id))
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(CustomField_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
