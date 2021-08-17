package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.Contact;
import ch.united.fastadmin.repository.ContactRepository;
import ch.united.fastadmin.service.criteria.ContactCriteria;
import ch.united.fastadmin.service.dto.ContactDTO;
import ch.united.fastadmin.service.mapper.ContactMapper;
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
 * Service for executing complex queries for {@link Contact} entities in the database.
 * The main input is a {@link ContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactDTO} or a {@link Page} of {@link ContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactQueryService extends QueryService<Contact> {

    private final Logger log = LoggerFactory.getLogger(ContactQueryService.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactQueryService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Return a {@link List} of {@link ContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> findByCriteria(ContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contact> specification = createSpecification(criteria);
        return contactMapper.toDto(contactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contact> specification = createSpecification(criteria);
        return contactRepository.findAll(specification, page).map(contactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contact> specification = createSpecification(criteria);
        return contactRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contact> createSpecification(ContactCriteria criteria) {
        Specification<Contact> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Contact_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), Contact_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Contact_.number));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Contact_.type));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Contact_.gender));
            }
            if (criteria.getGenderSalutationActive() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getGenderSalutationActive(), Contact_.genderSalutationActive));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Contact_.name));
            }
            if (criteria.getNameAddition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameAddition(), Contact_.nameAddition));
            }
            if (criteria.getSalutation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSalutation(), Contact_.salutation));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Contact_.phone));
            }
            if (criteria.getFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFax(), Contact_.fax));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Contact_.email));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Contact_.website));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Contact_.notes));
            }
            if (criteria.getCommunicationLanguage() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCommunicationLanguage(), Contact_.communicationLanguage));
            }
            if (criteria.getCommunicationChannel() != null) {
                specification = specification.and(buildSpecification(criteria.getCommunicationChannel(), Contact_.communicationChannel));
            }
            if (criteria.getCommunicationNewsletter() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCommunicationNewsletter(), Contact_.communicationNewsletter));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), Contact_.currency));
            }
            if (criteria.getEbillAccountId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEbillAccountId(), Contact_.ebillAccountId));
            }
            if (criteria.getVatIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVatIdentification(), Contact_.vatIdentification));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), Contact_.vatRate));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), Contact_.discountRate));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), Contact_.discountType));
            }
            if (criteria.getPaymentGrace() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentGrace(), Contact_.paymentGrace));
            }
            if (criteria.getHourlyRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHourlyRate(), Contact_.hourlyRate));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Contact_.created));
            }
            if (criteria.getMainAddressId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMainAddressId(), Contact_.mainAddressId));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Contact_.birthDate));
            }
            if (criteria.getBirthPlace() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBirthPlace(), Contact_.birthPlace));
            }
            if (criteria.getPlaceOfOrigin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaceOfOrigin(), Contact_.placeOfOrigin));
            }
            if (criteria.getCitizenCountry1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCitizenCountry1(), Contact_.citizenCountry1));
            }
            if (criteria.getCitizenCountry2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCitizenCountry2(), Contact_.citizenCountry2));
            }
            if (criteria.getSocialSecurityNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSocialSecurityNumber(), Contact_.socialSecurityNumber));
            }
            if (criteria.getHobbies() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHobbies(), Contact_.hobbies));
            }
            if (criteria.getDailyWork() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDailyWork(), Contact_.dailyWork));
            }
            if (criteria.getContactAttribute01() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactAttribute01(), Contact_.contactAttribute01));
            }
            if (criteria.getImageType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageType(), Contact_.imageType));
            }
            if (criteria.getInactiv() != null) {
                specification = specification.and(buildSpecification(criteria.getInactiv(), Contact_.inactiv));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(Contact_.customFields, JoinType.LEFT).get(CustomFieldValue_.id)
                        )
                    );
            }
            if (criteria.getRelationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRelationsId(),
                            root -> root.join(Contact_.relations, JoinType.LEFT).get(ContactRelation_.id)
                        )
                    );
            }
            if (criteria.getGroupsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGroupsId(), root -> root.join(Contact_.groups, JoinType.LEFT).get(ContactGroup_.id))
                    );
            }
        }
        return specification;
    }
}
