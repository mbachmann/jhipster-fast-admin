package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.Owner;
import ch.united.fastadmin.repository.OwnerRepository;
import ch.united.fastadmin.repository.search.OwnerSearchRepository;
import ch.united.fastadmin.service.criteria.OwnerCriteria;
import ch.united.fastadmin.service.dto.OwnerDTO;
import ch.united.fastadmin.service.mapper.OwnerMapper;
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
 * Service for executing complex queries for {@link Owner} entities in the database.
 * The main input is a {@link OwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OwnerDTO} or a {@link Page} of {@link OwnerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OwnerQueryService extends QueryService<Owner> {

    private final Logger log = LoggerFactory.getLogger(OwnerQueryService.class);

    private final OwnerRepository ownerRepository;

    private final OwnerMapper ownerMapper;

    private final OwnerSearchRepository ownerSearchRepository;

    public OwnerQueryService(OwnerRepository ownerRepository, OwnerMapper ownerMapper, OwnerSearchRepository ownerSearchRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.ownerSearchRepository = ownerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OwnerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OwnerDTO> findByCriteria(OwnerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerMapper.toDto(ownerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OwnerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OwnerDTO> findByCriteria(OwnerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.findAll(specification, page).map(ownerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OwnerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.count(specification);
    }

    /**
     * Function to convert {@link OwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Owner> createSpecification(OwnerCriteria criteria) {
        Specification<Owner> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Owner_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), Owner_.remoteId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Owner_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Owner_.surname));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Owner_.email));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Owner_.language));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Owner_.companyName));
            }
            if (criteria.getCompanyAddition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyAddition(), Owner_.companyAddition));
            }
            if (criteria.getCompanyCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyCountry(), Owner_.companyCountry));
            }
            if (criteria.getCompanyStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyStreet(), Owner_.companyStreet));
            }
            if (criteria.getCompanyStreetNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyStreetNo(), Owner_.companyStreetNo));
            }
            if (criteria.getCompanyStreet2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyStreet2(), Owner_.companyStreet2));
            }
            if (criteria.getCompanyPostcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyPostcode(), Owner_.companyPostcode));
            }
            if (criteria.getCompanyCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyCity(), Owner_.companyCity));
            }
            if (criteria.getCompanyPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyPhone(), Owner_.companyPhone));
            }
            if (criteria.getCompanyFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyFax(), Owner_.companyFax));
            }
            if (criteria.getCompanyEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyEmail(), Owner_.companyEmail));
            }
            if (criteria.getCompanyWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyWebsite(), Owner_.companyWebsite));
            }
            if (criteria.getCompanyVatId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyVatId(), Owner_.companyVatId));
            }
            if (criteria.getCompanyCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyCurrency(), Owner_.companyCurrency));
            }
        }
        return specification;
    }
}
