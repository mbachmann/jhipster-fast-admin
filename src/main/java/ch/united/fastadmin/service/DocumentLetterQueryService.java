package ch.united.fastadmin.service;

import ch.united.fastadmin.domain.*; // for static metamodels
import ch.united.fastadmin.domain.DocumentLetter;
import ch.united.fastadmin.repository.DocumentLetterRepository;
import ch.united.fastadmin.service.criteria.DocumentLetterCriteria;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import ch.united.fastadmin.service.mapper.DocumentLetterMapper;
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
 * Service for executing complex queries for {@link DocumentLetter} entities in the database.
 * The main input is a {@link DocumentLetterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentLetterDTO} or a {@link Page} of {@link DocumentLetterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentLetterQueryService extends QueryService<DocumentLetter> {

    private final Logger log = LoggerFactory.getLogger(DocumentLetterQueryService.class);

    private final DocumentLetterRepository documentLetterRepository;

    private final DocumentLetterMapper documentLetterMapper;

    public DocumentLetterQueryService(DocumentLetterRepository documentLetterRepository, DocumentLetterMapper documentLetterMapper) {
        this.documentLetterRepository = documentLetterRepository;
        this.documentLetterMapper = documentLetterMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentLetterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentLetterDTO> findByCriteria(DocumentLetterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocumentLetter> specification = createSpecification(criteria);
        return documentLetterMapper.toDto(documentLetterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocumentLetterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentLetterDTO> findByCriteria(DocumentLetterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocumentLetter> specification = createSpecification(criteria);
        return documentLetterRepository.findAll(specification, page).map(documentLetterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentLetterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocumentLetter> specification = createSpecification(criteria);
        return documentLetterRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentLetterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocumentLetter> createSpecification(DocumentLetterCriteria criteria) {
        Specification<DocumentLetter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocumentLetter_.id));
            }
            if (criteria.getRemoteId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRemoteId(), DocumentLetter_.remoteId));
            }
            if (criteria.getContactName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactName(), DocumentLetter_.contactName));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), DocumentLetter_.date));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), DocumentLetter_.title));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), DocumentLetter_.content));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), DocumentLetter_.language));
            }
            if (criteria.getPageAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageAmount(), DocumentLetter_.pageAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), DocumentLetter_.notes));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DocumentLetter_.status));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), DocumentLetter_.created));
            }
            if (criteria.getCustomFieldsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomFieldsId(),
                            root -> root.join(DocumentLetter_.customFields, JoinType.LEFT).get(CustomFieldValue_.id)
                        )
                    );
            }
            if (criteria.getFreeTextsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFreeTextsId(),
                            root -> root.join(DocumentLetter_.freeTexts, JoinType.LEFT).get(DocumentFreeText_.id)
                        )
                    );
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(DocumentLetter_.contact, JoinType.LEFT).get(Contact_.id)
                        )
                    );
            }
            if (criteria.getContactAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactAddressId(),
                            root -> root.join(DocumentLetter_.contactAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(DocumentLetter_.contactPerson, JoinType.LEFT).get(ContactPerson_.id)
                        )
                    );
            }
            if (criteria.getContactPrePageAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPrePageAddressId(),
                            root -> root.join(DocumentLetter_.contactPrePageAddress, JoinType.LEFT).get(ContactAddress_.id)
                        )
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLayoutId(), root -> root.join(DocumentLetter_.layout, JoinType.LEFT).get(Layout_.id))
                    );
            }
            if (criteria.getLayoutId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLayoutId(),
                            root -> root.join(DocumentLetter_.layout, JoinType.LEFT).get(Signature_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
