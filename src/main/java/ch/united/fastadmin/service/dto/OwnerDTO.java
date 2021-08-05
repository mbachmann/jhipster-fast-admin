package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.CurrencyType;
import ch.united.fastadmin.domain.enumeration.LanguageType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Owner} entity.
 */
public class OwnerDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String name;

    /**
     * user surname
     */
    @ApiModelProperty(value = "user surname")
    private String surname;

    /**
     * user email address
     */
    @ApiModelProperty(value = "user email address")
    private String email;

    /**
     * company communication language
     */
    @ApiModelProperty(value = "company communication language")
    private LanguageType language;

    /**
     * company name
     */
    @ApiModelProperty(value = "company name")
    private String companyName;

    /**
     * another company name line
     */
    @ApiModelProperty(value = "another company name line")
    private String companyAddition;

    /**
     * company address: country (ISO2)
     */
    @ApiModelProperty(value = "company address: country (ISO2)")
    private String companyCountry;

    /**
     * company address: street
     */
    @ApiModelProperty(value = "company address: street")
    private String companyStreet;

    /**
     * company address: street number
     */
    @ApiModelProperty(value = "company address: street number")
    private String companyStreetNo;

    /**
     * company address: another street line
     */
    @ApiModelProperty(value = "company address: another street line")
    private String companyStreet2;

    /**
     * company address: postcode
     */
    @ApiModelProperty(value = "company address: postcode")
    private String companyPostcode;

    /**
     * company address: city ,
     */
    @ApiModelProperty(value = "company address: city ,")
    private String companyCity;

    /**
     * phone number
     */
    @ApiModelProperty(value = "phone number")
    private String companyPhone;

    /**
     * fax number
     */
    @ApiModelProperty(value = "fax number")
    private String companyFax;

    /**
     * email address
     */
    @ApiModelProperty(value = "email address")
    private String companyEmail;

    /**
     * website URL
     */
    @ApiModelProperty(value = "website URL")
    private String companyWebsite;

    /**
     * VAT/EU VAT ID no.
     */
    @ApiModelProperty(value = "VAT/EU VAT ID no.")
    private String companyVatId;

    /**
     * default billing currency
     */
    @ApiModelProperty(value = "default billing currency")
    private CurrencyType companyCurrency;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LanguageType getLanguage() {
        return language;
    }

    public void setLanguage(LanguageType language) {
        this.language = language;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddition() {
        return companyAddition;
    }

    public void setCompanyAddition(String companyAddition) {
        this.companyAddition = companyAddition;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyStreetNo() {
        return companyStreetNo;
    }

    public void setCompanyStreetNo(String companyStreetNo) {
        this.companyStreetNo = companyStreetNo;
    }

    public String getCompanyStreet2() {
        return companyStreet2;
    }

    public void setCompanyStreet2(String companyStreet2) {
        this.companyStreet2 = companyStreet2;
    }

    public String getCompanyPostcode() {
        return companyPostcode;
    }

    public void setCompanyPostcode(String companyPostcode) {
        this.companyPostcode = companyPostcode;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyVatId() {
        return companyVatId;
    }

    public void setCompanyVatId(String companyVatId) {
        this.companyVatId = companyVatId;
    }

    public CurrencyType getCompanyCurrency() {
        return companyCurrency;
    }

    public void setCompanyCurrency(CurrencyType companyCurrency) {
        this.companyCurrency = companyCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OwnerDTO)) {
            return false;
        }

        OwnerDTO ownerDTO = (OwnerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ownerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnerDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", language='" + getLanguage() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", companyAddition='" + getCompanyAddition() + "'" +
            ", companyCountry='" + getCompanyCountry() + "'" +
            ", companyStreet='" + getCompanyStreet() + "'" +
            ", companyStreetNo='" + getCompanyStreetNo() + "'" +
            ", companyStreet2='" + getCompanyStreet2() + "'" +
            ", companyPostcode='" + getCompanyPostcode() + "'" +
            ", companyCity='" + getCompanyCity() + "'" +
            ", companyPhone='" + getCompanyPhone() + "'" +
            ", companyFax='" + getCompanyFax() + "'" +
            ", companyEmail='" + getCompanyEmail() + "'" +
            ", companyWebsite='" + getCompanyWebsite() + "'" +
            ", companyVatId='" + getCompanyVatId() + "'" +
            ", companyCurrency='" + getCompanyCurrency() + "'" +
            "}";
    }
}
