package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Contact} entity.
 */
@ApiModel(description = "Contact")
public class ContactDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * unique number
     */

    @ApiModelProperty(value = "unique number")
    private String number;

    /**
     * type of contact; possible values: C - Company, P - Private
     */
    @NotNull
    @ApiModelProperty(value = "type of contact; possible values: C - Company, P - Private", required = true)
    private ContactType type;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female, O - Other
     */
    @NotNull
    @ApiModelProperty(value = "gender of contact (required for P type); possible values: M - Male, F - Female, O - Other", required = true)
    private GenderType gender;

    /**
     * whether to show Mr./Ms. before contact name (for P type only)
     */
    @NotNull
    @ApiModelProperty(value = "whether to show Mr./Ms. before contact name (for P type only)", required = true)
    private Boolean genderSalutationActive;

    /**
     * name of contact
     */
    @NotNull
    @ApiModelProperty(value = "name of contact", required = true)
    private String name;

    /**
     * additional text for contact name (maiden name)
     */
    @ApiModelProperty(value = "additional text for contact name (maiden name)")
    private String nameAddition;

    /**
     * e.g. Dear Mr. Jones
     */
    @ApiModelProperty(value = "e.g. Dear Mr. Jones")
    private String salutation;

    /**
     * contact phone number
     */
    @ApiModelProperty(value = "contact phone number")
    private String phone;

    /**
     * contact fax number
     */
    @ApiModelProperty(value = "contact fax number")
    private String fax;

    /**
     * contact email address
     */
    @ApiModelProperty(value = "contact email address")
    private String email;

    /**
     * contact website address
     */
    @ApiModelProperty(value = "contact website address")
    private String website;

    /**
     * additional notes
     */
    @ApiModelProperty(value = "additional notes")
    private String notes;

    /**
     * main communication language
     */
    @ApiModelProperty(value = "main communication language")
    private String communicationLanguage;

    /**
     * possible values: U - No preference, E - Documents by e-mail, P - Documents by post
     */
    @ApiModelProperty(value = "possible values: U - No preference, E - Documents by e-mail, P - Documents by post")
    private CommunicationChannel communicationChannel;

    /**
     * possible values: A - Send to main e-mail address and contacts, M - Only send to main e-mail address, N - No newsletter wanted
     */
    @ApiModelProperty(
        value = "possible values: A - Send to main e-mail address and contacts, M - Only send to main e-mail address, N - No newsletter wanted"
    )
    private CommunicationNewsletter communicationNewsletter;

    /**
     * default currency
     */
    @ApiModelProperty(value = "default currency")
    private Currency currency;

    /**
     * e-bill account identification number
     */
    @ApiModelProperty(value = "e-bill account identification number")
    private String ebillAccountId;

    /**
     * VAT identification number
     */
    @ApiModelProperty(value = "VAT identification number")
    private String vatIdentification;

    /**
     * default VAT rate
     */
    @ApiModelProperty(value = "default VAT rate")
    private Double vatRate;

    /**
     * default discount rate
     */
    @ApiModelProperty(value = "default discount rate")
    private Double discountRate;

    /**
     * default discount type; possible values P - in %, A - Amount
     */
    @ApiModelProperty(value = "default discount type; possible values P - in %, A - Amount")
    private DiscountType discountType;

    /**
     * default payment deadline
     */
    @ApiModelProperty(value = "default payment deadline")
    private Integer paymentGrace;

    /**
     * cost per hour
     */
    @ApiModelProperty(value = "cost per hour")
    private Double hourlyRate;

    /**
     * when contact was created
     */
    @ApiModelProperty(value = "when contact was created")
    private ZonedDateTime created;

    /**
     * main address id
     */
    @ApiModelProperty(value = "main address id")
    private Integer mainAddressId;

    /**
     * contact birthday
     */
    @ApiModelProperty(value = "contact birthday")
    private LocalDate birthDate;

    /**
     * contact birth place
     */
    @ApiModelProperty(value = "contact birth place")
    private String birthPlace;

    /**
     * contact place of origin
     */
    @ApiModelProperty(value = "contact place of origin")
    private String placeOfOrigin;

    /**
     * citizen of country 1
     */
    @ApiModelProperty(value = "citizen of country 1")
    private String citizenCountry1;

    /**
     * citizen of country 1
     */
    @ApiModelProperty(value = "citizen of country 1")
    private String citizenCountry2;

    /**
     * the social security number
     */
    @ApiModelProperty(value = "the social security number")
    private String socialSecurityNumber;

    /**
     * hobbies and activities in free time
     */
    @ApiModelProperty(value = "hobbies and activities in free time")
    private String hobbies;

    /**
     * day structure
     */
    @ApiModelProperty(value = "day structure")
    private String dailyWork;

    /**
     * attribute 1
     */
    @ApiModelProperty(value = "attribute 1")
    private String contactAttribute01;

    /**
     * image of contact
     */
    @ApiModelProperty(value = "image of contact")
    @Lob
    private byte[] avatar;

    private String avatarContentType;

    /**
     * image type
     */
    @ApiModelProperty(value = "image type")
    private String imageType;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    private Set<ContactRelationDTO> relations = new HashSet<>();

    private Set<ContactGroupDTO> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public Boolean getGenderSalutationActive() {
        return genderSalutationActive;
    }

    public void setGenderSalutationActive(Boolean genderSalutationActive) {
        this.genderSalutationActive = genderSalutationActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAddition() {
        return nameAddition;
    }

    public void setNameAddition(String nameAddition) {
        this.nameAddition = nameAddition;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCommunicationLanguage() {
        return communicationLanguage;
    }

    public void setCommunicationLanguage(String communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }

    public CommunicationChannel getCommunicationChannel() {
        return communicationChannel;
    }

    public void setCommunicationChannel(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public CommunicationNewsletter getCommunicationNewsletter() {
        return communicationNewsletter;
    }

    public void setCommunicationNewsletter(CommunicationNewsletter communicationNewsletter) {
        this.communicationNewsletter = communicationNewsletter;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getEbillAccountId() {
        return ebillAccountId;
    }

    public void setEbillAccountId(String ebillAccountId) {
        this.ebillAccountId = ebillAccountId;
    }

    public String getVatIdentification() {
        return vatIdentification;
    }

    public void setVatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Integer getPaymentGrace() {
        return paymentGrace;
    }

    public void setPaymentGrace(Integer paymentGrace) {
        this.paymentGrace = paymentGrace;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Integer getMainAddressId() {
        return mainAddressId;
    }

    public void setMainAddressId(Integer mainAddressId) {
        this.mainAddressId = mainAddressId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getCitizenCountry1() {
        return citizenCountry1;
    }

    public void setCitizenCountry1(String citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
    }

    public String getCitizenCountry2() {
        return citizenCountry2;
    }

    public void setCitizenCountry2(String citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDailyWork() {
        return dailyWork;
    }

    public void setDailyWork(String dailyWork) {
        this.dailyWork = dailyWork;
    }

    public String getContactAttribute01() {
        return contactAttribute01;
    }

    public void setContactAttribute01(String contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Set<ContactRelationDTO> getRelations() {
        return relations;
    }

    public void setRelations(Set<ContactRelationDTO> relations) {
        this.relations = relations;
    }

    public Set<ContactGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<ContactGroupDTO> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
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
            ", imageType='" + getImageType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", relations=" + getRelations() +
            ", groups=" + getGroups() +
            "}";
    }
}
