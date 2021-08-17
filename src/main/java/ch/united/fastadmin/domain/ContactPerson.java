package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A person of the contact
 */
@Entity
@Table(name = "contact_person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
     * whether person wishes to receive newsletter
     */
    @Column(name = "wants_newsletter")
    private Boolean wantsNewsletter;

    /**
     * contact birthdate
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * contact birth place
     */
    @Column(name = "birth_place")
    private String birthPlace;

    /**
     * contact place of origin
     */
    @Column(name = "place_of_origin")
    private String placeOfOrigin;

    /**
     * citizen of country 1
     */
    @Column(name = "citizen_country_1")
    private String citizenCountry1;

    /**
     * citizen of country 1
     */
    @Column(name = "citizen_country_2")
    private String citizenCountry2;

    /**
     * the social security number
     */
    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    /**
     * hobbies and activities in freetime
     */
    @Column(name = "hobbies")
    private String hobbies;

    /**
     * day structure
     */
    @Column(name = "daily_work")
    private String dailyWork;

    /**
     * attribute 1
     */
    @Column(name = "contact_attribute_01")
    private String contactAttribute01;

    /**
     * image of contact
     */
    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    /**
     * image type
     */
    @Column(name = "image_type")
    private String imageType;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "contactPerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customField", "contact", "contactPerson", "project", "catalogProduct", "catalogService", "documentLetter", "deliveryNote",
        },
        allowSetters = true
    )
    private Set<CustomFieldValue> customFields = new HashSet<>();

    /**
     * The contact relation
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "relations", "groups" }, allowSetters = true)
    private Contact contact;

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

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public ContactPerson birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public ContactPerson birthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPlaceOfOrigin() {
        return this.placeOfOrigin;
    }

    public ContactPerson placeOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
        return this;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getCitizenCountry1() {
        return this.citizenCountry1;
    }

    public ContactPerson citizenCountry1(String citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
        return this;
    }

    public void setCitizenCountry1(String citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
    }

    public String getCitizenCountry2() {
        return this.citizenCountry2;
    }

    public ContactPerson citizenCountry2(String citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
        return this;
    }

    public void setCitizenCountry2(String citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
    }

    public String getSocialSecurityNumber() {
        return this.socialSecurityNumber;
    }

    public ContactPerson socialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getHobbies() {
        return this.hobbies;
    }

    public ContactPerson hobbies(String hobbies) {
        this.hobbies = hobbies;
        return this;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDailyWork() {
        return this.dailyWork;
    }

    public ContactPerson dailyWork(String dailyWork) {
        this.dailyWork = dailyWork;
        return this;
    }

    public void setDailyWork(String dailyWork) {
        this.dailyWork = dailyWork;
    }

    public String getContactAttribute01() {
        return this.contactAttribute01;
    }

    public ContactPerson contactAttribute01(String contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
        return this;
    }

    public void setContactAttribute01(String contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
    }

    public byte[] getAvatar() {
        return this.avatar;
    }

    public ContactPerson avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return this.avatarContentType;
    }

    public ContactPerson avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getImageType() {
        return this.imageType;
    }

    public ContactPerson imageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ContactPerson inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Set<CustomFieldValue> getCustomFields() {
        return this.customFields;
    }

    public ContactPerson customFields(Set<CustomFieldValue> customFieldValues) {
        this.setCustomFields(customFieldValues);
        return this;
    }

    public ContactPerson addCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.add(customFieldValue);
        customFieldValue.setContactPerson(this);
        return this;
    }

    public ContactPerson removeCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.remove(customFieldValue);
        customFieldValue.setContactPerson(null);
        return this;
    }

    public void setCustomFields(Set<CustomFieldValue> customFieldValues) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setContactPerson(null));
        }
        if (customFieldValues != null) {
            customFieldValues.forEach(i -> i.setContactPerson(this));
        }
        this.customFields = customFieldValues;
    }

    public Contact getContact() {
        return this.contact;
    }

    public ContactPerson contact(Contact contact) {
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
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", placeOfOrigin='" + getPlaceOfOrigin() + "'" +
            ", citizenCountry1='" + getCitizenCountry1() + "'" +
            ", citizenCountry2='" + getCitizenCountry2() + "'" +
            ", socialSecurityNumber='" + getSocialSecurityNumber() + "'" +
            ", hobbies='" + getHobbies() + "'" +
            ", dailyWork='" + getDailyWork() + "'" +
            ", contactAttribute01='" + getContactAttribute01() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
