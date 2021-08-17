package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.InvoiceStatus;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
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
 * Criteria class for the {@link ch.united.fastadmin.domain.Invoice} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable, Criteria {

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
     * Class for filtering IsrPosition
     */
    public static class IsrPositionFilter extends Filter<IsrPosition> {

        public IsrPositionFilter() {}

        public IsrPositionFilter(IsrPositionFilter filter) {
            super(filter);
        }

        @Override
        public IsrPositionFilter copy() {
            return new IsrPositionFilter(this);
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
     * Class for filtering InvoiceStatus
     */
    public static class InvoiceStatusFilter extends Filter<InvoiceStatus> {

        public InvoiceStatusFilter() {}

        public InvoiceStatusFilter(InvoiceStatusFilter filter) {
            super(filter);
        }

        @Override
        public InvoiceStatusFilter copy() {
            return new InvoiceStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter number;

    private StringFilter contactName;

    private LocalDateFilter date;

    private LocalDateFilter due;

    private LocalDateFilter periodFrom;

    private LocalDateFilter periodTo;

    private StringFilter periodText;

    private CurrencyFilter currency;

    private DoubleFilter total;

    private BooleanFilter vatIncluded;

    private DoubleFilter discountRate;

    private DiscountTypeFilter discountType;

    private IntegerFilter cashDiscountRate;

    private LocalDateFilter cashDiscountDate;

    private DoubleFilter totalPaid;

    private StringFilter paidDate;

    private IsrPositionFilter isrPosition;

    private StringFilter isrReferenceNumber;

    private BooleanFilter paymentLinkPaypal;

    private StringFilter paymentLinkPaypalUrl;

    private BooleanFilter paymentLinkPostfinance;

    private StringFilter paymentLinkPostfinanceUrl;

    private BooleanFilter paymentLinkPayrexx;

    private StringFilter paymentLinkPayrexxUrl;

    private BooleanFilter paymentLinkSmartcommerce;

    private StringFilter paymentLinkSmartcommerceUrl;

    private DocumentLanguageFilter language;

    private IntegerFilter pageAmount;

    private StringFilter notes;

    private InvoiceStatusFilter status;

    private ZonedDateTimeFilter created;

    private LongFilter freeTextsId;

    private LongFilter textsId;

    private LongFilter positionsId;

    private LongFilter contactId;

    private LongFilter contactAddressId;

    private LongFilter contactPersonId;

    private LongFilter contactPrePageAddressId;

    private LongFilter layoutId;

    private LongFilter layoutId;

    private LongFilter bankAccountId;

    private LongFilter isrId;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.due = other.due == null ? null : other.due.copy();
        this.periodFrom = other.periodFrom == null ? null : other.periodFrom.copy();
        this.periodTo = other.periodTo == null ? null : other.periodTo.copy();
        this.periodText = other.periodText == null ? null : other.periodText.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.vatIncluded = other.vatIncluded == null ? null : other.vatIncluded.copy();
        this.discountRate = other.discountRate == null ? null : other.discountRate.copy();
        this.discountType = other.discountType == null ? null : other.discountType.copy();
        this.cashDiscountRate = other.cashDiscountRate == null ? null : other.cashDiscountRate.copy();
        this.cashDiscountDate = other.cashDiscountDate == null ? null : other.cashDiscountDate.copy();
        this.totalPaid = other.totalPaid == null ? null : other.totalPaid.copy();
        this.paidDate = other.paidDate == null ? null : other.paidDate.copy();
        this.isrPosition = other.isrPosition == null ? null : other.isrPosition.copy();
        this.isrReferenceNumber = other.isrReferenceNumber == null ? null : other.isrReferenceNumber.copy();
        this.paymentLinkPaypal = other.paymentLinkPaypal == null ? null : other.paymentLinkPaypal.copy();
        this.paymentLinkPaypalUrl = other.paymentLinkPaypalUrl == null ? null : other.paymentLinkPaypalUrl.copy();
        this.paymentLinkPostfinance = other.paymentLinkPostfinance == null ? null : other.paymentLinkPostfinance.copy();
        this.paymentLinkPostfinanceUrl = other.paymentLinkPostfinanceUrl == null ? null : other.paymentLinkPostfinanceUrl.copy();
        this.paymentLinkPayrexx = other.paymentLinkPayrexx == null ? null : other.paymentLinkPayrexx.copy();
        this.paymentLinkPayrexxUrl = other.paymentLinkPayrexxUrl == null ? null : other.paymentLinkPayrexxUrl.copy();
        this.paymentLinkSmartcommerce = other.paymentLinkSmartcommerce == null ? null : other.paymentLinkSmartcommerce.copy();
        this.paymentLinkSmartcommerceUrl = other.paymentLinkSmartcommerceUrl == null ? null : other.paymentLinkSmartcommerceUrl.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.pageAmount = other.pageAmount == null ? null : other.pageAmount.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.freeTextsId = other.freeTextsId == null ? null : other.freeTextsId.copy();
        this.textsId = other.textsId == null ? null : other.textsId.copy();
        this.positionsId = other.positionsId == null ? null : other.positionsId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.contactAddressId = other.contactAddressId == null ? null : other.contactAddressId.copy();
        this.contactPersonId = other.contactPersonId == null ? null : other.contactPersonId.copy();
        this.contactPrePageAddressId = other.contactPrePageAddressId == null ? null : other.contactPrePageAddressId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
        this.layoutId = other.layoutId == null ? null : other.layoutId.copy();
        this.bankAccountId = other.bankAccountId == null ? null : other.bankAccountId.copy();
        this.isrId = other.isrId == null ? null : other.isrId.copy();
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
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

    public LocalDateFilter getDue() {
        return due;
    }

    public LocalDateFilter due() {
        if (due == null) {
            due = new LocalDateFilter();
        }
        return due;
    }

    public void setDue(LocalDateFilter due) {
        this.due = due;
    }

    public LocalDateFilter getPeriodFrom() {
        return periodFrom;
    }

    public LocalDateFilter periodFrom() {
        if (periodFrom == null) {
            periodFrom = new LocalDateFilter();
        }
        return periodFrom;
    }

    public void setPeriodFrom(LocalDateFilter periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDateFilter getPeriodTo() {
        return periodTo;
    }

    public LocalDateFilter periodTo() {
        if (periodTo == null) {
            periodTo = new LocalDateFilter();
        }
        return periodTo;
    }

    public void setPeriodTo(LocalDateFilter periodTo) {
        this.periodTo = periodTo;
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

    public IntegerFilter getCashDiscountRate() {
        return cashDiscountRate;
    }

    public IntegerFilter cashDiscountRate() {
        if (cashDiscountRate == null) {
            cashDiscountRate = new IntegerFilter();
        }
        return cashDiscountRate;
    }

    public void setCashDiscountRate(IntegerFilter cashDiscountRate) {
        this.cashDiscountRate = cashDiscountRate;
    }

    public LocalDateFilter getCashDiscountDate() {
        return cashDiscountDate;
    }

    public LocalDateFilter cashDiscountDate() {
        if (cashDiscountDate == null) {
            cashDiscountDate = new LocalDateFilter();
        }
        return cashDiscountDate;
    }

    public void setCashDiscountDate(LocalDateFilter cashDiscountDate) {
        this.cashDiscountDate = cashDiscountDate;
    }

    public DoubleFilter getTotalPaid() {
        return totalPaid;
    }

    public DoubleFilter totalPaid() {
        if (totalPaid == null) {
            totalPaid = new DoubleFilter();
        }
        return totalPaid;
    }

    public void setTotalPaid(DoubleFilter totalPaid) {
        this.totalPaid = totalPaid;
    }

    public StringFilter getPaidDate() {
        return paidDate;
    }

    public StringFilter paidDate() {
        if (paidDate == null) {
            paidDate = new StringFilter();
        }
        return paidDate;
    }

    public void setPaidDate(StringFilter paidDate) {
        this.paidDate = paidDate;
    }

    public IsrPositionFilter getIsrPosition() {
        return isrPosition;
    }

    public IsrPositionFilter isrPosition() {
        if (isrPosition == null) {
            isrPosition = new IsrPositionFilter();
        }
        return isrPosition;
    }

    public void setIsrPosition(IsrPositionFilter isrPosition) {
        this.isrPosition = isrPosition;
    }

    public StringFilter getIsrReferenceNumber() {
        return isrReferenceNumber;
    }

    public StringFilter isrReferenceNumber() {
        if (isrReferenceNumber == null) {
            isrReferenceNumber = new StringFilter();
        }
        return isrReferenceNumber;
    }

    public void setIsrReferenceNumber(StringFilter isrReferenceNumber) {
        this.isrReferenceNumber = isrReferenceNumber;
    }

    public BooleanFilter getPaymentLinkPaypal() {
        return paymentLinkPaypal;
    }

    public BooleanFilter paymentLinkPaypal() {
        if (paymentLinkPaypal == null) {
            paymentLinkPaypal = new BooleanFilter();
        }
        return paymentLinkPaypal;
    }

    public void setPaymentLinkPaypal(BooleanFilter paymentLinkPaypal) {
        this.paymentLinkPaypal = paymentLinkPaypal;
    }

    public StringFilter getPaymentLinkPaypalUrl() {
        return paymentLinkPaypalUrl;
    }

    public StringFilter paymentLinkPaypalUrl() {
        if (paymentLinkPaypalUrl == null) {
            paymentLinkPaypalUrl = new StringFilter();
        }
        return paymentLinkPaypalUrl;
    }

    public void setPaymentLinkPaypalUrl(StringFilter paymentLinkPaypalUrl) {
        this.paymentLinkPaypalUrl = paymentLinkPaypalUrl;
    }

    public BooleanFilter getPaymentLinkPostfinance() {
        return paymentLinkPostfinance;
    }

    public BooleanFilter paymentLinkPostfinance() {
        if (paymentLinkPostfinance == null) {
            paymentLinkPostfinance = new BooleanFilter();
        }
        return paymentLinkPostfinance;
    }

    public void setPaymentLinkPostfinance(BooleanFilter paymentLinkPostfinance) {
        this.paymentLinkPostfinance = paymentLinkPostfinance;
    }

    public StringFilter getPaymentLinkPostfinanceUrl() {
        return paymentLinkPostfinanceUrl;
    }

    public StringFilter paymentLinkPostfinanceUrl() {
        if (paymentLinkPostfinanceUrl == null) {
            paymentLinkPostfinanceUrl = new StringFilter();
        }
        return paymentLinkPostfinanceUrl;
    }

    public void setPaymentLinkPostfinanceUrl(StringFilter paymentLinkPostfinanceUrl) {
        this.paymentLinkPostfinanceUrl = paymentLinkPostfinanceUrl;
    }

    public BooleanFilter getPaymentLinkPayrexx() {
        return paymentLinkPayrexx;
    }

    public BooleanFilter paymentLinkPayrexx() {
        if (paymentLinkPayrexx == null) {
            paymentLinkPayrexx = new BooleanFilter();
        }
        return paymentLinkPayrexx;
    }

    public void setPaymentLinkPayrexx(BooleanFilter paymentLinkPayrexx) {
        this.paymentLinkPayrexx = paymentLinkPayrexx;
    }

    public StringFilter getPaymentLinkPayrexxUrl() {
        return paymentLinkPayrexxUrl;
    }

    public StringFilter paymentLinkPayrexxUrl() {
        if (paymentLinkPayrexxUrl == null) {
            paymentLinkPayrexxUrl = new StringFilter();
        }
        return paymentLinkPayrexxUrl;
    }

    public void setPaymentLinkPayrexxUrl(StringFilter paymentLinkPayrexxUrl) {
        this.paymentLinkPayrexxUrl = paymentLinkPayrexxUrl;
    }

    public BooleanFilter getPaymentLinkSmartcommerce() {
        return paymentLinkSmartcommerce;
    }

    public BooleanFilter paymentLinkSmartcommerce() {
        if (paymentLinkSmartcommerce == null) {
            paymentLinkSmartcommerce = new BooleanFilter();
        }
        return paymentLinkSmartcommerce;
    }

    public void setPaymentLinkSmartcommerce(BooleanFilter paymentLinkSmartcommerce) {
        this.paymentLinkSmartcommerce = paymentLinkSmartcommerce;
    }

    public StringFilter getPaymentLinkSmartcommerceUrl() {
        return paymentLinkSmartcommerceUrl;
    }

    public StringFilter paymentLinkSmartcommerceUrl() {
        if (paymentLinkSmartcommerceUrl == null) {
            paymentLinkSmartcommerceUrl = new StringFilter();
        }
        return paymentLinkSmartcommerceUrl;
    }

    public void setPaymentLinkSmartcommerceUrl(StringFilter paymentLinkSmartcommerceUrl) {
        this.paymentLinkSmartcommerceUrl = paymentLinkSmartcommerceUrl;
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

    public InvoiceStatusFilter getStatus() {
        return status;
    }

    public InvoiceStatusFilter status() {
        if (status == null) {
            status = new InvoiceStatusFilter();
        }
        return status;
    }

    public void setStatus(InvoiceStatusFilter status) {
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

    public LongFilter getBankAccountId() {
        return bankAccountId;
    }

    public LongFilter bankAccountId() {
        if (bankAccountId == null) {
            bankAccountId = new LongFilter();
        }
        return bankAccountId;
    }

    public void setBankAccountId(LongFilter bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LongFilter getIsrId() {
        return isrId;
    }

    public LongFilter isrId() {
        if (isrId == null) {
            isrId = new LongFilter();
        }
        return isrId;
    }

    public void setIsrId(LongFilter isrId) {
        this.isrId = isrId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(number, that.number) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(date, that.date) &&
            Objects.equals(due, that.due) &&
            Objects.equals(periodFrom, that.periodFrom) &&
            Objects.equals(periodTo, that.periodTo) &&
            Objects.equals(periodText, that.periodText) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(total, that.total) &&
            Objects.equals(vatIncluded, that.vatIncluded) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(cashDiscountRate, that.cashDiscountRate) &&
            Objects.equals(cashDiscountDate, that.cashDiscountDate) &&
            Objects.equals(totalPaid, that.totalPaid) &&
            Objects.equals(paidDate, that.paidDate) &&
            Objects.equals(isrPosition, that.isrPosition) &&
            Objects.equals(isrReferenceNumber, that.isrReferenceNumber) &&
            Objects.equals(paymentLinkPaypal, that.paymentLinkPaypal) &&
            Objects.equals(paymentLinkPaypalUrl, that.paymentLinkPaypalUrl) &&
            Objects.equals(paymentLinkPostfinance, that.paymentLinkPostfinance) &&
            Objects.equals(paymentLinkPostfinanceUrl, that.paymentLinkPostfinanceUrl) &&
            Objects.equals(paymentLinkPayrexx, that.paymentLinkPayrexx) &&
            Objects.equals(paymentLinkPayrexxUrl, that.paymentLinkPayrexxUrl) &&
            Objects.equals(paymentLinkSmartcommerce, that.paymentLinkSmartcommerce) &&
            Objects.equals(paymentLinkSmartcommerceUrl, that.paymentLinkSmartcommerceUrl) &&
            Objects.equals(language, that.language) &&
            Objects.equals(pageAmount, that.pageAmount) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(status, that.status) &&
            Objects.equals(created, that.created) &&
            Objects.equals(freeTextsId, that.freeTextsId) &&
            Objects.equals(textsId, that.textsId) &&
            Objects.equals(positionsId, that.positionsId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(contactAddressId, that.contactAddressId) &&
            Objects.equals(contactPersonId, that.contactPersonId) &&
            Objects.equals(contactPrePageAddressId, that.contactPrePageAddressId) &&
            Objects.equals(layoutId, that.layoutId) &&
            Objects.equals(layoutId, that.layoutId) &&
            Objects.equals(bankAccountId, that.bankAccountId) &&
            Objects.equals(isrId, that.isrId)
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
            due,
            periodFrom,
            periodTo,
            periodText,
            currency,
            total,
            vatIncluded,
            discountRate,
            discountType,
            cashDiscountRate,
            cashDiscountDate,
            totalPaid,
            paidDate,
            isrPosition,
            isrReferenceNumber,
            paymentLinkPaypal,
            paymentLinkPaypalUrl,
            paymentLinkPostfinance,
            paymentLinkPostfinanceUrl,
            paymentLinkPayrexx,
            paymentLinkPayrexxUrl,
            paymentLinkSmartcommerce,
            paymentLinkSmartcommerceUrl,
            language,
            pageAmount,
            notes,
            status,
            created,
            freeTextsId,
            textsId,
            positionsId,
            contactId,
            contactAddressId,
            contactPersonId,
            contactPrePageAddressId,
            layoutId,
            layoutId,
            bankAccountId,
            isrId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (due != null ? "due=" + due + ", " : "") +
            (periodFrom != null ? "periodFrom=" + periodFrom + ", " : "") +
            (periodTo != null ? "periodTo=" + periodTo + ", " : "") +
            (periodText != null ? "periodText=" + periodText + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (vatIncluded != null ? "vatIncluded=" + vatIncluded + ", " : "") +
            (discountRate != null ? "discountRate=" + discountRate + ", " : "") +
            (discountType != null ? "discountType=" + discountType + ", " : "") +
            (cashDiscountRate != null ? "cashDiscountRate=" + cashDiscountRate + ", " : "") +
            (cashDiscountDate != null ? "cashDiscountDate=" + cashDiscountDate + ", " : "") +
            (totalPaid != null ? "totalPaid=" + totalPaid + ", " : "") +
            (paidDate != null ? "paidDate=" + paidDate + ", " : "") +
            (isrPosition != null ? "isrPosition=" + isrPosition + ", " : "") +
            (isrReferenceNumber != null ? "isrReferenceNumber=" + isrReferenceNumber + ", " : "") +
            (paymentLinkPaypal != null ? "paymentLinkPaypal=" + paymentLinkPaypal + ", " : "") +
            (paymentLinkPaypalUrl != null ? "paymentLinkPaypalUrl=" + paymentLinkPaypalUrl + ", " : "") +
            (paymentLinkPostfinance != null ? "paymentLinkPostfinance=" + paymentLinkPostfinance + ", " : "") +
            (paymentLinkPostfinanceUrl != null ? "paymentLinkPostfinanceUrl=" + paymentLinkPostfinanceUrl + ", " : "") +
            (paymentLinkPayrexx != null ? "paymentLinkPayrexx=" + paymentLinkPayrexx + ", " : "") +
            (paymentLinkPayrexxUrl != null ? "paymentLinkPayrexxUrl=" + paymentLinkPayrexxUrl + ", " : "") +
            (paymentLinkSmartcommerce != null ? "paymentLinkSmartcommerce=" + paymentLinkSmartcommerce + ", " : "") +
            (paymentLinkSmartcommerceUrl != null ? "paymentLinkSmartcommerceUrl=" + paymentLinkSmartcommerceUrl + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (pageAmount != null ? "pageAmount=" + pageAmount + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (freeTextsId != null ? "freeTextsId=" + freeTextsId + ", " : "") +
            (textsId != null ? "textsId=" + textsId + ", " : "") +
            (positionsId != null ? "positionsId=" + positionsId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (contactAddressId != null ? "contactAddressId=" + contactAddressId + ", " : "") +
            (contactPersonId != null ? "contactPersonId=" + contactPersonId + ", " : "") +
            (contactPrePageAddressId != null ? "contactPrePageAddressId=" + contactPrePageAddressId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            (layoutId != null ? "layoutId=" + layoutId + ", " : "") +
            (bankAccountId != null ? "bankAccountId=" + bankAccountId + ", " : "") +
            (isrId != null ? "isrId=" + isrId + ", " : "") +
            "}";
    }
}
