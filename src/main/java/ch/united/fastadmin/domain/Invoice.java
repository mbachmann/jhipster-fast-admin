package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.InvoiceStatus;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id of a remote system
     */

    @Column(name = "remote_id", unique = true)
    private Integer remoteId;

    /**
     * document number
     */

    @Column(name = "number", unique = true)
    private String number;

    /**
     * name of a contact
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * date of the document
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * due date of the invoice
     */
    @Column(name = "due")
    private LocalDate due;

    /**
     * service period from
     */
    @Column(name = "period_from")
    private LocalDate periodFrom;

    /**
     * service period to
     */
    @Column(name = "period_to")
    private LocalDate periodTo;

    /**
     * service/delivery
     */
    @Column(name = "period_text")
    private String periodText;

    /**
     * currency (e.g. CHF, EUR)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    /**
     * total amount of the delivery note
     */
    @Column(name = "total")
    private Double total;

    /**
     * whether prices in delivery note include VAT
     */
    @Column(name = "vat_included")
    private Boolean vatIncluded;

    /**
     * optional discount rate
     */
    @Column(name = "discount_rate")
    private Double discountRate;

    /**
     * optional discount type; possible values: A - amount, P - in %
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    /**
     * cash discount value in %
     */
    @Column(name = "cash_discount_rate")
    private Integer cashDiscountRate;

    /**
     * cash discount deadline date
     */
    @Column(name = "cash_discount_date")
    private LocalDate cashDiscountDate;

    /**
     * paid amount of the invoice
     */
    @Column(name = "total_paid")
    private Double totalPaid;

    /**
     * date when invoice was fully paid
     */
    @Column(name = "paid_date")
    private String paidDate;

    /**
     * ISR position; possible values: A - additional page, F - first page, L - last page
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "isr_position")
    private IsrPosition isrPosition;

    /**
     * ISR reference number
     */
    @Column(name = "isr_reference_number")
    private String isrReferenceNumber;

    /**
     * whether to print PayPal link on invoice
     */
    @Column(name = "payment_link_paypal")
    private Boolean paymentLinkPaypal;

    /**
     * PayPal link URL
     */
    @Column(name = "payment_link_paypal_url")
    private String paymentLinkPaypalUrl;

    /**
     * whether to print PostFinance link on invoice
     */
    @Column(name = "payment_link_postfinance")
    private Boolean paymentLinkPostfinance;

    /**
     * PostFinance link URL
     */
    @Column(name = "payment_link_postfinance_url")
    private String paymentLinkPostfinanceUrl;

    /**
     * whether to print Payrexx link on invoice
     */
    @Column(name = "payment_link_payrexx")
    private Boolean paymentLinkPayrexx;

    /**
     * Payrexx link URL
     */
    @Column(name = "payment_link_payrexx_url")
    private String paymentLinkPayrexxUrl;

    /**
     * whether to print SmartCommerce link on invoice
     */
    @Column(name = "payment_link_smartcommerce")
    private Boolean paymentLinkSmartcommerce;

    /**
     * SmartCommerce link URL
     */
    @Column(name = "payment_link_smartcommerce_url")
    private String paymentLinkSmartcommerceUrl;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private DocumentLanguage language;

    /**
     * how many pages the document has
     */
    @Column(name = "page_amount")
    private Integer pageAmount;

    /**
     * optional notes
     */
    @Column(name = "notes")
    private String notes;

    /**
     * status of invoice, possible values: DR - draft, S - sent, P - paid,\nPP - partially paid, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder,\nR - reminder, DC - debt collection, C - cancelled, D - deleted (but still visible)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * the free texts belonging to this document
     */
    @OneToMany(mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "documentLetter", "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DocumentFreeText> freeTexts = new HashSet<>();

    /**
     * the title, introduction, condition texts belonging to this document
     */
    @OneToMany(mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DescriptiveDocumentText> texts = new HashSet<>();

    /**
     * the positions belonging to this document
     */
    @OneToMany(mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unit", "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DocumentPosition> positions = new HashSet<>();

    /**
     * the document contact
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "relations", "groups" }, allowSetters = true)
    private Contact contact;

    /**
     * the optional contact address
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private ContactAddress contactAddress;

    /**
     * the optional contact person
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "contact" }, allowSetters = true)
    private ContactPerson contactPerson;

    /**
     * the optional postal address
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private ContactAddress contactPrePageAddress;

    /**
     * the layout information for rendering
     */
    @ManyToOne
    private Layout layout;

    /**
     * the related user signature
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "applicationUser" }, allowSetters = true)
    private Signature layout;

    /**
     * the related bank account
     */
    @ManyToOne
    private BankAccount bankAccount;

    /**
     * the related payment slip (red and orange valid until 30.09.2022)
     */
    @ManyToOne
    private Isr isr;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Invoice remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public Invoice number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return this.contactName;
    }

    public Invoice contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Invoice date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDue() {
        return this.due;
    }

    public Invoice due(LocalDate due) {
        this.due = due;
        return this;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public LocalDate getPeriodFrom() {
        return this.periodFrom;
    }

    public Invoice periodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
        return this;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return this.periodTo;
    }

    public Invoice periodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
        return this;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }

    public String getPeriodText() {
        return this.periodText;
    }

    public Invoice periodText(String periodText) {
        this.periodText = periodText;
        return this;
    }

    public void setPeriodText(String periodText) {
        this.periodText = periodText;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Invoice currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return this.total;
    }

    public Invoice total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getVatIncluded() {
        return this.vatIncluded;
    }

    public Invoice vatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
        return this;
    }

    public void setVatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
    }

    public Double getDiscountRate() {
        return this.discountRate;
    }

    public Invoice discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Invoice discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Integer getCashDiscountRate() {
        return this.cashDiscountRate;
    }

    public Invoice cashDiscountRate(Integer cashDiscountRate) {
        this.cashDiscountRate = cashDiscountRate;
        return this;
    }

    public void setCashDiscountRate(Integer cashDiscountRate) {
        this.cashDiscountRate = cashDiscountRate;
    }

    public LocalDate getCashDiscountDate() {
        return this.cashDiscountDate;
    }

    public Invoice cashDiscountDate(LocalDate cashDiscountDate) {
        this.cashDiscountDate = cashDiscountDate;
        return this;
    }

    public void setCashDiscountDate(LocalDate cashDiscountDate) {
        this.cashDiscountDate = cashDiscountDate;
    }

    public Double getTotalPaid() {
        return this.totalPaid;
    }

    public Invoice totalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
        return this;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getPaidDate() {
        return this.paidDate;
    }

    public Invoice paidDate(String paidDate) {
        this.paidDate = paidDate;
        return this;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public IsrPosition getIsrPosition() {
        return this.isrPosition;
    }

    public Invoice isrPosition(IsrPosition isrPosition) {
        this.isrPosition = isrPosition;
        return this;
    }

    public void setIsrPosition(IsrPosition isrPosition) {
        this.isrPosition = isrPosition;
    }

    public String getIsrReferenceNumber() {
        return this.isrReferenceNumber;
    }

    public Invoice isrReferenceNumber(String isrReferenceNumber) {
        this.isrReferenceNumber = isrReferenceNumber;
        return this;
    }

    public void setIsrReferenceNumber(String isrReferenceNumber) {
        this.isrReferenceNumber = isrReferenceNumber;
    }

    public Boolean getPaymentLinkPaypal() {
        return this.paymentLinkPaypal;
    }

    public Invoice paymentLinkPaypal(Boolean paymentLinkPaypal) {
        this.paymentLinkPaypal = paymentLinkPaypal;
        return this;
    }

    public void setPaymentLinkPaypal(Boolean paymentLinkPaypal) {
        this.paymentLinkPaypal = paymentLinkPaypal;
    }

    public String getPaymentLinkPaypalUrl() {
        return this.paymentLinkPaypalUrl;
    }

    public Invoice paymentLinkPaypalUrl(String paymentLinkPaypalUrl) {
        this.paymentLinkPaypalUrl = paymentLinkPaypalUrl;
        return this;
    }

    public void setPaymentLinkPaypalUrl(String paymentLinkPaypalUrl) {
        this.paymentLinkPaypalUrl = paymentLinkPaypalUrl;
    }

    public Boolean getPaymentLinkPostfinance() {
        return this.paymentLinkPostfinance;
    }

    public Invoice paymentLinkPostfinance(Boolean paymentLinkPostfinance) {
        this.paymentLinkPostfinance = paymentLinkPostfinance;
        return this;
    }

    public void setPaymentLinkPostfinance(Boolean paymentLinkPostfinance) {
        this.paymentLinkPostfinance = paymentLinkPostfinance;
    }

    public String getPaymentLinkPostfinanceUrl() {
        return this.paymentLinkPostfinanceUrl;
    }

    public Invoice paymentLinkPostfinanceUrl(String paymentLinkPostfinanceUrl) {
        this.paymentLinkPostfinanceUrl = paymentLinkPostfinanceUrl;
        return this;
    }

    public void setPaymentLinkPostfinanceUrl(String paymentLinkPostfinanceUrl) {
        this.paymentLinkPostfinanceUrl = paymentLinkPostfinanceUrl;
    }

    public Boolean getPaymentLinkPayrexx() {
        return this.paymentLinkPayrexx;
    }

    public Invoice paymentLinkPayrexx(Boolean paymentLinkPayrexx) {
        this.paymentLinkPayrexx = paymentLinkPayrexx;
        return this;
    }

    public void setPaymentLinkPayrexx(Boolean paymentLinkPayrexx) {
        this.paymentLinkPayrexx = paymentLinkPayrexx;
    }

    public String getPaymentLinkPayrexxUrl() {
        return this.paymentLinkPayrexxUrl;
    }

    public Invoice paymentLinkPayrexxUrl(String paymentLinkPayrexxUrl) {
        this.paymentLinkPayrexxUrl = paymentLinkPayrexxUrl;
        return this;
    }

    public void setPaymentLinkPayrexxUrl(String paymentLinkPayrexxUrl) {
        this.paymentLinkPayrexxUrl = paymentLinkPayrexxUrl;
    }

    public Boolean getPaymentLinkSmartcommerce() {
        return this.paymentLinkSmartcommerce;
    }

    public Invoice paymentLinkSmartcommerce(Boolean paymentLinkSmartcommerce) {
        this.paymentLinkSmartcommerce = paymentLinkSmartcommerce;
        return this;
    }

    public void setPaymentLinkSmartcommerce(Boolean paymentLinkSmartcommerce) {
        this.paymentLinkSmartcommerce = paymentLinkSmartcommerce;
    }

    public String getPaymentLinkSmartcommerceUrl() {
        return this.paymentLinkSmartcommerceUrl;
    }

    public Invoice paymentLinkSmartcommerceUrl(String paymentLinkSmartcommerceUrl) {
        this.paymentLinkSmartcommerceUrl = paymentLinkSmartcommerceUrl;
        return this;
    }

    public void setPaymentLinkSmartcommerceUrl(String paymentLinkSmartcommerceUrl) {
        this.paymentLinkSmartcommerceUrl = paymentLinkSmartcommerceUrl;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public Invoice language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return this.pageAmount;
    }

    public Invoice pageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return this.notes;
    }

    public Invoice notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public Invoice status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Invoice created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<DocumentFreeText> getFreeTexts() {
        return this.freeTexts;
    }

    public Invoice freeTexts(Set<DocumentFreeText> documentFreeTexts) {
        this.setFreeTexts(documentFreeTexts);
        return this;
    }

    public Invoice addFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.add(documentFreeText);
        documentFreeText.setInvoice(this);
        return this;
    }

    public Invoice removeFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.remove(documentFreeText);
        documentFreeText.setInvoice(null);
        return this;
    }

    public void setFreeTexts(Set<DocumentFreeText> documentFreeTexts) {
        if (this.freeTexts != null) {
            this.freeTexts.forEach(i -> i.setInvoice(null));
        }
        if (documentFreeTexts != null) {
            documentFreeTexts.forEach(i -> i.setInvoice(this));
        }
        this.freeTexts = documentFreeTexts;
    }

    public Set<DescriptiveDocumentText> getTexts() {
        return this.texts;
    }

    public Invoice texts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        this.setTexts(descriptiveDocumentTexts);
        return this;
    }

    public Invoice addTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.add(descriptiveDocumentText);
        descriptiveDocumentText.setInvoice(this);
        return this;
    }

    public Invoice removeTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.remove(descriptiveDocumentText);
        descriptiveDocumentText.setInvoice(null);
        return this;
    }

    public void setTexts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        if (this.texts != null) {
            this.texts.forEach(i -> i.setInvoice(null));
        }
        if (descriptiveDocumentTexts != null) {
            descriptiveDocumentTexts.forEach(i -> i.setInvoice(this));
        }
        this.texts = descriptiveDocumentTexts;
    }

    public Set<DocumentPosition> getPositions() {
        return this.positions;
    }

    public Invoice positions(Set<DocumentPosition> documentPositions) {
        this.setPositions(documentPositions);
        return this;
    }

    public Invoice addPositions(DocumentPosition documentPosition) {
        this.positions.add(documentPosition);
        documentPosition.setInvoice(this);
        return this;
    }

    public Invoice removePositions(DocumentPosition documentPosition) {
        this.positions.remove(documentPosition);
        documentPosition.setInvoice(null);
        return this;
    }

    public void setPositions(Set<DocumentPosition> documentPositions) {
        if (this.positions != null) {
            this.positions.forEach(i -> i.setInvoice(null));
        }
        if (documentPositions != null) {
            documentPositions.forEach(i -> i.setInvoice(this));
        }
        this.positions = documentPositions;
    }

    public Contact getContact() {
        return this.contact;
    }

    public Invoice contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactAddress getContactAddress() {
        return this.contactAddress;
    }

    public Invoice contactAddress(ContactAddress contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public Invoice contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddress getContactPrePageAddress() {
        return this.contactPrePageAddress;
    }

    public Invoice contactPrePageAddress(ContactAddress contactAddress) {
        this.setContactPrePageAddress(contactAddress);
        return this;
    }

    public void setContactPrePageAddress(ContactAddress contactAddress) {
        this.contactPrePageAddress = contactAddress;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public Invoice layout(Layout layout) {
        this.setLayout(layout);
        return this;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Signature getLayout() {
        return this.layout;
    }

    public Invoice layout(Signature signature) {
        this.setLayout(signature);
        return this;
    }

    public void setLayout(Signature signature) {
        this.layout = signature;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public Invoice bankAccount(BankAccount bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Isr getIsr() {
        return this.isr;
    }

    public Invoice isr(Isr isr) {
        this.setIsr(isr);
        return this;
    }

    public void setIsr(Isr isr) {
        this.isr = isr;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", due='" + getDue() + "'" +
            ", periodFrom='" + getPeriodFrom() + "'" +
            ", periodTo='" + getPeriodTo() + "'" +
            ", periodText='" + getPeriodText() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", total=" + getTotal() +
            ", vatIncluded='" + getVatIncluded() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", cashDiscountRate=" + getCashDiscountRate() +
            ", cashDiscountDate='" + getCashDiscountDate() + "'" +
            ", totalPaid=" + getTotalPaid() +
            ", paidDate='" + getPaidDate() + "'" +
            ", isrPosition='" + getIsrPosition() + "'" +
            ", isrReferenceNumber='" + getIsrReferenceNumber() + "'" +
            ", paymentLinkPaypal='" + getPaymentLinkPaypal() + "'" +
            ", paymentLinkPaypalUrl='" + getPaymentLinkPaypalUrl() + "'" +
            ", paymentLinkPostfinance='" + getPaymentLinkPostfinance() + "'" +
            ", paymentLinkPostfinanceUrl='" + getPaymentLinkPostfinanceUrl() + "'" +
            ", paymentLinkPayrexx='" + getPaymentLinkPayrexx() + "'" +
            ", paymentLinkPayrexxUrl='" + getPaymentLinkPayrexxUrl() + "'" +
            ", paymentLinkSmartcommerce='" + getPaymentLinkSmartcommerce() + "'" +
            ", paymentLinkSmartcommerceUrl='" + getPaymentLinkSmartcommerceUrl() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
