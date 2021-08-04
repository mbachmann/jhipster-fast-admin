package ch.united.fastadmin.domain;

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
 * A ContactAddress.
 */
@Entity
@Table(name = "contact_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contactaddress")
public class ContactAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "country", nullable = false)
    private String country;

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
     * additional address information
     */
    @Column(name = "street_2")
    private String street2;

    /**
     * postcode oder zip code
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

    @OneToMany(mappedBy = "contactAddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "role", "contact", "contactAddress" }, allowSetters = true)
    private Set<Permission> permissions = new HashSet<>();

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

    public String getCountry() {
        return this.country;
    }

    public ContactAddress country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
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

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public ContactAddress permissions(Set<Permission> permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public ContactAddress addPermissions(Permission permission) {
        this.permissions.add(permission);
        permission.setContactAddress(this);
        return this;
    }

    public ContactAddress removePermissions(Permission permission) {
        this.permissions.remove(permission);
        permission.setContactAddress(null);
        return this;
    }

    public void setPermissions(Set<Permission> permissions) {
        if (this.permissions != null) {
            this.permissions.forEach(i -> i.setContactAddress(null));
        }
        if (permissions != null) {
            permissions.forEach(i -> i.setContactAddress(this));
        }
        this.permissions = permissions;
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
            ", defaultAddress='" + getDefaultAddress() + "'" +
            ", country='" + getCountry() + "'" +
            ", street='" + getStreet() + "'" +
            ", streetNo='" + getStreetNo() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", postcode='" + getPostcode() + "'" +
            ", city='" + getCity() + "'" +
            ", hideOnDocuments='" + getHideOnDocuments() + "'" +
            ", defaultPrepage='" + getDefaultPrepage() + "'" +
            "}";
    }
}
