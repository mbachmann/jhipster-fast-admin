package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.ContactRelation;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Contact} entity.
 */
@ApiModel(description = "Contact")
public class ContactDTO implements Serializable {

    private Long id;

    /**
     * unique number
     */
    @ApiModelProperty(value = "unique number")
    private String number;

    /**
     * type of contact's relation; possible values: CL - Customer, CR - Creditor
     */
    @ApiModelProperty(value = "type of contact's relation; possible values: CL - Customer, CR - Creditor")
    private ContactRelation relation;

    /**
     * type of contact; possible values: C - Company, P - Private
     */
    @ApiModelProperty(value = "type of contact; possible values: C - Company, P - Private")
    private ContactType type;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female
     */
    @ApiModelProperty(value = "gender of contact (required for P type); possible values: M - Male, F - Female")
    private GenderType gender;

    /**
     * whether to show Mr./Ms. before contact name (for P type only)
     */
    @ApiModelProperty(value = "whether to show Mr./Ms. before contact name (for P type only)")
    private Boolean genderSalutationActive;

    /**
     * name of contact
     */
    @ApiModelProperty(value = "name of contact")
    private String name;

    /**
     * additional text for contact name
     */
    @ApiModelProperty(value = "additional text for contact name")
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
    private String communicationChannel;

    /**
     * possible values: A - Send to main e-mail address and contacts, M - Only send to main e-mail address, N - No newsletter wanted
     */
    @ApiModelProperty(
        value = "possible values: A - Send to main e-mail address and contacts, M - Only send to main e-mail address, N - No newsletter wanted"
    )
    private String communicationNewsletter;

    /**
     * default currency
     */
    @ApiModelProperty(value = "default currency")
    private String currency;

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
     * default VAT rate ,
     */
    @ApiModelProperty(value = "default VAT rate ,")
    private Double vatRate;

    /**
     * default discount rate
     */
    @ApiModelProperty(value = "default discount rate")
    private Double discountRate;

    /**
     * default discount type; possible valuesP - in %, A - Amount
     */
    @ApiModelProperty(value = "default discount type; possible valuesP - in %, A - Amount")
    private String discountType;

    /**
     * default payment deadline
     */
    @ApiModelProperty(value = "default payment deadline")
    private Integer paymentGrace;

    /**
     * cost per hour ,
     */
    @ApiModelProperty(value = "cost per hour ,")
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

    private ContactAddressDTO mainAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactRelation getRelation() {
        return relation;
    }

    public void setRelation(ContactRelation relation) {
        this.relation = relation;
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

    public String getCommunicationChannel() {
        return communicationChannel;
    }

    public void setCommunicationChannel(String communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public String getCommunicationNewsletter() {
        return communicationNewsletter;
    }

    public void setCommunicationNewsletter(String communicationNewsletter) {
        this.communicationNewsletter = communicationNewsletter;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
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

    public ContactAddressDTO getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(ContactAddressDTO mainAddress) {
        this.mainAddress = mainAddress;
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
            ", number='" + getNumber() + "'" +
            ", relation='" + getRelation() + "'" +
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
            ", mainAddress=" + getMainAddress() +
            "}";
    }
}
