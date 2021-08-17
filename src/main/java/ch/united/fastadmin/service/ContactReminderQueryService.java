package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.ContactReminder;
import ch.united.fastadmin.repository.ContactReminderRepository;
import ch.united.fastadmin.service.criteria.ContactReminderCriteria;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import ch.united.fastadmin.service.mapper.ContactReminderMapper;
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
 * Service for executing complex queries for {@link ContactReminder} entities in the database.
 * The main input is a {@link ContactReminderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactReminderDTO} or a {@link Page} of {@link ContactReminderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactReminderQueryService extends QueryService<ContactReminder> {

    private final Logger log = LoggerFactory.getLogger(ContactReminderQueryService.class);

    private final ContactReminderRepository contactReminderRepository;

    private final ContactReminderMapper contactReminderMapper;

    public ContactReminderQueryService(ContactReminderRepository contactReminderRepository, ContactReminderMapper contactReminderMapper) {
        this.contactReminderRepository = contactReminderRepository;
        this.contactReminderMapper = contactReminderMapper;
    }

    /**
     * Return a {@link List} of {@link ContactReminderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactReminderDTO> findByCriteria(ContactReminderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactReminder> specification = createSpecification(criteria);
        return contactReminderMapper.toDto(contactReminderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactReminderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactReminderDTO> findByCriteria(ContactReminderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactReminder> specification = createSpecification(criteria);
        return contactReminderRepository.findAll(specification, page).map(contactReminderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactReminderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactReminder> specification = createSpecification(criteria);
        return contactReminderRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactReminderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactReminder> createSpecification(ContactReminderCriteria criteria) {
        Specification<ContactReminder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactReminder_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), ContactReminder_.remoteId));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContactId(), ContactReminder_.contactId));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), ContactReminder_.contactName));
            }
            if (criteria.getDateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTime(), ContactReminder_.dateTime));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ContactReminder_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContactReminder_.description));
            }
            if (criteria.getIntervalValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntervalValue(), ContactReminder_.intervalValue));
            }
            if (criteria.getIntervalType() != null) {
                specification = specification.and(buildSpecification(criteria.getIntervalType(), ContactReminder_.intervalType));
            }
            if (criteria.getInactiv() != null) {
                specification = specification.and(buildSpecification(criteria.getInactiv(), ContactReminder_.inactiv));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(ContactReminder_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
