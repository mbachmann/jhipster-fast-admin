package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.DeliveryNote;
import ch.united.fastadmin.repository.DeliveryNoteRepository;
import ch.united.fastadmin.service.criteria.DeliveryNoteCriteria;
import ch.united.fastadmin.service.dto.DeliveryNoteDTO;
import ch.united.fastadmin.service.mapper.DeliveryNoteMapper;
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
 * Service for executing complex queries for {@link DeliveryNote} entities in the database.
 * The main input is a {@link DeliveryNoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeliveryNoteDTO} or a {@link Page} of {@link DeliveryNoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeliveryNoteQueryService extends QueryService<DeliveryNote> {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteQueryService.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteMapper deliveryNoteMapper;

    public DeliveryNoteQueryService(DeliveryNoteRepository deliveryNoteRepository, DeliveryNoteMapper deliveryNoteMapper) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteMapper = deliveryNoteMapper;
    }

    /**
     * Return a {@link List} of {@link DeliveryNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeliveryNoteDTO> findByCriteria(DeliveryNoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteMapper.toDto(deliveryNoteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeliveryNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryNoteDTO> findByCriteria(DeliveryNoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteRepository.findAll(specification, page).map(deliveryNoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeliveryNoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteRepository.count(specification);
    }

    /**
     * Function to convert {@link DeliveryNoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeliveryNote> createSpecification(DeliveryNoteCriteria criteria) {
        Specification<DeliveryNote> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DeliveryNote_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), DeliveryNote_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), DeliveryNote_.number));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), DeliveryNote_.contactName));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), DeliveryNote_.date));
            }
            if (criteria.getPeriodText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodText(), DeliveryNote_.periodText));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), DeliveryNote_.currency));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), DeliveryNote_.total));
            }
            if (criteria.getVatIncluded() != null) {
                specification = specification.and(buildSpecification(criteria.getVatIncluded(), DeliveryNote_.vatIncluded));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), DeliveryNote_.discountRate));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), DeliveryNote_.discountType));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), DeliveryNote_.language));
            }
            if (criteria.getPageAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageAmount(), DeliveryNote_.pageAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), DeliveryNote_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DeliveryNote_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), DeliveryNote_.created));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(DeliveryNote_.customFields, JoinType.LEFT).get(CustomFieldValue_.id)
                        )
                    );
            }
            if (criteria.getFreeTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFreeTextsId(),
                            root -> root.join(DeliveryNote_.freeTexts, JoinType.LEFT).get(DocumentFreeText_.id)
                        )
                    );
            }
            if (criteria.getTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTextsId(),
                            root -> root.join(DeliveryNote_.texts, JoinType.LEFT).get(DescriptiveDocumentText_.id)
                        )
                    );
            }
            if (criteria.getPositionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPositionsId(),
                            root -> root.join(DeliveryNote_.positions, JoinType.LEFT).get(DocumentPosition_.id)
                        )
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(DeliveryNote_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
            if (criteria.getContactAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactAddressId(),
                            root -> root.join(DeliveryNote_.contactAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(DeliveryNote_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
            if (criteria.getContactPrePageAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPrePageAddressId(),
                            root -> root.join(DeliveryNote_.contactPrePageAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayoutId(), root -> root.join(DeliveryNote_.layout, JoinType.LEFT).get(Layout_.id))
                    );
            }
            if (criteria.getSignatureId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatureId(),
                            root -> root.join(DeliveryNote_.signature, JoinType.LEFT).get(Signature_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
