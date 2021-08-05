package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.ContactGroup;
import ch.united.fastadmin.repository.ContactGroupRepository;
import ch.united.fastadmin.repository.search.ContactGroupSearchRepository;
import ch.united.fastadmin.service.criteria.ContactGroupCriteria;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import ch.united.fastadmin.service.mapper.ContactGroupMapper;
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
 * Service for executing complex queries for {@link ContactGroup} entities in the database.
 * The main input is a {@link ContactGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactGroupDTO} or a {@link Page} of {@link ContactGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactGroupQueryService extends QueryService<ContactGroup> {

    private final Logger log = LoggerFactory.getLogger(ContactGroupQueryService.class);

    private final ContactGroupRepository contactGroupRepository;

    private final ContactGroupMapper contactGroupMapper;

    private final ContactGroupSearchRepository contactGroupSearchRepository;

    public ContactGroupQueryService(
        ContactGroupRepository contactGroupRepository,
        ContactGroupMapper contactGroupMapper,
        ContactGroupSearchRepository contactGroupSearchRepository
    ) {
        this.contactGroupRepository = contactGroupRepository;
        this.contactGroupMapper = contactGroupMapper;
        this.contactGroupSearchRepository = contactGroupSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactGroupDTO> findByCriteria(ContactGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactGroup> specification = createSpecification(criteria);
        return contactGroupMapper.toDto(contactGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactGroupDTO> findByCriteria(ContactGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactGroup> specification = createSpecification(criteria);
        return contactGroupRepository.findAll(specification, page).map(contactGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactGroup> specification = createSpecification(criteria);
        return contactGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactGroup> createSpecification(ContactGroupCriteria criteria) {
        Specification<ContactGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContactGroup_.name));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(ContactGroup_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
