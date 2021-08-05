package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.DomainArea;
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
 * Criteria class for the {@link ch.united.fastadmin.domain.CustomField} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.CustomFieldResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /custom-fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomFieldCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DomainArea
     */
    public static class DomainAreaFilter extends Filter<DomainArea> {

        public DomainAreaFilter() {}

        public DomainAreaFilter(DomainAreaFilter filter) {
            super(filter);
        }

        @Override
        public DomainAreaFilter copy() {
            return new DomainAreaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DomainAreaFilter domainArea;

    private StringFilter key;

    private StringFilter name;

    private StringFilter value;

    private LongFilter contactId;

    private LongFilter contactPersonId;

    public CustomFieldCriteria() {}

    public CustomFieldCriteria(CustomFieldCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.domainArea = other.domainArea == null ? null : other.domainArea.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.contactPersonId = other.contactPersonId == null ? null : other.contactPersonId.copy();
    }

    @Override
    public CustomFieldCriteria copy() {
        return new CustomFieldCriteria(this);
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

    public DomainAreaFilter getDomainArea() {
        return domainArea;
    }

    public DomainAreaFilter domainArea() {
        if (domainArea == null) {
            domainArea = new DomainAreaFilter();
        }
        return domainArea;
    }

    public void setDomainArea(DomainAreaFilter domainArea) {
        this.domainArea = domainArea;
    }

    public StringFilter getKey() {
        return key;
    }

    public StringFilter key() {
        if (key == null) {
            key = new StringFilter();
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public LongFilter contactId() {
        if (contactId == null) {
            contactId = new LongFilter();
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getContactPersonId() {
        return contactPersonId;
    }

    public LongFilter contactPersonId() {
        if (contactPersonId == null) {
            contactPersonId = new LongFilter();
        }
        return contactPersonId;
    }

    public void setContactPersonId(LongFilter contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomFieldCriteria that = (CustomFieldCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(domainArea, that.domainArea) &&
            Objects.equals(key, that.key) &&
            Objects.equals(name, that.name) &&
            Objects.equals(value, that.value) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(contactPersonId, that.contactPersonId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domainArea, key, name, value, contactId, contactPersonId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomFieldCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (domainArea != null ? "domainArea=" + domainArea + ", " : "") +
            (key != null ? "key=" + key + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (contactPersonId != null ? "contactPersonId=" + contactPersonId + ", " : "") +
            "}";
    }
}
