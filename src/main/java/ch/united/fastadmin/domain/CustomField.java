package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainResource;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_resource")
    private DomainResource domainResource;

    @Column(name = "jhl_key")
    private String key;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mainAddress", "permissions", "groups", "customFields" }, allowSetters = true)
    private Contact contact;

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

    public DomainResource getDomainResource() {
        return this.domainResource;
    }

    public CustomField domainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
        return this;
    }

    public void setDomainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
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
            ", domainResource='" + getDomainResource() + "'" +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
