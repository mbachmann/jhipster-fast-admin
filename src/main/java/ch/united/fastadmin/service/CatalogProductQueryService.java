package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.CatalogProduct;
import ch.united.fastadmin.repository.CatalogProductRepository;
import ch.united.fastadmin.service.criteria.CatalogProductCriteria;
import ch.united.fastadmin.service.dto.CatalogProductDTO;
import ch.united.fastadmin.service.mapper.CatalogProductMapper;
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
 * Service for executing complex queries for {@link CatalogProduct} entities in the database.
 * The main input is a {@link CatalogProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatalogProductDTO} or a {@link Page} of {@link CatalogProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogProductQueryService extends QueryService<CatalogProduct> {

    private final Logger log = LoggerFactory.getLogger(CatalogProductQueryService.class);

    private final CatalogProductRepository catalogProductRepository;

    private final CatalogProductMapper catalogProductMapper;

    public CatalogProductQueryService(CatalogProductRepository catalogProductRepository, CatalogProductMapper catalogProductMapper) {
        this.catalogProductRepository = catalogProductRepository;
        this.catalogProductMapper = catalogProductMapper;
    }

    /**
     * Return a {@link List} of {@link CatalogProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatalogProductDTO> findByCriteria(CatalogProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CatalogProduct> specification = createSpecification(criteria);
        return catalogProductMapper.toDto(catalogProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CatalogProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogProductDTO> findByCriteria(CatalogProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CatalogProduct> specification = createSpecification(criteria);
        return catalogProductRepository.findAll(specification, page).map(catalogProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CatalogProduct> specification = createSpecification(criteria);
        return catalogProductRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CatalogProduct> createSpecification(CatalogProductCriteria criteria) {
        Specification<CatalogProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CatalogProduct_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), CatalogProduct_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), CatalogProduct_.number));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CatalogProduct_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CatalogProduct_.description));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), CatalogProduct_.notes));
            }
            if (criteria.getCategoryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryName(), CatalogProduct_.categoryName));
            }
            if (criteria.getIncludingVat() != null) {
                specification = specification.and(buildSpecification(criteria.getIncludingVat(), CatalogProduct_.includingVat));
            }
            if (criteria.getVat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVat(), CatalogProduct_.vat));
            }
            if (criteria.getUnitName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitName(), CatalogProduct_.unitName));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), CatalogProduct_.price));
            }
            if (criteria.getPricePurchase() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPricePurchase(), CatalogProduct_.pricePurchase));
            }
            if (criteria.getInventoryAvailable() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getInventoryAvailable(), CatalogProduct_.inventoryAvailable));
            }
            if (criteria.getInventoryReserved() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getInventoryReserved(), CatalogProduct_.inventoryReserved));
            }
            if (criteria.getDefaultAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultAmount(), CatalogProduct_.defaultAmount));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), CatalogProduct_.created));
            }
            if (criteria.getInactiv() != null) {
                specification = specification.and(buildSpecification(criteria.getInactiv(), CatalogProduct_.inactiv));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(CatalogProduct_.customFields, JoinType.LEFT).get(CustomFieldValue_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(CatalogProduct_.category, JoinType.LEFT).get(CatalogCategory_.id)
                        )
                    );
            }
            if (criteria.getUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUnitId(),
                            root -> root.join(CatalogProduct_.unit, JoinType.LEFT).get(CatalogUnit_.id)
                        )
                    );
            }
            if (criteria.getVatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVatId(), root -> root.join(CatalogProduct_.vat, JoinType.LEFT).get(Vat_.id))
                    );
            }
        }
        return specification;
    }
}
