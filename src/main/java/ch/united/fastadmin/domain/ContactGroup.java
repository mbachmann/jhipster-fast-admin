package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ContactGroup.
 */
@Entity
@Table(name = "contact_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contactgroup")
public class ContactGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

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

    public ContactGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ContactGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return this.contact;
    }

    public ContactGroup contact(Contact contact) {
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
        if (!(o instanceof ContactGroup)) {
            return false;
        }
        return id != null && id.equals(((ContactGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
