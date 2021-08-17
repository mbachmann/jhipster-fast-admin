package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.GenderType;
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
 * Contact
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contact implements Serializable {

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
     * unique number
     */

    @Column(name = "number", unique = true)
    private String number;

    /**
     * type of contact; possible values: C - Company, P - Private
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContactType type;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female, O - Other
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderType gender;

    /**
     * whether to show Mr./Ms. before contact name (for P type only)
     */
    @NotNull
    @Column(name = "gender_salutation_active", nullable = false)
    private Boolean genderSalutationActive;

    /**
     * name of contact
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * additional text for contact name (maiden name)
     */
    @Column(name = "name_addition")
    private String nameAddition;

    /**
     * e.g. Dear Mr. Jones
     */
    @Column(name = "salutation")
    private String salutation;

    /**
     * contact phone number
     */
    @Column(name = "phone")
    private String phone;

    /**
     * contact fax number
     */
    @Column(name = "fax")
    private String fax;

    /**
     * contact email address
     */
    @Column(name = "email")
    private String email;

    /**
     * contact website address
     */
    @Column(name = "website")
    private String website;

    /**
     * additional notes
     */
    @Column(name = "notes")
    private String notes;

    /**
     * main communication language
     */
    @Column(name = "communication_language")
    private String communicationLanguage;

    /**
     * possible values: U - No preference, E - Documents by e-mail, P - Documents by post
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "communication_channel")
    private CommunicationChannel communicationChannel;

    /**
     * possible values: A - Send to main e-mail address and contacts, M - Only send to main e-mail address, N - No newsletter wanted
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "communication_newsletter")
    private CommunicationNewsletter communicationNewsletter;

    /**
     * default currency
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    /**
     * e-bill account identification number
     */
    @Column(name = "ebill_account_id")
    private String ebillAccountId;

    /**
     * VAT identification number
     */
    @Column(name = "vat_identification")
    private String vatIdentification;

    /**
     * default VAT rate
     */
    @Column(name = "vat_rate")
    private Double vatRate;

    /**
     * default discount rate
     */
    @Column(name = "discount_rate")
    private Double discountRate;

    /**
     * default discount type; possible values P - in %, A - Amount
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    /**
     * default payment deadline
     */
    @Column(name = "payment_grace")
    private Integer paymentGrace;

    /**
     * cost per hour
     */
    @Column(name = "hourly_rate")
    private Double hourlyRate;

    /**
     * when contact was created
     */
    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * main address id
     */
    @Column(name = "main_address_id")
    private Integer mainAddressId;

    /**
     * contact birthday
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
     * hobbies and activities in free time
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
    @OneToMany(mappedBy = "contact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customField", "contact", "contactPerson", "project", "catalogProduct", "catalogService", "documentLetter", "deliveryNote",
        },
        allowSetters = true
    )
    private Set<CustomFieldValue> customFields = new HashSet<>();

    /**
     * type of contact's relation; possible values: type of contact's relation; possible values: CL - Customer, CR - Creditor, TE - TEAM, OF - Authorities (Behörden), ME - Medical, OT - Others
     */
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_contact__relations",
        joinColumns = @JoinColumn(name = "contact_id"),
        inverseJoinColumns = @JoinColumn(name = "relations_id")
    )
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<ContactRelation> relations = new HashSet<>();

    /**
     * contact groups
     */
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_contact__groups",
        joinColumns = @JoinColumn(name = "contact_id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<ContactGroup> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contact id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Contact remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public Contact number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactType getType() {
        return this.type;
    }

    public Contact type(ContactType type) {
        this.type = type;
        return this;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public GenderType getGender() {
        return this.gender;
    }

    public Contact gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public Boolean getGenderSalutationActive() {
        return this.genderSalutationActive;
    }

    public Contact genderSalutationActive(Boolean genderSalutationActive) {
        this.genderSalutationActive = genderSalutationActive;
        return this;
    }

    public void setGenderSalutationActive(Boolean genderSalutationActive) {
        this.genderSalutationActive = genderSalutationActive;
    }

    public String getName() {
        return this.name;
    }

    public Contact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAddition() {
        return this.nameAddition;
    }

    public Contact nameAddition(String nameAddition) {
        this.nameAddition = nameAddition;
        return this;
    }

    public void setNameAddition(String nameAddition) {
        this.nameAddition = nameAddition;
    }

    public String getSalutation() {
        return this.salutation;
    }

    public Contact salutation(String salutation) {
        this.salutation = salutation;
        return this;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getPhone() {
        return this.phone;
    }

    public Contact phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return this.fax;
    }

    public Contact fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return this.email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return this.website;
    }

    public Contact website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return this.notes;
    }

    public Contact notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCommunicationLanguage() {
        return this.communicationLanguage;
    }

    public Contact communicationLanguage(String communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
        return this;
    }

    public void setCommunicationLanguage(String communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }

    public CommunicationChannel getCommunicationChannel() {
        return this.communicationChannel;
    }

    public Contact communicationChannel(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
        return this;
    }

    public void setCommunicationChannel(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public CommunicationNewsletter getCommunicationNewsletter() {
        return this.communicationNewsletter;
    }

    public Contact communicationNewsletter(CommunicationNewsletter communicationNewsletter) {
        this.communicationNewsletter = communicationNewsletter;
        return this;
    }

    public void setCommunicationNewsletter(CommunicationNewsletter communicationNewsletter) {
        this.communicationNewsletter = communicationNewsletter;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Contact currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getEbillAccountId() {
        return this.ebillAccountId;
    }

    public Contact ebillAccountId(String ebillAccountId) {
        this.ebillAccountId = ebillAccountId;
        return this;
    }

    public void setEbillAccountId(String ebillAccountId) {
        this.ebillAccountId = ebillAccountId;
    }

    public String getVatIdentification() {
        return this.vatIdentification;
    }

    public Contact vatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
        return this;
    }

    public void setVatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public Double getVatRate() {
        return this.vatRate;
    }

    public Contact vatRate(Double vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Double getDiscountRate() {
        return this.discountRate;
    }

    public Contact discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Contact discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Integer getPaymentGrace() {
        return this.paymentGrace;
    }

    public Contact paymentGrace(Integer paymentGrace) {
        this.paymentGrace = paymentGrace;
        return this;
    }

    public void setPaymentGrace(Integer paymentGrace) {
        this.paymentGrace = paymentGrace;
    }

    public Double getHourlyRate() {
        return this.hourlyRate;
    }

    public Contact hourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Contact created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Integer getMainAddressId() {
        return this.mainAddressId;
    }

    public Contact mainAddressId(Integer mainAddressId) {
        this.mainAddressId = mainAddressId;
        return this;
    }

    public void setMainAddressId(Integer mainAddressId) {
        this.mainAddressId = mainAddressId;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Contact birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Contact birthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPlaceOfOrigin() {
        return this.placeOfOrigin;
    }

    public Contact placeOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
        return this;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getCitizenCountry1() {
        return this.citizenCountry1;
    }

    public Contact citizenCountry1(String citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
        return this;
    }

    public void setCitizenCountry1(String citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
    }

    public String getCitizenCountry2() {
        return this.citizenCountry2;
    }

    public Contact citizenCountry2(String citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
        return this;
    }

    public void setCitizenCountry2(String citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
    }

    public String getSocialSecurityNumber() {
        return this.socialSecurityNumber;
    }

    public Contact socialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getHobbies() {
        return this.hobbies;
    }

    public Contact hobbies(String hobbies) {
        this.hobbies = hobbies;
        return this;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDailyWork() {
        return this.dailyWork;
    }

    public Contact dailyWork(String dailyWork) {
        this.dailyWork = dailyWork;
        return this;
    }

    public void setDailyWork(String dailyWork) {
        this.dailyWork = dailyWork;
    }

    public String getContactAttribute01() {
        return this.contactAttribute01;
    }

    public Contact contactAttribute01(String contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
        return this;
    }

    public void setContactAttribute01(String contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
    }

    public byte[] getAvatar() {
        return this.avatar;
    }

    public Contact avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return this.avatarContentType;
    }

    public Contact avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getImageType() {
        return this.imageType;
    }

    public Contact imageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public Contact inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Set<CustomFieldValue> getCustomFields() {
        return this.customFields;
    }

    public Contact customFields(Set<CustomFieldValue> customFieldValues) {
        this.setCustomFields(customFieldValues);
        return this;
    }

    public Contact addCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.add(customFieldValue);
        customFieldValue.setContact(this);
        return this;
    }

    public Contact removeCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.remove(customFieldValue);
        customFieldValue.setContact(null);
        return this;
    }

    public void setCustomFields(Set<CustomFieldValue> customFieldValues) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setContact(null));
        }
        if (customFieldValues != null) {
            customFieldValues.forEach(i -> i.setContact(this));
        }
        this.customFields = customFieldValues;
    }

    public Set<ContactRelation> getRelations() {
        return this.relations;
    }

    public Contact relations(Set<ContactRelation> contactRelations) {
        this.setRelations(contactRelations);
        return this;
    }

    public Contact addRelations(ContactRelation contactRelation) {
        this.relations.add(contactRelation);
        contactRelation.getContacts().add(this);
        return this;
    }

    public Contact removeRelations(ContactRelation contactRelation) {
        this.relations.remove(contactRelation);
        contactRelation.getContacts().remove(this);
        return this;
    }

    public void setRelations(Set<ContactRelation> contactRelations) {
        this.relations = contactRelations;
    }

    public Set<ContactGroup> getGroups() {
        return this.groups;
    }

    public Contact groups(Set<ContactGroup> contactGroups) {
        this.setGroups(contactGroups);
        return this;
    }

    public Contact addGroups(ContactGroup contactGroup) {
        this.groups.add(contactGroup);
        contactGroup.getContacts().add(this);
        return this;
    }

    public Contact removeGroups(ContactGroup contactGroup) {
        this.groups.remove(contactGroup);
        contactGroup.getContacts().remove(this);
        return this;
    }

    public void setGroups(Set<ContactGroup> contactGroups) {
        this.groups = contactGroups;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", type='" + getType() + "'" +
            ", gender='" + getGender() + "'" +
            ", genderSalutationActive='" + getGenderSalutationActive() + "'" +
            ", name='" + getName() + "'" +
            ", nameAddition='" + getNameAddition() + "'" +
            ", salutation='" + getSalutation() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fax='" + getFax() + "'" +
            ", email='" + getEmail() + "'" +
            ", website='" + getWebsite() + "'" +
            ", notes='" + getNotes() + "'" +
            ", communicationLanguage='" + getCommunicationLanguage() + "'" +
            ", communicationChannel='" + getCommunicationChannel() + "'" +
            ", communicationNewsletter='" + getCommunicationNewsletter() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", ebillAccountId='" + getEbillAccountId() + "'" +
            ", vatIdentification='" + getVatIdentification() + "'" +
            ", vatRate=" + getVatRate() +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", paymentGrace=" + getPaymentGrace() +
            ", hourlyRate=" + getHourlyRate() +
            ", created='" + getCreated() + "'" +
            ", mainAddressId=" + getMainAddressId() +
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
