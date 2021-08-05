package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.ContactAddress;
import ch.united.fastadmin.repository.ContactAddressRepository;
import ch.united.fastadmin.repository.search.ContactAddressSearchRepository;
import ch.united.fastadmin.service.criteria.ContactAddressCriteria;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import ch.united.fastadmin.service.mapper.ContactAddressMapper;
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
 * Service for executing complex queries for {@link ContactAddress} entities in the database.
 * The main input is a {@link ContactAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactAddressDTO} or a {@link Page} of {@link ContactAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactAddressQueryService extends QueryService<ContactAddress> {

    private final Logger log = LoggerFactory.getLogger(ContactAddressQueryService.class);

    private final ContactAddressRepository contactAddressRepository;

    private final ContactAddressMapper contactAddressMapper;

    private final ContactAddressSearchRepository contactAddressSearchRepository;

    public ContactAddressQueryService(
        ContactAddressRepository contactAddressRepository,
        ContactAddressMapper contactAddressMapper,
        ContactAddressSearchRepository contactAddressSearchRepository
    ) {
        this.contactAddressRepository = contactAddressRepository;
        this.contactAddressMapper = contactAddressMapper;
        this.contactAddressSearchRepository = contactAddressSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContactAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactAddressDTO> findByCriteria(ContactAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactAddress> specification = createSpecification(criteria);
        return contactAddressMapper.toDto(contactAddressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactAddressDTO> findByCriteria(ContactAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactAddress> specification = createSpecification(criteria);
        return contactAddressRepository.findAll(specification, page).map(contactAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContactAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContactAddress> specification = createSpecification(criteria);
        return contactAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link ContactAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContactAddress> createSpecification(ContactAddressCriteria criteria) {
        Specification<ContactAddress> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContactAddress_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), ContactAddress_.remoteId));
            }
            if (criteria.getDefaultAddress() != null) {
                specification = specification.and(buildSpecification(criteria.getDefaultAddress(), ContactAddress_.defaultAddress));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), ContactAddress_.country));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), ContactAddress_.street));
            }
            if (criteria.getStreetNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetNo(), ContactAddress_.streetNo));
            }
            if (criteria.getStreet2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet2(), ContactAddress_.street2));
            }
            if (criteria.getPostcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostcode(), ContactAddress_.postcode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), ContactAddress_.city));
            }
            if (criteria.getHideOnDocuments() != null) {
                specification = specification.and(buildSpecification(criteria.getHideOnDocuments(), ContactAddress_.hideOnDocuments));
            }
            if (criteria.getDefaultPrepage() != null) {
                specification = specification.and(buildSpecification(criteria.getDefaultPrepage(), ContactAddress_.defaultPrepage));
            }
        }
        return specification;
    }
}
