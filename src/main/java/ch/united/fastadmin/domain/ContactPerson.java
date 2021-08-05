package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ContactPerson.
 */
@Entity
@Table(name = "contact_person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contactperson")
public class ContactPerson implements Serializable {

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
     * whether it is a default contact's person
     */
    @Column(name = "default_person")
    private Boolean defaultPerson;

    /**
     * the person first name
     */
    @Column(name = "name")
    private String name;

    /**
     * the person surname
     */
    @Column(name = "surname")
    private String surname;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female, O - Other
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    /**
     * The person's eMail
     */
    @Column(name = "email")
    private String email;

    /**
     * the person's phone number
     */
    @Column(name = "phone")
    private String phone;

    /**
     * the person's department
     */
    @Column(name = "department")
    private String department;

    /**
     * e.g. Dear Mr. Jones
     */
    @Column(name = "salutation")
    private String salutation;

    /**
     * whether to show Mr/Ms before name
     */
    @Column(name = "show_title")
    private Boolean showTitle;

    /**
     * whether to show department
     */
    @Column(name = "show_department")
    private Boolean showDepartment;

    /**
     * whether person whishes to receive newsletter
     */
    @Column(name = "wants_newsletter")
    private Boolean wantsNewsletter;

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "contactPerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contact", "contactPerson" }, allowSetters = true)
    private Set<CustomField> customFields = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactPerson id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ContactPerson remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Boolean getDefaultPerson() {
        return this.defaultPerson;
    }

    public ContactPerson defaultPerson(Boolean defaultPerson) {
        this.defaultPerson = defaultPerson;
        return this;
    }

    public void setDefaultPerson(Boolean defaultPerson) {
        this.defaultPerson = defaultPerson;
    }

    public String getName() {
        return this.name;
    }

    public ContactPerson name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public ContactPerson surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GenderType getGender() {
        return this.gender;
    }

    public ContactPerson gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactPerson email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public ContactPerson phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return this.department;
    }

    public ContactPerson department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSalutation() {
        return this.salutation;
    }

    public ContactPerson salutation(String salutation) {
        this.salutation = salutation;
        return this;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Boolean getShowTitle() {
        return this.showTitle;
    }

    public ContactPerson showTitle(Boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public Boolean getShowDepartment() {
        return this.showDepartment;
    }

    public ContactPerson showDepartment(Boolean showDepartment) {
        this.showDepartment = showDepartment;
        return this;
    }

    public void setShowDepartment(Boolean showDepartment) {
        this.showDepartment = showDepartment;
    }

    public Boolean getWantsNewsletter() {
        return this.wantsNewsletter;
    }

    public ContactPerson wantsNewsletter(Boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
        return this;
    }

    public void setWantsNewsletter(Boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
    }

    public Set<CustomField> getCustomFields() {
        return this.customFields;
    }

    public ContactPerson customFields(Set<CustomField> customFields) {
        this.setCustomFields(customFields);
        return this;
    }

    public ContactPerson addCustomFields(CustomField customField) {
        this.customFields.add(customField);
        customField.setContactPerson(this);
        return this;
    }

    public ContactPerson removeCustomFields(CustomField customField) {
        this.customFields.remove(customField);
        customField.setContactPerson(null);
        return this;
    }

    public void setCustomFields(Set<CustomField> customFields) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setContactPerson(null));
        }
        if (customFields != null) {
            customFields.forEach(i -> i.setContactPerson(this));
        }
        this.customFields = customFields;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPerson)) {
            return false;
        }
        return id != null && id.equals(((ContactPerson) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPerson{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultPerson='" + getDefaultPerson() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", department='" + getDepartment() + "'" +
            ", salutation='" + getSalutation() + "'" +
            ", showTitle='" + getShowTitle() + "'" +
            ", showDepartment='" + getShowDepartment() + "'" +
            ", wantsNewsletter='" + getWantsNewsletter() + "'" +
            "}";
    }
}
