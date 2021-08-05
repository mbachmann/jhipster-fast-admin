package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.ContactRelationType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.ContactRelation} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ContactRelationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-relations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactRelationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContactRelationType
     */
    public static class ContactRelationTypeFilter extends Filter<ContactRelationType> {

        public ContactRelationTypeFilter() {}

        public ContactRelationTypeFilter(ContactRelationTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContactRelationTypeFilter copy() {
            return new ContactRelationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ContactRelationTypeFilter contactRelationType;

    private LongFilter contactsId;

    public ContactRelationCriteria() {}

    public ContactRelationCriteria(ContactRelationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactRelationType = other.contactRelationType == null ? null : other.contactRelationType.copy();
        this.contactsId = other.contactsId == null ? null : other.contactsId.copy();
    }

    @Override
    public ContactRelationCriteria copy() {
        return new ContactRelationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ContactRelationTypeFilter getContactRelationType() {
        return contactRelationType;
    }

    public ContactRelationTypeFilter contactRelationType() {
        if (contactRelationType == null) {
            contactRelationType = new ContactRelationTypeFilter();
        }
        return contactRelationType;
    }

    public void setContactRelationType(ContactRelationTypeFilter contactRelationType) {
        this.contactRelationType = contactRelationType;
    }

    public LongFilter getContactsId() {
        return contactsId;
    }

    public LongFilter contactsId() {
        if (contactsId == null) {
            contactsId = new LongFilter();
        }
        return contactsId;
    }

    public void setContactsId(LongFilter contactsId) {
        this.contactsId = contactsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactRelationCriteria that = (ContactRelationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contactRelationType, that.contactRelationType) &&
            Objects.equals(contactsId, that.contactsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactRelationType, contactsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactRelationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (contactRelationType != null ? "contactRelationType=" + contactRelationType + ", " : "") +
            (contactsId != null ? "contactsId=" + contactsId + ", " : "") +
            "}";
    }
}
