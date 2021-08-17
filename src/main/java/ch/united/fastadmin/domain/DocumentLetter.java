package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.LetterStatus;
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
 * A DocumentLetter.
 */
@Entity
@Table(name = "document_letter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentLetter implements Serializable {

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
     * name of a contact
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * date of the letter
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * title of the document
     */
    @Column(name = "title")
    private String title;

    /**
     * content of the document
     */
    @Column(name = "content")
    private String content;

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
     * status of letter, possible values: DR - draft, S - sent, D - deleted (but still visible)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LetterStatus status;

    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "documentLetter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customField", "contact", "contactPerson", "project", "catalogProduct", "catalogService", "documentLetter", "deliveryNote",
        },
        allowSetters = true
    )
    private Set<CustomFieldValue> customFields = new HashSet<>();

    /**
     * the free texts belonging to this letter
     */
    @OneToMany(mappedBy = "documentLetter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "documentLetter", "deliveryNote", "invoice", "offer", "orderConfirmation" }, allowSetters = true)
    private Set<DocumentFreeText> freeTexts = new HashSet<>();

    /**
     * the letter contact
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

    public DocumentLetter id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public DocumentLetter remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getContactName() {
        return this.contactName;
    }

    public DocumentLetter contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DocumentLetter date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public DocumentLetter title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public DocumentLetter content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public DocumentLetter language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return this.pageAmount;
    }

    public DocumentLetter pageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
        return this;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return this.notes;
    }

    public DocumentLetter notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LetterStatus getStatus() {
        return this.status;
    }

    public DocumentLetter status(LetterStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LetterStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public DocumentLetter created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<CustomFieldValue> getCustomFields() {
        return this.customFields;
    }

    public DocumentLetter customFields(Set<CustomFieldValue> customFieldValues) {
        this.setCustomFields(customFieldValues);
        return this;
    }

    public DocumentLetter addCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.add(customFieldValue);
        customFieldValue.setDocumentLetter(this);
        return this;
    }

    public DocumentLetter removeCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.remove(customFieldValue);
        customFieldValue.setDocumentLetter(null);
        return this;
    }

    public void setCustomFields(Set<CustomFieldValue> customFieldValues) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setDocumentLetter(null));
        }
        if (customFieldValues != null) {
            customFieldValues.forEach(i -> i.setDocumentLetter(this));
        }
        this.customFields = customFieldValues;
    }

    public Set<DocumentFreeText> getFreeTexts() {
        return this.freeTexts;
    }

    public DocumentLetter freeTexts(Set<DocumentFreeText> documentFreeTexts) {
        this.setFreeTexts(documentFreeTexts);
        return this;
    }

    public DocumentLetter addFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.add(documentFreeText);
        documentFreeText.setDocumentLetter(this);
        return this;
    }

    public DocumentLetter removeFreeTexts(DocumentFreeText documentFreeText) {
        this.freeTexts.remove(documentFreeText);
        documentFreeText.setDocumentLetter(null);
        return this;
    }

    public void setFreeTexts(Set<DocumentFreeText> documentFreeTexts) {
        if (this.freeTexts != null) {
            this.freeTexts.forEach(i -> i.setDocumentLetter(null));
        }
        if (documentFreeTexts != null) {
            documentFreeTexts.forEach(i -> i.setDocumentLetter(this));
        }
        this.freeTexts = documentFreeTexts;
    }

    public Contact getContact() {
        return this.contact;
    }

    public DocumentLetter contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactAddress getContactAddress() {
        return this.contactAddress;
    }

    public DocumentLetter contactAddress(ContactAddress contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public DocumentLetter contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddress getContactPrePageAddress() {
        return this.contactPrePageAddress;
    }

    public DocumentLetter contactPrePageAddress(ContactAddress contactAddress) {
        this.setContactPrePageAddress(contactAddress);
        return this;
    }

    public void setContactPrePageAddress(ContactAddress contactAddress) {
        this.contactPrePageAddress = contactAddress;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public DocumentLetter layout(Layout layout) {
        this.setLayout(layout);
        return this;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public DocumentLetter signature(Signature signature) {
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
        if (!(o instanceof DocumentLetter)) {
            return false;
        }
        return id != null && id.equals(((DocumentLetter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentLetter{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
