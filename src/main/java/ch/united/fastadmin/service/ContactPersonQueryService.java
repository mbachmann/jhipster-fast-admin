package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.ContactPerson;
import ch.united.fastadmin.repository.ContactPersonRepository;
import ch.united.fastadmin.repository.search.ContactPersonSearchRepository;
import ch.united.fastadmin.service.criteria.ContactPersonCriteria;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import ch.united.fastadmin.service.mapper.ContactPersonMapper;
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
 * Service for executing complex queries for {@link ContactPerson} entities in the database.
 * The main input is a {@link ContactPersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactPersonDTO} or a {@link Page} of {@link ContactPersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactPersonQueryService extends QueryService<ContactPerson> {

    private final Logger log = LoggerFactory.getLogger(ContactPersonQueryService.class);

    private final ContactPersonRepository contactPersonRepository;

    private final ContactPersonMapper contactPersonMapper;

    private final ContactPersonSearchRepository contactPersonSearchRepository;

    public ContactPersonQueryService(
        ContactPersonRepository contactPersonRepository,
        ContactPersonMapper contactPersonMapper,
        ContactPersonSearchRepository contactPersonSearchRepository
    ) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
        this.contactPersonSearchRepository = contactPersonSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactPersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactPersonDTO> findByCriteria(ContactPersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactPerson> specification = createSpecification(criteria);
        return contactPersonMapper.toDto(contactPersonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactPersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactPersonDTO> findByCriteria(ContactPersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactPerson> specification = createSpecification(criteria);
        return contactPersonRepository.findAll(specification, page).map(contactPersonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactPersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactPerson> specification = createSpecification(criteria);
        return contactPersonRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactPersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactPerson> createSpecification(ContactPersonCriteria criteria) {
        Specification<ContactPerson> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactPerson_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), ContactPerson_.remoteId));
            }
            if (criteria.getDefaultPerson() != null) {
                specification = specification.and(buildSpecification(criteria.getDefaultPerson(), ContactPerson_.defaultPerson));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContactPerson_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), ContactPerson_.surname));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), ContactPerson_.gender));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), ContactPerson_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), ContactPerson_.phone));
            }
            if (criteria.getDepartment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartment(), ContactPerson_.department));
            }
            if (criteria.getSalutation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSalutation(), ContactPerson_.salutation));
            }
            if (criteria.getShowTitle() != null) {
                specification = specification.and(buildSpecification(criteria.getShowTitle(), ContactPerson_.showTitle));
            }
            if (criteria.getShowDepartment() != null) {
                specification = specification.and(buildSpecification(criteria.getShowDepartment(), ContactPerson_.showDepartment));
            }
            if (criteria.getWantsNewsletter() != null) {
                specification = specification.and(buildSpecification(criteria.getWantsNewsletter(), ContactPerson_.wantsNewsletter));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(ContactPerson_.customFields, JoinType.LEFT).get(CustomField_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
