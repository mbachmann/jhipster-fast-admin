package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactPerson} entity.
 */
@ApiModel(description = "A person of the contact")
public class ContactPersonDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * whether it is a default contact's person
     */
    @ApiModelProperty(value = "whether it is a default contact's person")
    private Boolean defaultPerson;

    /**
     * the person first name
     */
    @ApiModelProperty(value = "the person first name")
    private String name;

    /**
     * the person surname
     */
    @ApiModelProperty(value = "the person surname")
    private String surname;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female, O - Other
     */
    @ApiModelProperty(value = "gender of contact (required for P type); possible values: M - Male, F - Female, O - Other")
    private GenderType gender;

    /**
     * The person's eMail
     */
    @ApiModelProperty(value = "The person's eMail")
    private String email;

    /**
     * the person's phone number
     */
    @ApiModelProperty(value = "the person's phone number")
    private String phone;

    /**
     * the person's department
     */
    @ApiModelProperty(value = "the person's department")
    private String department;

    /**
     * e.g. Dear Mr. Jones
     */
    @ApiModelProperty(value = "e.g. Dear Mr. Jones")
    private String salutation;

    /**
     * whether to show Mr/Ms before name
     */
    @ApiModelProperty(value = "whether to show Mr/Ms before name")
    private Boolean showTitle;

    /**
     * whether to show department
     */
    @ApiModelProperty(value = "whether to show department")
    private Boolean showDepartment;

    /**
     * whether person wishes to receive newsletter
     */
    @ApiModelProperty(value = "whether person wishes to receive newsletter")
    private Boolean wantsNewsletter;

    /**
     * contact birthdate
     */
    @ApiModelProperty(value = "contact birthdate")
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
     * hobbies and activities in freetime
     */
    @ApiModelProperty(value = "hobbies and activities in freetime")
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

    private ContactDTO contact;

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

    public Boolean getDefaultPerson() {
        return defaultPerson;
    }

    public void setDefaultPerson(Boolean defaultPerson) {
        this.defaultPerson = defaultPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public Boolean getShowDepartment() {
        return showDepartment;
    }

    public void setShowDepartment(Boolean showDepartment) {
        this.showDepartment = showDepartment;
    }

    public Boolean getWantsNewsletter() {
        return wantsNewsletter;
    }

    public void setWantsNewsletter(Boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
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

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPersonDTO)) {
            return false;
        }

        ContactPersonDTO contactPersonDTO = (ContactPersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactPersonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPersonDTO{" +
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
            ", imageType='" + getImageType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
