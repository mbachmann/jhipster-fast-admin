package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.CatalogService;
import ch.united.fastadmin.repository.CatalogServiceRepository;
import ch.united.fastadmin.service.criteria.CatalogServiceCriteria;
import ch.united.fastadmin.service.dto.CatalogServiceDTO;
import ch.united.fastadmin.service.mapper.CatalogServiceMapper;
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
 * Service for executing complex queries for {@link CatalogService} entities in the database.
 * The main input is a {@link CatalogServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatalogServiceDTO} or a {@link Page} of {@link CatalogServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogServiceQueryService extends QueryService<CatalogService> {

    private final Logger log = LoggerFactory.getLogger(CatalogServiceQueryService.class);

    private final CatalogServiceRepository catalogServiceRepository;

    private final CatalogServiceMapper catalogServiceMapper;

    public CatalogServiceQueryService(CatalogServiceRepository catalogServiceRepository, CatalogServiceMapper catalogServiceMapper) {
        this.catalogServiceRepository = catalogServiceRepository;
        this.catalogServiceMapper = catalogServiceMapper;
    }

    /**
     * Return a {@link List} of {@link CatalogServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogServiceDTO> findByCriteria(CatalogServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CatalogService> specification = createSpecification(criteria);
        return catalogServiceMapper.toDto(catalogServiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CatalogServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogServiceDTO> findByCriteria(CatalogServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CatalogService> specification = createSpecification(criteria);
        return catalogServiceRepository.findAll(specification, page).map(catalogServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CatalogService> specification = createSpecification(criteria);
        return catalogServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CatalogService> createSpecification(CatalogServiceCriteria criteria) {
        Specification<CatalogService> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CatalogService_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), CatalogService_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), CatalogService_.number));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CatalogService_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CatalogService_.description));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), CatalogService_.notes));
            }
            if (criteria.getCategoryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryName(), CatalogService_.categoryName));
            }
            if (criteria.getIncludingVat() != null) {
                specification = specification.and(buildSpecification(criteria.getIncludingVat(), CatalogService_.includingVat));
            }
            if (criteria.getVat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVat(), CatalogService_.vat));
            }
            if (criteria.getUnitName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitName(), CatalogService_.unitName));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), CatalogService_.price));
            }
            if (criteria.getDefaultAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultAmount(), CatalogService_.defaultAmount));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), CatalogService_.created));
            }
            if (criteria.getInactiv() != null) {
                specification = specification.and(buildSpecification(criteria.getInactiv(), CatalogService_.inactiv));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(CatalogService_.customFields, JoinType.LEFT).get(CustomFieldValue_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(CatalogService_.category, JoinType.LEFT).get(CatalogCategory_.id)
                        )
                    );
            }
            if (criteria.getUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUnitId(),
                            root -> root.join(CatalogService_.unit, JoinType.LEFT).get(CatalogUnit_.id)
                        )
                    );
            }
            if (criteria.getVatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVatId(), root -> root.join(CatalogService_.vat, JoinType.LEFT).get(Vat_.id))
                    );
            }
        }
        return specification;
    }
}
