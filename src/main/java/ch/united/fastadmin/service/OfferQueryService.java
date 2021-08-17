package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.Offer;
import ch.united.fastadmin.repository.OfferRepository;
import ch.united.fastadmin.service.criteria.OfferCriteria;
import ch.united.fastadmin.service.dto.OfferDTO;
import ch.united.fastadmin.service.mapper.OfferMapper;
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
 * Service for executing complex queries for {@link Offer} entities in the database.
 * The main input is a {@link OfferCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OfferDTO} or a {@link Page} of {@link OfferDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OfferQueryService extends QueryService<Offer> {

    private final Logger log = LoggerFactory.getLogger(OfferQueryService.class);

    private final OfferRepository offerRepository;

    private final OfferMapper offerMapper;

    public OfferQueryService(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    /**
     * Return a {@link List} of {@link OfferDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OfferDTO> findByCriteria(OfferCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Offer> specification = createSpecification(criteria);
        return offerMapper.toDto(offerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OfferDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OfferDTO> findByCriteria(OfferCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Offer> specification = createSpecification(criteria);
        return offerRepository.findAll(specification, page).map(offerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OfferCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Offer> specification = createSpecification(criteria);
        return offerRepository.count(specification);
    }

    /**
     * Function to convert {@link OfferCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Offer> createSpecification(OfferCriteria criteria) {
        Specification<Offer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Offer_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), Offer_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Offer_.number));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), Offer_.contactName));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Offer_.date));
            }
            if (criteria.getValidUntil() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidUntil(), Offer_.validUntil));
            }
            if (criteria.getPeriodText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodText(), Offer_.periodText));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), Offer_.currency));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Offer_.total));
            }
            if (criteria.getVatIncluded() != null) {
                specification = specification.and(buildSpecification(criteria.getVatIncluded(), Offer_.vatIncluded));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), Offer_.discountRate));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), Offer_.discountType));
            }
            if (criteria.getAcceptOnline() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceptOnline(), Offer_.acceptOnline));
            }
            if (criteria.getAcceptOnlineUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAcceptOnlineUrl(), Offer_.acceptOnlineUrl));
            }
            if (criteria.getAcceptOnlineStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceptOnlineStatus(), Offer_.acceptOnlineStatus));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Offer_.language));
            }
            if (criteria.getPageAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageAmount(), Offer_.pageAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Offer_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Offer_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Offer_.created));
            }
            if (criteria.getFreeTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFreeTextsId(),
                            root -> root.join(Offer_.freeTexts, JoinType.LEFT).get(DocumentFreeText_.id)
                        )
                    );
            }
            if (criteria.getTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTextsId(),
                            root -> root.join(Offer_.texts, JoinType.LEFT).get(DescriptiveDocumentText_.id)
                        )
                    );
            }
            if (criteria.getPositionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPositionsId(),
                            root -> root.join(Offer_.positions, JoinType.LEFT).get(DocumentPosition_.id)
                        )
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContactId(), root -> root.join(Offer_.contact, JoinType.LEFT).get(Contact_.id))
                    );
            }
            if (criteria.getContactAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactAddressId(),
                            root -> root.join(Offer_.contactAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(Offer_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
            if (criteria.getContactPrePageAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPrePageAddressId(),
                            root -> root.join(Offer_.contactPrePageAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayoutId(), root -> root.join(Offer_.layout, JoinType.LEFT).get(Layout_.id))
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayoutId(), root -> root.join(Offer_.layout, JoinType.LEFT).get(Signature_.id))
                    );
            }
        }
        return specification;
    }
}
