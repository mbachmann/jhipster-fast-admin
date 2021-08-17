package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.Invoice;
import ch.united.fastadmin.repository.InvoiceRepository;
import ch.united.fastadmin.service.criteria.InvoiceCriteria;
import ch.united.fastadmin.service.dto.InvoiceDTO;
import ch.united.fastadmin.service.mapper.InvoiceMapper;
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
 * Service for executing complex queries for {@link Invoice} entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceDTO} or a {@link Page} of {@link InvoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoiceQueryService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceMapper.toDto(invoiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page).map(invoiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), Invoice_.remoteId));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Invoice_.number));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), Invoice_.contactName));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Invoice_.date));
            }
            if (criteria.getDue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDue(), Invoice_.due));
            }
            if (criteria.getPeriodFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodFrom(), Invoice_.periodFrom));
            }
            if (criteria.getPeriodTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodTo(), Invoice_.periodTo));
            }
            if (criteria.getPeriodText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodText(), Invoice_.periodText));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), Invoice_.currency));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Invoice_.total));
            }
            if (criteria.getVatIncluded() != null) {
                specification = specification.and(buildSpecification(criteria.getVatIncluded(), Invoice_.vatIncluded));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), Invoice_.discountRate));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), Invoice_.discountType));
            }
            if (criteria.getCashDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCashDiscountRate(), Invoice_.cashDiscountRate));
            }
            if (criteria.getCashDiscountDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCashDiscountDate(), Invoice_.cashDiscountDate));
            }
            if (criteria.getTotalPaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPaid(), Invoice_.totalPaid));
            }
            if (criteria.getPaidDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaidDate(), Invoice_.paidDate));
            }
            if (criteria.getIsrPosition() != null) {
                specification = specification.and(buildSpecification(criteria.getIsrPosition(), Invoice_.isrPosition));
            }
            if (criteria.getIsrReferenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIsrReferenceNumber(), Invoice_.isrReferenceNumber));
            }
            if (criteria.getPaymentLinkPaypal() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentLinkPaypal(), Invoice_.paymentLinkPaypal));
            }
            if (criteria.getPaymentLinkPaypalUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentLinkPaypalUrl(), Invoice_.paymentLinkPaypalUrl));
            }
            if (criteria.getPaymentLinkPostfinance() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPaymentLinkPostfinance(), Invoice_.paymentLinkPostfinance));
            }
            if (criteria.getPaymentLinkPostfinanceUrl() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getPaymentLinkPostfinanceUrl(), Invoice_.paymentLinkPostfinanceUrl)
                    );
            }
            if (criteria.getPaymentLinkPayrexx() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentLinkPayrexx(), Invoice_.paymentLinkPayrexx));
            }
            if (criteria.getPaymentLinkPayrexxUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentLinkPayrexxUrl(), Invoice_.paymentLinkPayrexxUrl));
            }
            if (criteria.getPaymentLinkSmartcommerce() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getPaymentLinkSmartcommerce(), Invoice_.paymentLinkSmartcommerce));
            }
            if (criteria.getPaymentLinkSmartcommerceUrl() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getPaymentLinkSmartcommerceUrl(), Invoice_.paymentLinkSmartcommerceUrl)
                    );
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Invoice_.language));
            }
            if (criteria.getPageAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageAmount(), Invoice_.pageAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Invoice_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Invoice_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Invoice_.created));
            }
            if (criteria.getFreeTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFreeTextsId(),
                            root -> root.join(Invoice_.freeTexts, JoinType.LEFT).get(DocumentFreeText_.id)
                        )
                    );
            }
            if (criteria.getTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTextsId(),
                            root -> root.join(Invoice_.texts, JoinType.LEFT).get(DescriptiveDocumentText_.id)
                        )
                    );
            }
            if (criteria.getPositionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPositionsId(),
                            root -> root.join(Invoice_.positions, JoinType.LEFT).get(DocumentPosition_.id)
                        )
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getContactId(), root -> root.join(Invoice_.contact, JoinType.LEFT).get(Contact_.id))
                    );
            }
            if (criteria.getContactAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactAddressId(),
                            root -> root.join(Invoice_.contactAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(Invoice_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
            if (criteria.getContactPrePageAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPrePageAddressId(),
                            root -> root.join(Invoice_.contactPrePageAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayoutId(), root -> root.join(Invoice_.layout, JoinType.LEFT).get(Layout_.id))
                    );
            }
            if (criteria.getSignatureId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatureId(),
                            root -> root.join(Invoice_.signature, JoinType.LEFT).get(Signature_.id)
                        )
                    );
            }
            if (criteria.getBankAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankAccountId(),
                            root -> root.join(Invoice_.bankAccount, JoinType.LEFT).get(BankAccount_.id)
                        )
                    );
            }
            if (criteria.getIsrId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getIsrId(), root -> root.join(Invoice_.isr, JoinType.LEFT).get(Isr_.id)));
            }
        }
        return specification;
    }
}
