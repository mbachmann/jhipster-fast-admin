package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DeliveryNoteStatus;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
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
 * Criteria class for the {@link ch.united.fastadmin.domain.DeliveryNote} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.DeliveryNoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delivery-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeliveryNoteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {}

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }
    }

    /**
     * Class for filtering DiscountType
     */
    public static class DiscountTypeFilter extends Filter<DiscountType> {

        public DiscountTypeFilter() {}

        public DiscountTypeFilter(DiscountTypeFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeFilter copy() {
            return new DiscountTypeFilter(this);
        }
    }

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
     * Class for filtering DeliveryNoteStatus
     */
    public static class DeliveryNoteStatusFilter extends Filter<DeliveryNoteStatus> {

        public DeliveryNoteStatusFilter() {}

        public DeliveryNoteStatusFilter(DeliveryNoteStatusFilter filter) {
            super(filter);
        }

        @Override
        public DeliveryNoteStatusFilter copy() {
            return new DeliveryNoteStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter number;

    private StringFilter contactName;

    private LocalDateFilter date;

    private StringFilter periodText;

    private CurrencyFilter currency;

    private DoubleFilter total;

    private BooleanFilter vatIncluded;

    private DoubleFilter discountRate;

    private DiscountTypeFilter discountType;

    private DocumentLanguageFilter language;

    private IntegerFilter pageAmount;

    private StringFilter notes;

    private DeliveryNoteStatusFilter status;

    private ZonedDateTimeFilter created;

    private LongFilter customFieldsId;

    private LongFilter freeTextsId;

    private LongFilter textsId;

    private LongFilter positionsId;

    private LongFilter contactId;

    private LongFilter contactAddressId;

    private LongFilter contactPersonId;

    private LongFilter contactPrePageAddressId;

    private LongFilter layoutId;

    private LongFilter layoutId;

    public DeliveryNoteCriteria() {}

    public DeliveryNoteCriteria(DeliveryNoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.periodText = other.periodText == null ? null : other.periodText.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.vatIncluded = other.vatIncluded == null ? null : other.vatIncluded.copy();
        this.discountRate = other.discountRate == null ? null : other.discountRate.copy();
        this.discountType = other.discountType == null ? null : other.discountType.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.pageAmount = other.pageAmount == null ? null : other.pageAmount.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
        this.freeTextsId = other.freeTextsId == null ? null : other.freeTextsId.copy();
        this.textsId = other.textsId == null ? null : other.textsId.copy();
        this.positionsId = other.positionsId == null ? null : other.positionsId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.contactAddressId = other.contactAddressId == null ? null : other.contactAddressId.copy();
        this.contactPersonId = other.contactPersonId == null ? null : other.contactPersonId.copy();
        this.contactPrePageAddressId = other.contactPrePageAddressId == null ? null : other.contactPrePageAddressId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
    }

    @Override
    public DeliveryNoteCriteria copy() {
        return new DeliveryNoteCriteria(this);
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

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
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

    public StringFilter getPeriodText() {
        return periodText;
    }

    public StringFilter periodText() {
        if (periodText == null) {
            periodText = new StringFilter();
        }
        return periodText;
    }

    public void setPeriodText(StringFilter periodText) {
        this.periodText = periodText;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public CurrencyFilter currency() {
        if (currency == null) {
            currency = new CurrencyFilter();
        }
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public DoubleFilter getTotal() {
        return total;
    }

    public DoubleFilter total() {
        if (total == null) {
            total = new DoubleFilter();
        }
        return total;
    }

    public void setTotal(DoubleFilter total) {
        this.total = total;
    }

    public BooleanFilter getVatIncluded() {
        return vatIncluded;
    }

    public BooleanFilter vatIncluded() {
        if (vatIncluded == null) {
            vatIncluded = new BooleanFilter();
        }
        return vatIncluded;
    }

    public void setVatIncluded(BooleanFilter vatIncluded) {
        this.vatIncluded = vatIncluded;
    }

    public DoubleFilter getDiscountRate() {
        return discountRate;
    }

    public DoubleFilter discountRate() {
        if (discountRate == null) {
            discountRate = new DoubleFilter();
        }
        return discountRate;
    }

    public void setDiscountRate(DoubleFilter discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountTypeFilter getDiscountType() {
        return discountType;
    }

    public DiscountTypeFilter discountType() {
        if (discountType == null) {
            discountType = new DiscountTypeFilter();
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeFilter discountType) {
        this.discountType = discountType;
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

    public DeliveryNoteStatusFilter getStatus() {
        return status;
    }

    public DeliveryNoteStatusFilter status() {
        if (status == null) {
            status = new DeliveryNoteStatusFilter();
        }
        return status;
    }

    public void setStatus(DeliveryNoteStatusFilter status) {
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

    public LongFilter getTextsId() {
        return textsId;
    }

    public LongFilter textsId() {
        if (textsId == null) {
            textsId = new LongFilter();
        }
        return textsId;
    }

    public void setTextsId(LongFilter textsId) {
        this.textsId = textsId;
    }

    public LongFilter getPositionsId() {
        return positionsId;
    }

    public LongFilter positionsId() {
        if (positionsId == null) {
            positionsId = new LongFilter();
        }
        return positionsId;
    }

    public void setPositionsId(LongFilter positionsId) {
        this.positionsId = positionsId;
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
        final DeliveryNoteCriteria that = (DeliveryNoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(number, that.number) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(date, that.date) &&
            Objects.equals(periodText, that.periodText) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(total, that.total) &&
            Objects.equals(vatIncluded, that.vatIncluded) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(language, that.language) &&
            Objects.equals(pageAmount, that.pageAmount) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(status, that.status) &&
            Objects.equals(created, that.created) &&
            Objects.equals(customFieldsId, that.customFieldsId) &&
            Objects.equals(freeTextsId, that.freeTextsId) &&
            Objects.equals(textsId, that.textsId) &&
            Objects.equals(positionsId, that.positionsId) &&
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
            number,
            contactName,
            date,
            periodText,
            currency,
            total,
            vatIncluded,
            discountRate,
            discountType,
            language,
            pageAmount,
            notes,
            status,
            created,
            customFieldsId,
            freeTextsId,
            textsId,
            positionsId,
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
        return "DeliveryNoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (periodText != null ? "periodText=" + periodText + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (vatIncluded != null ? "vatIncluded=" + vatIncluded + ", " : "") +
            (discountRate != null ? "discountRate=" + discountRate + ", " : "") +
            (discountType != null ? "discountType=" + discountType + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (pageAmount != null ? "pageAmount=" + pageAmount + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            (freeTextsId != null ? "freeTextsId=" + freeTextsId + ", " : "") +
            (textsId != null ? "textsId=" + textsId + ", " : "") +
            (positionsId != null ? "positionsId=" + positionsId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (contactAddressId != null ? "contactAddressId=" + contactAddressId + ", " : "") +
            (contactPersonId != null ? "contactPersonId=" + contactPersonId + ", " : "") +
            (contactPrePageAddressId != null ? "contactPrePageAddressId=" + contactPrePageAddressId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            "}";
    }
}
