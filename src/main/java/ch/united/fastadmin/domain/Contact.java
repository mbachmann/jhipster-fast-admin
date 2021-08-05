package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.DiscountRate;
import ch.united.fastadmin.domain.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Contact
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contact")
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
    @NotNull
    @Column(name = "number", nullable = false, unique = true)
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
     * additional text for contact name
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
    @Column(name = "currency")
    private String currency;

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
    private DiscountRate discountType;

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

    @OneToOne
    @JoinColumn(unique = true)
    private ContactAddress contactMainAddress;

    /**
     * permission can be, edit or manage
     */
    @OneToMany(mappedBy = "contact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "role", "contact" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

    /**
     * contact group
     */
    @OneToMany(mappedBy = "contact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    private Set<ContactGroup> groups = new HashSet<>();

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "contact")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contact", "contactPerson" }, allowSetters = true)
    private Set<CustomField> customFields = new HashSet<>();

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

    public String getCurrency() {
        return this.currency;
    }

    public Contact currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
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

    public DiscountRate getDiscountType() {
        return this.discountType;
    }

    public Contact discountType(DiscountRate discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountRate discountType) {
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

    public ContactAddress getContactMainAddress() {
        return this.contactMainAddress;
    }

    public Contact contactMainAddress(ContactAddress contactAddress) {
        this.setContactMainAddress(contactAddress);
        return this;
    }

    public void setContactMainAddress(ContactAddress contactAddress) {
        this.contactMainAddress = contactAddress;
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public Contact permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public Contact addPermissions(Permission permission) {
        this.permissions.add(permission);
        permission.setContact(this);
        return this;
    }

    public Contact removePermissions(Permission permission) {
        this.permissions.remove(permission);
        permission.setContact(null);
        return this;
    }

    public void setPermissions(Set<Permission> permissions) {
        if (this.permissions != null) {
            this.permissions.forEach(i -> i.setContact(null));
        }
        if (permissions != null) {
            permissions.forEach(i -> i.setContact(this));
        }
        this.permissions = permissions;
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
        contactGroup.setContact(this);
        return this;
    }

    public Contact removeGroups(ContactGroup contactGroup) {
        this.groups.remove(contactGroup);
        contactGroup.setContact(null);
        return this;
    }

    public void setGroups(Set<ContactGroup> contactGroups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.setContact(null));
        }
        if (contactGroups != null) {
            contactGroups.forEach(i -> i.setContact(this));
        }
        this.groups = contactGroups;
    }

    public Set<CustomField> getCustomFields() {
        return this.customFields;
    }

    public Contact customFields(Set<CustomField> customFields) {
        this.setCustomFields(customFields);
        return this;
    }

    public Contact addCustomFields(CustomField customField) {
        this.customFields.add(customField);
        customField.setContact(this);
        return this;
    }

    public Contact removeCustomFields(CustomField customField) {
        this.customFields.remove(customField);
        customField.setContact(null);
        return this;
    }

    public void setCustomFields(Set<CustomField> customFields) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setContact(null));
        }
        if (customFields != null) {
            customFields.forEach(i -> i.setContact(this));
        }
        this.customFields = customFields;
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
            "}";
    }
}
