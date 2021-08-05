package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Additional information for a resource
 */
@Entity
@Table(name = "custom_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customfield")
public class CustomField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the custom field if for the domain resource
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "domain_area")
    private DomainArea domainArea;

    /**
     * the key to identify the custom field
     */
    @Column(name = "fa_key")
    private String key;

    /**
     * a name which appears on the dialog
     */
    @Column(name = "name")
    private String name;

    /**
     * the value of the resourcce
     */
    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contactMainAddress", "permissions", "groups", "customFields", "relations" }, allowSetters = true)
    private Contact contact;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields" }, allowSetters = true)
    private ContactPerson contactPerson;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomField id(Long id) {
        this.id = id;
        return this;
    }

    public DomainArea getDomainArea() {
        return this.domainArea;
    }

    public CustomField domainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
        return this;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public String getKey() {
        return this.key;
    }

    public CustomField key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public CustomField name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public CustomField value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Contact getContact() {
        return this.contact;
    }

    public CustomField contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ContactPerson getContactPerson() {
        return this.contactPerson;
    }

    public CustomField contactPerson(ContactPerson contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomField)) {
            return false;
        }
        return id != null && id.equals(((CustomField) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomField{" +
            "id=" + getId() +
            ", domainArea='" + getDomainArea() + "'" +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
