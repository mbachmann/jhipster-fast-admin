package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OrderConfirmationStatus;
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
 * A OrderConfirmation.
 */
@Entity
@Table(name = "order_confirmation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderConfirmation implements Serializable {

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
     * status of order confirmation, possible values: DR - draft, S - sent, B - billed, D - deleted (but still visible)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderConfirmationStatus status;

    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * the free texts belonging to this document
     */
    @OneToMany(mappedBy = "orderConfirmation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "documentLetter", "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DocumentFreeText> freeTexts = new HashSet<>();

    /**
     * the title, introduction, condition texts belonging to this document
     */
    @OneToMany(mappedBy = "orderConfirmation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DescriptiveDocumentText> texts = new HashSet<>();

    /**
     * the positions belonging to this document
     */
    @OneToMany(mappedBy = "orderConfirmation")
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
    private Signature signature;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderConfirmation id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public OrderConfirmation remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public OrderConfirmation number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return this.contactName;
    }

    public OrderConfirmation contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public OrderConfirmation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPeriodText() {
        return this.periodText;
    }

    public OrderConfirmation periodText(String periodText) {
        this.periodText = periodText;
        return this;
    }

    public void setPeriodText(String periodText) {
        this.periodText = periodText;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public OrderConfirmation currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return this.total;
    }

    public OrderConfirmation total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getVatIncluded() {
        return this.vatIncluded;
    }

    public OrderConfirmation vatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
        return this;
    }

    public void setVatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
    }

    public Double getDiscountRate() {
        return this.discountRate;
    }

    public OrderConfirmation discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public OrderConfirmation discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public OrderConfirmation language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return this.pageAmount;
    }

    public OrderConfirmation pageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return this.notes;
    }

    public OrderConfirmation notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrderConfirmationStatus getStatus() {
        return this.status;
    }

    public OrderConfirmation status(OrderConfirmationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderConfirmationStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public OrderConfirmation created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<DocumentFreeText> getFreeTexts() {
        return this.freeTexts;
    }

    public OrderConfirmation freeTexts(Set<DocumentFreeText> documentFreeTexts) {
        this.setFreeTexts(documentFreeTexts);
        return this;
    }

    public OrderConfirmation addFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.add(documentFreeText);
        documentFreeText.setOrderConfirmation(this);
        return this;
    }

    public OrderConfirmation removeFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.remove(documentFreeText);
        documentFreeText.setOrderConfirmation(null);
        return this;
    }

    public void setFreeTexts(Set<DocumentFreeText> documentFreeTexts) {
        if (this.freeTexts != null) {
            this.freeTexts.forEach(i -> i.setOrderConfirmation(null));
        }
        if (documentFreeTexts != null) {
            documentFreeTexts.forEach(i -> i.setOrderConfirmation(this));
        }
        this.freeTexts = documentFreeTexts;
    }

    public Set<DescriptiveDocumentText> getTexts() {
        return this.texts;
    }

    public OrderConfirmation texts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        this.setTexts(descriptiveDocumentTexts);
        return this;
    }

    public OrderConfirmation addTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.add(descriptiveDocumentText);
        descriptiveDocumentText.setOrderConfirmation(this);
        return this;
    }

    public OrderConfirmation removeTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.remove(descriptiveDocumentText);
        descriptiveDocumentText.setOrderConfirmation(null);
        return this;
    }

    public void setTexts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        if (this.texts != null) {
            this.texts.forEach(i -> i.setOrderConfirmation(null));
        }
        if (descriptiveDocumentTexts != null) {
            descriptiveDocumentTexts.forEach(i -> i.setOrderConfirmation(this));
        }
        this.texts = descriptiveDocumentTexts;
    }

    public Set<DocumentPosition> getPositions() {
        return this.positions;
    }

    public OrderConfirmation positions(Set<DocumentPosition> documentPositions) {
        this.setPositions(documentPositions);
        return this;
    }

    public OrderConfirmation addPositions(DocumentPosition documentPosition) {
        this.positions.add(documentPosition);
        documentPosition.setOrderConfirmation(this);
        return this;
    }

    public OrderConfirmation removePositions(DocumentPosition documentPosition) {
        this.positions.remove(documentPosition);
        documentPosition.setOrderConfirmation(null);
        return this;
    }

    public void setPositions(Set<DocumentPosition> documentPositions) {
        if (this.positions != null) {
            this.positions.forEach(i -> i.setOrderConfirmation(null));
        }
        if (documentPositions != null) {
            documentPositions.forEach(i -> i.setOrderConfirmation(this));
        }
        this.positions = documentPositions;
    }

    public Contact getContact() {
        return this.contact;
    }

    public OrderConfirmation contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactAddress getContactAddress() {
        return this.contactAddress;
    }

    public OrderConfirmation contactAddress(ContactAddress contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public OrderConfirmation contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddress getContactPrePageAddress() {
        return this.contactPrePageAddress;
    }

    public OrderConfirmation contactPrePageAddress(ContactAddress contactAddress) {
        this.setContactPrePageAddress(contactAddress);
        return this;
    }

    public void setContactPrePageAddress(ContactAddress contactAddress) {
        this.contactPrePageAddress = contactAddress;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public OrderConfirmation layout(Layout layout) {
        this.setLayout(layout);
        return this;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public OrderConfirmation signature(Signature signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderConfirmation)) {
            return false;
        }
        return id != null && id.equals(((OrderConfirmation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderConfirmation{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", periodText='" + getPeriodText() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", total=" + getTotal() +
            ", vatIncluded='" + getVatIncluded() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
