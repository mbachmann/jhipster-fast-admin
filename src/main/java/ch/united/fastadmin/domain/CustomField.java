package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Additional information for a resource
 */
@Entity
@Table(name = "custom_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @Column(name = "default_value")
    private String defaultValue;

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

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public CustomField defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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
            ", defaultValue='" + getDefaultValue() + "'" +
            "}";
    }
}
