package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.OrderConfirmation;
import ch.united.fastadmin.repository.OrderConfirmationRepository;
import ch.united.fastadmin.service.criteria.OrderConfirmationCriteria;
import ch.united.fastadmin.service.dto.OrderConfirmationDTO;
import ch.united.fastadmin.service.mapper.OrderConfirmationMapper;
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
 * Service for executing complex queries for {@link OrderConfirmation} entities in the database.
 * The main input is a {@link OrderConfirmationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderConfirmationDTO} or a {@link Page} of {@link OrderConfirmationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderConfirmationQueryService extends QueryService<OrderConfirmation> {

    private final Logger log = LoggerFactory.getLogger(OrderConfirmationQueryService.class);

    private final OrderConfirmationRepository orderConfirmationRepository;

    private final OrderConfirmationMapper orderConfirmationMapper;

    public OrderConfirmationQueryService(
        OrderConfirmationRepository orderConfirmationRepository,
        OrderConfirmationMapper orderConfirmationMapper
    ) {
        this.orderConfirmationRepository = orderConfirmationRepository;
        this.orderConfirmationMapper = orderConfirmationMapper;
    }

    /**
     * Return a {@link List} of {@link OrderConfirmationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderConfirmationDTO> findByCriteria(OrderConfirmationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderConfirmation> specification = createSpecification(criteria);
        return orderConfirmationMapper.toDto(orderConfirmationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderConfirmationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderConfirmationDTO> findByCriteria(OrderConfirmationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderConfirmation> specification = createSpecification(criteria);
        return orderConfirmationRepository.findAll(specification, page).map(orderConfirmationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderConfirmationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderConfirmation> specification = createSpecification(criteria);
        return orderConfirmationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderConfirmationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderConfirmation> createSpecification(OrderConfirmationCriteria criteria) {
        Specification<OrderConfirmation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderConfirmation_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), OrderConfirmation_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), OrderConfirmation_.number));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), OrderConfirmation_.contactName));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), OrderConfirmation_.date));
            }
            if (criteria.getPeriodText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodText(), OrderConfirmation_.periodText));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), OrderConfirmation_.currency));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), OrderConfirmation_.total));
            }
            if (criteria.getVatIncluded() != null) {
                specification = specification.and(buildSpecification(criteria.getVatIncluded(), OrderConfirmation_.vatIncluded));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), OrderConfirmation_.discountRate));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), OrderConfirmation_.discountType));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), OrderConfirmation_.language));
            }
            if (criteria.getPageAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageAmount(), OrderConfirmation_.pageAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), OrderConfirmation_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderConfirmation_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), OrderConfirmation_.created));
            }
            if (criteria.getFreeTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFreeTextsId(),
                            root -> root.join(OrderConfirmation_.freeTexts, JoinType.LEFT).get(DocumentFreeText_.id)
                        )
                    );
            }
            if (criteria.getTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTextsId(),
                            root -> root.join(OrderConfirmation_.texts, JoinType.LEFT).get(DescriptiveDocumentText_.id)
                        )
                    );
            }
            if (criteria.getPositionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPositionsId(),
                            root -> root.join(OrderConfirmation_.positions, JoinType.LEFT).get(DocumentPosition_.id)
                        )
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(OrderConfirmation_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
            if (criteria.getContactAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactAddressId(),
                            root -> root.join(OrderConfirmation_.contactAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(OrderConfirmation_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
            if (criteria.getContactPrePageAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPrePageAddressId(),
                            root -> root.join(OrderConfirmation_.contactPrePageAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayoutId(),
                            root -> root.join(OrderConfirmation_.layout, JoinType.LEFT).get(Layout_.id)
                        )
                    );
            }
            if (criteria.getSignatureId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatureId(),
                            root -> root.join(OrderConfirmation_.signature, JoinType.LEFT).get(Signature_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
