package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.LetterStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.DocumentLetter} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.DocumentLetterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-letters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentLetterCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentLanguage
     */
    public static class DocumentLanguageFilter extends Filter<DocumentLanguage> {

        public DocumentLanguageFilter() {}

        public DocumentLanguageFilter(DocumentLanguageFilter filter) {
            super(filter);
        }

        @Override
        public DocumentLanguageFilter copy() {
            return new DocumentLanguageFilter(this);
        }
    }

    /**
     * Class for filtering LetterStatus
     */
    public static class LetterStatusFilter extends Filter<LetterStatus> {

        public LetterStatusFilter() {}

        public LetterStatusFilter(LetterStatusFilter filter) {
            super(filter);
        }

        @Override
        public LetterStatusFilter copy() {
            return new LetterStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter contactName;

    private LocalDateFilter date;

    private StringFilter title;

    private StringFilter content;

    private DocumentLanguageFilter language;

    private IntegerFilter pageAmount;

    private StringFilter notes;

    private LetterStatusFilter status;

    private ZonedDateTimeFilter created;

    private LongFilter customFieldsId;

    private LongFilter freeTextsId;

    private LongFilter contactId;

    private LongFilter contactAddressId;

    private LongFilter contactPersonId;

    private LongFilter contactPrePageAddressId;

    private LongFilter layoutId;

    private LongFilter layoutId;

    public DocumentLetterCriteria() {}

    public DocumentLetterCriteria(DocumentLetterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.pageAmount = other.pageAmount == null ? null : other.pageAmount.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
        this.freeTextsId = other.freeTextsId == null ? null : other.freeTextsId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.contactAddressId = other.contactAddressId == null ? null : other.contactAddressId.copy();
        this.contactPersonId = other.contactPersonId == null ? null : other.contactPersonId.copy();
        this.contactPrePageAddressId = other.contactPrePageAddressId == null ? null : other.contactPrePageAddressId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
    }

    @Override
    public DocumentLetterCriteria copy() {
        return new DocumentLetterCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
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

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public DocumentLanguageFilter getLanguage() {
        return language;
    }

    public DocumentLanguageFilter language() {
        if (language == null) {
            language = new DocumentLanguageFilter();
        }
        return language;
    }

    public void setLanguage(DocumentLanguageFilter language) {
        this.language = language;
    }

    public IntegerFilter getPageAmount() {
        return pageAmount;
    }

    public IntegerFilter pageAmount() {
        if (pageAmount == null) {
            pageAmount = new IntegerFilter();
        }
        return pageAmount;
    }

    public void setPageAmount(IntegerFilter pageAmount) {
        this.pageAmount = pageAmount;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LetterStatusFilter getStatus() {
        return status;
    }

    public LetterStatusFilter status() {
        if (status == null) {
            status = new LetterStatusFilter();
        }
        return status;
    }

    public void setStatus(LetterStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getCreated() {
        return created;
    }

    public ZonedDateTimeFilter created() {
        if (created == null) {
            created = new ZonedDateTimeFilter();
        }
        return created;
    }

    public void setCreated(ZonedDateTimeFilter created) {
        this.created = created;
    }

    public LongFilter getCustomFieldsId() {
        return customFieldsId;
    }

    public LongFilter customFieldsId() {
        if (customFieldsId == null) {
            customFieldsId = new LongFilter();
        }
        return customFieldsId;
    }

    public void setCustomFieldsId(LongFilter customFieldsId) {
        this.customFieldsId = customFieldsId;
    }

    public LongFilter getFreeTextsId() {
        return freeTextsId;
    }

    public LongFilter freeTextsId() {
        if (freeTextsId == null) {
            freeTextsId = new LongFilter();
        }
        return freeTextsId;
    }

    public void setFreeTextsId(LongFilter freeTextsId) {
        this.freeTextsId = freeTextsId;
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

    public LongFilter getContactAddressId() {
        return contactAddressId;
    }

    public LongFilter contactAddressId() {
        if (contactAddressId == null) {
            contactAddressId = new LongFilter();
        }
        return contactAddressId;
    }

    public void setContactAddressId(LongFilter contactAddressId) {
        this.contactAddressId = contactAddressId;
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

    public LongFilter getContactPrePageAddressId() {
        return contactPrePageAddressId;
    }

    public LongFilter contactPrePageAddressId() {
        if (contactPrePageAddressId == null) {
            contactPrePageAddressId = new LongFilter();
        }
        return contactPrePageAddressId;
    }

    public void setContactPrePageAddressId(LongFilter contactPrePageAddressId) {
        this.contactPrePageAddressId = contactPrePageAddressId;
    }

    public LongFilter getLayoutId() {
        return layoutId;
    }

    public LongFilter layoutId() {
        if (layoutId == null) {
            layoutId = new LongFilter();
        }
        return layoutId;
    }

    public void setLayoutId(LongFilter layoutId) {
        this.layoutId = layoutId;
    }

    public LongFilter getLayoutId() {
        return layoutId;
    }

    public LongFilter layoutId() {
        if (layoutId == null) {
            layoutId = new LongFilter();
        }
        return layoutId;
    }

    public void setLayoutId(LongFilter layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentLetterCriteria that = (DocumentLetterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(date, that.date) &&
            Objects.equals(title, that.title) &&
            Objects.equals(content, that.content) &&
            Objects.equals(language, that.language) &&
            Objects.equals(pageAmount, that.pageAmount) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(status, that.status) &&
            Objects.equals(created, that.created) &&
            Objects.equals(customFieldsId, that.customFieldsId) &&
            Objects.equals(freeTextsId, that.freeTextsId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(contactAddressId, that.contactAddressId) &&
            Objects.equals(contactPersonId, that.contactPersonId) &&
            Objects.equals(contactPrePageAddressId, that.contactPrePageAddressId) &&
            Objects.equals(layoutId, that.layoutId) &&
            Objects.equals(layoutId, that.layoutId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            contactName,
            date,
            title,
            content,
            language,
            pageAmount,
            notes,
            status,
            created,
            customFieldsId,
            freeTextsId,
            contactId,
            contactAddressId,
            contactPersonId,
            contactPrePageAddressId,
            layoutId,
            layoutId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentLetterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (pageAmount != null ? "pageAmount=" + pageAmount + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            (freeTextsId != null ? "freeTextsId=" + freeTextsId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (contactAddressId != null ? "contactAddressId=" + contactAddressId + ", " : "") +
            (contactPersonId != null ? "contactPersonId=" + contactPersonId + ", " : "") +
            (contactPrePageAddressId != null ? "contactPrePageAddressId=" + contactPrePageAddressId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            "}";
    }
}
