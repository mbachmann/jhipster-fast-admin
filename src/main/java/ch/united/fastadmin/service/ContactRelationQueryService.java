package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.ContactRelation;
import ch.united.fastadmin.repository.ContactRelationRepository;
import ch.united.fastadmin.repository.search.ContactRelationSearchRepository;
import ch.united.fastadmin.service.criteria.ContactRelationCriteria;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import ch.united.fastadmin.service.mapper.ContactRelationMapper;
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
 * Service for executing complex queries for {@link ContactRelation} entities in the database.
 * The main input is a {@link ContactRelationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactRelationDTO} or a {@link Page} of {@link ContactRelationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactRelationQueryService extends QueryService<ContactRelation> {

    private final Logger log = LoggerFactory.getLogger(ContactRelationQueryService.class);

    private final ContactRelationRepository contactRelationRepository;

    private final ContactRelationMapper contactRelationMapper;

    private final ContactRelationSearchRepository contactRelationSearchRepository;

    public ContactRelationQueryService(
        ContactRelationRepository contactRelationRepository,
        ContactRelationMapper contactRelationMapper,
        ContactRelationSearchRepository contactRelationSearchRepository
    ) {
        this.contactRelationRepository = contactRelationRepository;
        this.contactRelationMapper = contactRelationMapper;
        this.contactRelationSearchRepository = contactRelationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactRelationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactRelationDTO> findByCriteria(ContactRelationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactRelation> specification = createSpecification(criteria);
        return contactRelationMapper.toDto(contactRelationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactRelationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactRelationDTO> findByCriteria(ContactRelationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactRelation> specification = createSpecification(criteria);
        return contactRelationRepository.findAll(specification, page).map(contactRelationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactRelationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactRelation> specification = createSpecification(criteria);
        return contactRelationRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactRelationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactRelation> createSpecification(ContactRelationCriteria criteria) {
        Specification<ContactRelation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactRelation_.id));
            }
            if (criteria.getContactRelationType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getContactRelationType(), ContactRelation_.contactRelationType));
            }
            if (criteria.getContactsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactsId(),
                            root -> root.join(ContactRelation_.contacts, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
