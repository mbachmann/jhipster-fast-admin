package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Additional information for a resource
 */
@Entity
@Table(name = "custom_field_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomFieldValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the key to identify the custom field
     */
    @NotNull
    @Column(name = "fa_key", nullable = false)
    private String key;

    /**
     * a name which appears on the dialog
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * the value of the resource
     */
    @Column(name = "value")
    private String value;

    /**
     * The descriptive custom field relates to the custom field definition
     */
    @ManyToOne
    private CustomField customField;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "relations", "groups" }, allowSetters = true)
    private Contact contact;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "contact" }, allowSetters = true)
    private ContactPerson contactPerson;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "contact" }, allowSetters = true)
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "category", "unit", "valueAddedTax" }, allowSetters = true)
    private CatalogProduct catalogProduct;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "category", "unit", "valueAddedTax" }, allowSetters = true)
    private CatalogService catalogService;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customFields", "freeTexts", "contact", "contactAddress", "contactPerson", "contactPrePageAddress", "layout", "signature",
        },
        allowSetters = true
    )
    private DocumentLetter documentLetter;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customFields",
            "freeTexts",
            "texts",
            "positions",
            "contact",
            "contactAddress",
            "contactPerson",
            "contactPrePageAddress",
            "layout",
            "signature",
        },
        allowSetters = true
    )
    private DeliveryNote deliveryNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomFieldValue id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public CustomFieldValue key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public CustomFieldValue name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public CustomFieldValue value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CustomField getCustomField() {
        return this.customField;
    }

    public CustomFieldValue customField(CustomField customField) {
        this.setCustomField(customField);
        return this;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }

    public Contact getContact() {
        return this.contact;
    }

    public CustomFieldValue contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public CustomFieldValue contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Project getProject() {
        return this.project;
    }

    public CustomFieldValue project(Project project) {
        this.setProject(project);
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public CatalogProduct getCatalogProduct() {
        return this.catalogProduct;
    }

    public CustomFieldValue catalogProduct(CatalogProduct catalogProduct) {
        this.setCatalogProduct(catalogProduct);
        return this;
    }

    public void setCatalogProduct(CatalogProduct catalogProduct) {
        this.catalogProduct = catalogProduct;
    }

    public CatalogService getCatalogService() {
        return this.catalogService;
    }

    public CustomFieldValue catalogService(CatalogService catalogService) {
        this.setCatalogService(catalogService);
        return this;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public DocumentLetter getDocumentLetter() {
        return this.documentLetter;
    }

    public CustomFieldValue documentLetter(DocumentLetter documentLetter) {
        this.setDocumentLetter(documentLetter);
        return this;
    }

    public void setDocumentLetter(DocumentLetter documentLetter) {
        this.documentLetter = documentLetter;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
    }

    public CustomFieldValue deliveryNote(DeliveryNote deliveryNote) {
        this.setDeliveryNote(deliveryNote);
        return this;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomFieldValue)) {
            return false;
        }
        return id != null && id.equals(((CustomFieldValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomFieldValue{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
