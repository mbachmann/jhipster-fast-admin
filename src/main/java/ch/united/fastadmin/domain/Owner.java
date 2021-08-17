package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CompanyCurrency;
import ch.united.fastadmin.domain.enumeration.CompanyLanguage;
import ch.united.fastadmin.domain.enumeration.Country;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Owner.
 */
@Entity
@Table(name = "owner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Owner implements Serializable {

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
     * user name
     */
    @Column(name = "name")
    private String name;

    /**
     * user surname
     */
    @Column(name = "surname")
    private String surname;

    /**
     * user email address
     */
    @Column(name = "email")
    private String email;

    /**
     * company communication language
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private CompanyLanguage language;

    /**
     * company name
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * another company name line
     */
    @Column(name = "company_addition")
    private String companyAddition;

    /**
     * company address: country (ISO2)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "company_country")
    private Country companyCountry;

    /**
     * company address: street
     */
    @Column(name = "company_street")
    private String companyStreet;

    /**
     * company address: street number
     */
    @Column(name = "company_street_no")
    private String companyStreetNo;

    /**
     * company address: another street line
     */
    @Column(name = "company_street_2")
    private String companyStreet2;

    /**
     * company address: postcode
     */
    @Column(name = "company_postcode")
    private String companyPostcode;

    /**
     * company address: city ,
     */
    @Column(name = "company_city")
    private String companyCity;

    /**
     * phone number
     */
    @Column(name = "company_phone")
    private String companyPhone;

    /**
     * fax number
     */
    @Column(name = "company_fax")
    private String companyFax;

    /**
     * email address
     */
    @Column(name = "company_email")
    private String companyEmail;

    /**
     * website URL
     */
    @Column(name = "company_website")
    private String companyWebsite;

    /**
     * VAT/EU VAT ID no.
     */
    @Column(name = "company_vat_id")
    private String companyVatId;

    /**
     * default billing currency
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "company_currency")
    private CompanyCurrency companyCurrency;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Owner id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Owner remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return this.name;
    }

    public Owner name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Owner surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public Owner email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyLanguage getLanguage() {
        return this.language;
    }

    public Owner language(CompanyLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(CompanyLanguage language) {
        this.language = language;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Owner companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddition() {
        return this.companyAddition;
    }

    public Owner companyAddition(String companyAddition) {
        this.companyAddition = companyAddition;
        return this;
    }

    public void setCompanyAddition(String companyAddition) {
        this.companyAddition = companyAddition;
    }

    public Country getCompanyCountry() {
        return this.companyCountry;
    }

    public Owner companyCountry(Country companyCountry) {
        this.companyCountry = companyCountry;
        return this;
    }

    public void setCompanyCountry(Country companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyStreet() {
        return this.companyStreet;
    }

    public Owner companyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
        return this;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyStreetNo() {
        return this.companyStreetNo;
    }

    public Owner companyStreetNo(String companyStreetNo) {
        this.companyStreetNo = companyStreetNo;
        return this;
    }

    public void setCompanyStreetNo(String companyStreetNo) {
        this.companyStreetNo = companyStreetNo;
    }

    public String getCompanyStreet2() {
        return this.companyStreet2;
    }

    public Owner companyStreet2(String companyStreet2) {
        this.companyStreet2 = companyStreet2;
        return this;
    }

    public void setCompanyStreet2(String companyStreet2) {
        this.companyStreet2 = companyStreet2;
    }

    public String getCompanyPostcode() {
        return this.companyPostcode;
    }

    public Owner companyPostcode(String companyPostcode) {
        this.companyPostcode = companyPostcode;
        return this;
    }

    public void setCompanyPostcode(String companyPostcode) {
        this.companyPostcode = companyPostcode;
    }

    public String getCompanyCity() {
        return this.companyCity;
    }

    public Owner companyCity(String companyCity) {
        this.companyCity = companyCity;
        return this;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyPhone() {
        return this.companyPhone;
    }

    public Owner companyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
        return this;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {
        return this.companyFax;
    }

    public Owner companyFax(String companyFax) {
        this.companyFax = companyFax;
        return this;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax;
    }

    public String getCompanyEmail() {
        return this.companyEmail;
    }

    public Owner companyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
        return this;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyWebsite() {
        return this.companyWebsite;
    }

    public Owner companyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
        return this;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyVatId() {
        return this.companyVatId;
    }

    public Owner companyVatId(String companyVatId) {
        this.companyVatId = companyVatId;
        return this;
    }

    public void setCompanyVatId(String companyVatId) {
        this.companyVatId = companyVatId;
    }

    public CompanyCurrency getCompanyCurrency() {
        return this.companyCurrency;
    }

    public Owner companyCurrency(CompanyCurrency companyCurrency) {
        this.companyCurrency = companyCurrency;
        return this;
    }

    public void setCompanyCurrency(CompanyCurrency companyCurrency) {
        this.companyCurrency = companyCurrency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Owner)) {
            return false;
        }
        return id != null && id.equals(((Owner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Owner{" +
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
