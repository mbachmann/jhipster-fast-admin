package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OfferAcceptOnlineStatus;
import ch.united.fastadmin.domain.enumeration.OfferStatus;
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
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Offer implements Serializable {

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
     * date that offer is valid until
     */
    @Column(name = "valid_until")
    private LocalDate validUntil;

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
     * whether offer can be accepted online
     */
    @Column(name = "accept_online")
    private Boolean acceptOnline;

    /**
     * generated URL for accepting the offer online
     */
    @Column(name = "accept_online_url")
    private String acceptOnlineUrl;

    /**
     * status of offer's acceptance; possible values: A - accepted, D - declined, U - unknown
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "accept_online_status")
    private OfferAcceptOnlineStatus acceptOnlineStatus;

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
     * status of offer, possible values: DR - draft, S - sent, DE - declined, A - accepted, B - billed, D - deleted (but still visible)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OfferStatus status;

    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * the free texts belonging to this document
     */
    @OneToMany(mappedBy = "offer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "documentLetter", "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DocumentFreeText> freeTexts = new HashSet<>();

    /**
     * the title, introduction, condition texts belonging to this document
     */
    @OneToMany(mappedBy = "offer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DescriptiveDocumentText> texts = new HashSet<>();

    /**
     * the positions belonging to this document
     */
    @OneToMany(mappedBy = "offer")
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

    public Offer id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Offer remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public Offer number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return this.contactName;
    }

    public Offer contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Offer date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getValidUntil() {
        return this.validUntil;
    }

    public Offer validUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getPeriodText() {
        return this.periodText;
    }

    public Offer periodText(String periodText) {
        this.periodText = periodText;
        return this;
    }

    public void setPeriodText(String periodText) {
        this.periodText = periodText;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Offer currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return this.total;
    }

    public Offer total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getVatIncluded() {
        return this.vatIncluded;
    }

    public Offer vatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
        return this;
    }

    public void setVatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
    }

    public Double getDiscountRate() {
        return this.discountRate;
    }

    public Offer discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Offer discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Boolean getAcceptOnline() {
        return this.acceptOnline;
    }

    public Offer acceptOnline(Boolean acceptOnline) {
        this.acceptOnline = acceptOnline;
        return this;
    }

    public void setAcceptOnline(Boolean acceptOnline) {
        this.acceptOnline = acceptOnline;
    }

    public String getAcceptOnlineUrl() {
        return this.acceptOnlineUrl;
    }

    public Offer acceptOnlineUrl(String acceptOnlineUrl) {
        this.acceptOnlineUrl = acceptOnlineUrl;
        return this;
    }

    public void setAcceptOnlineUrl(String acceptOnlineUrl) {
        this.acceptOnlineUrl = acceptOnlineUrl;
    }

    public OfferAcceptOnlineStatus getAcceptOnlineStatus() {
        return this.acceptOnlineStatus;
    }

    public Offer acceptOnlineStatus(OfferAcceptOnlineStatus acceptOnlineStatus) {
        this.acceptOnlineStatus = acceptOnlineStatus;
        return this;
    }

    public void setAcceptOnlineStatus(OfferAcceptOnlineStatus acceptOnlineStatus) {
        this.acceptOnlineStatus = acceptOnlineStatus;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public Offer language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return this.pageAmount;
    }

    public Offer pageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return this.notes;
    }

    public Offer notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OfferStatus getStatus() {
        return this.status;
    }

    public Offer status(OfferStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Offer created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<DocumentFreeText> getFreeTexts() {
        return this.freeTexts;
    }

    public Offer freeTexts(Set<DocumentFreeText> documentFreeTexts) {
        this.setFreeTexts(documentFreeTexts);
        return this;
    }

    public Offer addFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.add(documentFreeText);
        documentFreeText.setOffer(this);
        return this;
    }

    public Offer removeFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.remove(documentFreeText);
        documentFreeText.setOffer(null);
        return this;
    }

    public void setFreeTexts(Set<DocumentFreeText> documentFreeTexts) {
        if (this.freeTexts != null) {
            this.freeTexts.forEach(i -> i.setOffer(null));
        }
        if (documentFreeTexts != null) {
            documentFreeTexts.forEach(i -> i.setOffer(this));
        }
        this.freeTexts = documentFreeTexts;
    }

    public Set<DescriptiveDocumentText> getTexts() {
        return this.texts;
    }

    public Offer texts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        this.setTexts(descriptiveDocumentTexts);
        return this;
    }

    public Offer addTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.add(descriptiveDocumentText);
        descriptiveDocumentText.setOffer(this);
        return this;
    }

    public Offer removeTexts(DescriptiveDocumentText descriptiveDocumentText) {
        this.texts.remove(descriptiveDocumentText);
        descriptiveDocumentText.setOffer(null);
        return this;
    }

    public void setTexts(Set<DescriptiveDocumentText> descriptiveDocumentTexts) {
        if (this.texts != null) {
            this.texts.forEach(i -> i.setOffer(null));
        }
        if (descriptiveDocumentTexts != null) {
            descriptiveDocumentTexts.forEach(i -> i.setOffer(this));
        }
        this.texts = descriptiveDocumentTexts;
    }

    public Set<DocumentPosition> getPositions() {
        return this.positions;
    }

    public Offer positions(Set<DocumentPosition> documentPositions) {
        this.setPositions(documentPositions);
        return this;
    }

    public Offer addPositions(DocumentPosition documentPosition) {
        this.positions.add(documentPosition);
        documentPosition.setOffer(this);
        return this;
    }

    public Offer removePositions(DocumentPosition documentPosition) {
        this.positions.remove(documentPosition);
        documentPosition.setOffer(null);
        return this;
    }

    public void setPositions(Set<DocumentPosition> documentPositions) {
        if (this.positions != null) {
            this.positions.forEach(i -> i.setOffer(null));
        }
        if (documentPositions != null) {
            documentPositions.forEach(i -> i.setOffer(this));
        }
        this.positions = documentPositions;
    }

    public Contact getContact() {
        return this.contact;
    }

    public Offer contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactAddress getContactAddress() {
        return this.contactAddress;
    }

    public Offer contactAddress(ContactAddress contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public Offer contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddress getContactPrePageAddress() {
        return this.contactPrePageAddress;
    }

    public Offer contactPrePageAddress(ContactAddress contactAddress) {
        this.setContactPrePageAddress(contactAddress);
        return this;
    }

    public void setContactPrePageAddress(ContactAddress contactAddress) {
        this.contactPrePageAddress = contactAddress;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public Offer layout(Layout layout) {
        this.setLayout(layout);
        return this;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public Offer signature(Signature signature) {
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
        if (!(o instanceof Offer)) {
            return false;
        }
        return id != null && id.equals(((Offer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Offer{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", periodText='" + getPeriodText() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", total=" + getTotal() +
            ", vatIncluded='" + getVatIncluded() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", acceptOnline='" + getAcceptOnline() + "'" +
            ", acceptOnlineUrl='" + getAcceptOnlineUrl() + "'" +
            ", acceptOnlineStatus='" + getAcceptOnlineStatus() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
