package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.Country;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactAddress} entity.
 */
public class ContactAddressDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * whether it is a default contact's address
     */
    @NotNull
    @ApiModelProperty(value = "whether it is a default contact's address", required = true)
    private Boolean defaultAddress;

    /**
     * country ISO2 code (eg. CH)
     */
    @NotNull
    @ApiModelProperty(value = "country ISO2 code (eg. CH)", required = true)
    private Country country;

    /**
     * the street of the address
     */
    @ApiModelProperty(value = "the street of the address")
    private String street;

    /**
     * number of apartment
     */
    @ApiModelProperty(value = "number of apartment")
    private String streetNo;

    /**
     * additional address information (state)
     */
    @ApiModelProperty(value = "additional address information (state)")
    private String street2;

    /**
     * post code oder zip code
     */
    @ApiModelProperty(value = "post code oder zip code")
    private String postcode;

    /**
     * city
     */
    @ApiModelProperty(value = "city")
    private String city;

    /**
     * whether to hide this address on documents
     */
    @NotNull
    @ApiModelProperty(value = "whether to hide this address on documents", required = true)
    private Boolean hideOnDocuments;

    /**
     * whether the address is a default postal address
     */
    @NotNull
    @ApiModelProperty(value = "whether the address is a default postal address", required = true)
    private Boolean defaultPrepage;

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

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getHideOnDocuments() {
        return hideOnDocuments;
    }

    public void setHideOnDocuments(Boolean hideOnDocuments) {
        this.hideOnDocuments = hideOnDocuments;
    }

    public Boolean getDefaultPrepage() {
        return defaultPrepage;
    }

    public void setDefaultPrepage(Boolean defaultPrepage) {
        this.defaultPrepage = defaultPrepage;
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
        if (!(o instanceof ContactAddressDTO)) {
            return false;
        }

        ContactAddressDTO contactAddressDTO = (ContactAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAddressDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultAddress='" + getDefaultAddress() + "'" +
            ", country='" + getCountry() + "'" +
            ", street='" + getStreet() + "'" +
            ", streetNo='" + getStreetNo() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", postcode='" + getPostcode() + "'" +
            ", city='" + getCity() + "'" +
            ", hideOnDocuments='" + getHideOnDocuments() + "'" +
            ", defaultPrepage='" + getDefaultPrepage() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
