package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.Country;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContactAddress.
 */
@Entity
@Table(name = "contact_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactAddress implements Serializable {

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
     * whether it is a default contact's address
     */
    @NotNull
    @Column(name = "default_address", nullable = false)
    private Boolean defaultAddress;

    /**
     * country ISO2 code (eg. CH)
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private Country country;

    /**
     * the street of the address
     */
    @Column(name = "street")
    private String street;

    /**
     * number of apartment
     */
    @Column(name = "street_no")
    private String streetNo;

    /**
     * additional address information (state)
     */
    @Column(name = "street_2")
    private String street2;

    /**
     * post code oder zip code
     */
    @Column(name = "postcode")
    private String postcode;

    /**
     * city
     */
    @Column(name = "city")
    private String city;

    /**
     * whether to hide this address on documents
     */
    @NotNull
    @Column(name = "hide_on_documents", nullable = false)
    private Boolean hideOnDocuments;

    /**
     * whether the address is a default postal address
     */
    @NotNull
    @Column(name = "default_prepage", nullable = false)
    private Boolean defaultPrepage;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

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

    public ContactAddress id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ContactAddress remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Boolean getDefaultAddress() {
        return this.defaultAddress;
    }

    public ContactAddress defaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
        return this;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public Country getCountry() {
        return this.country;
    }

    public ContactAddress country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getStreet() {
        return this.street;
    }

    public ContactAddress street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNo() {
        return this.streetNo;
    }

    public ContactAddress streetNo(String streetNo) {
        this.streetNo = streetNo;
        return this;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreet2() {
        return this.street2;
    }

    public ContactAddress street2(String street2) {
        this.street2 = street2;
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public ContactAddress postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return this.city;
    }

    public ContactAddress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getHideOnDocuments() {
        return this.hideOnDocuments;
    }

    public ContactAddress hideOnDocuments(Boolean hideOnDocuments) {
        this.hideOnDocuments = hideOnDocuments;
        return this;
    }

    public void setHideOnDocuments(Boolean hideOnDocuments) {
        this.hideOnDocuments = hideOnDocuments;
    }

    public Boolean getDefaultPrepage() {
        return this.defaultPrepage;
    }

    public ContactAddress defaultPrepage(Boolean defaultPrepage) {
        this.defaultPrepage = defaultPrepage;
        return this;
    }

    public void setDefaultPrepage(Boolean defaultPrepage) {
        this.defaultPrepage = defaultPrepage;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ContactAddress inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Contact getContact() {
        return this.contact;
    }

    public ContactAddress contact(Contact contact) {
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
        if (!(o instanceof ContactAddress)) {
            return false;
        }
        return id != null && id.equals(((ContactAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAddress{" +
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
            "}";
    }
}
