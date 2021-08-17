package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.IntervalType;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.ContactReminder} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ContactReminderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-reminders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactReminderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IntervalType
     */
    public static class IntervalTypeFilter extends Filter<IntervalType> {

        public IntervalTypeFilter() {}

        public IntervalTypeFilter(IntervalTypeFilter filter) {
            super(filter);
        }

        @Override
        public IntervalTypeFilter copy() {
            return new IntervalTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private IntegerFilter contactId;

    private StringFilter contactName;

    private ZonedDateTimeFilter dateTime;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter intervalValue;

    private IntervalTypeFilter intervalType;

    private BooleanFilter inactiv;

    private LongFilter contactId;

    public ContactReminderCriteria() {}

    public ContactReminderCriteria(ContactReminderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.dateTime = other.dateTime == null ? null : other.dateTime.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.intervalValue = other.intervalValue == null ? null : other.intervalValue.copy();
        this.intervalType = other.intervalType == null ? null : other.intervalType.copy();
        this.inactiv = other.inactiv == null ? null : other.inactiv.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
    }

    @Override
    public ContactReminderCriteria copy() {
        return new ContactReminderCriteria(this);
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

    public IntegerFilter getRemoteId() {
        return remoteId;
    }

    public IntegerFilter remoteId() {
        if (remoteId == null) {
            remoteId = new IntegerFilter();
        }
        return remoteId;
    }

    public void setRemoteId(IntegerFilter remoteId) {
        this.remoteId = remoteId;
    }

    public IntegerFilter getContactId() {
        return contactId;
    }

    public IntegerFilter contactId() {
        if (contactId == null) {
            contactId = new IntegerFilter();
        }
        return contactId;
    }

    public void setContactId(IntegerFilter contactId) {
        this.contactId = contactId;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public StringFilter contactName() {
        if (contactName == null) {
            contactName = new StringFilter();
        }
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public ZonedDateTimeFilter getDateTime() {
        return dateTime;
    }

    public ZonedDateTimeFilter dateTime() {
        if (dateTime == null) {
            dateTime = new ZonedDateTimeFilter();
        }
        return dateTime;
    }

    public void setDateTime(ZonedDateTimeFilter dateTime) {
        this.dateTime = dateTime;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getIntervalValue() {
        return intervalValue;
    }

    public IntegerFilter intervalValue() {
        if (intervalValue == null) {
            intervalValue = new IntegerFilter();
        }
        return intervalValue;
    }

    public void setIntervalValue(IntegerFilter intervalValue) {
        this.intervalValue = intervalValue;
    }

    public IntervalTypeFilter getIntervalType() {
        return intervalType;
    }

    public IntervalTypeFilter intervalType() {
        if (intervalType == null) {
            intervalType = new IntervalTypeFilter();
        }
        return intervalType;
    }

    public void setIntervalType(IntervalTypeFilter intervalType) {
        this.intervalType = intervalType;
    }

    public BooleanFilter getInactiv() {
        return inactiv;
    }

    public BooleanFilter inactiv() {
        if (inactiv == null) {
            inactiv = new BooleanFilter();
        }
        return inactiv;
    }

    public void setInactiv(BooleanFilter inactiv) {
        this.inactiv = inactiv;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactReminderCriteria that = (ContactReminderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(dateTime, that.dateTime) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(intervalValue, that.intervalValue) &&
            Objects.equals(intervalType, that.intervalType) &&
            Objects.equals(inactiv, that.inactiv) &&
            Objects.equals(contactId, that.contactId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            contactId,
            contactName,
            dateTime,
            title,
            description,
            intervalValue,
            intervalType,
            inactiv,
            contactId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactReminderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (dateTime != null ? "dateTime=" + dateTime + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (intervalValue != null ? "intervalValue=" + intervalValue + ", " : "") +
            (intervalType != null ? "intervalType=" + intervalType + ", " : "") +
            (inactiv != null ? "inactiv=" + inactiv + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            "}";
    }
}
