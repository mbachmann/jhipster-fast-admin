package ch.united.fastadmin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.ContactAddress} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ContactAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private BooleanFilter defaultAddress;

    private StringFilter country;

    private StringFilter street;

    private StringFilter streetNo;

    private StringFilter street2;

    private StringFilter postcode;

    private StringFilter city;

    private BooleanFilter hideOnDocuments;

    private BooleanFilter defaultPrepage;

    public ContactAddressCriteria() {}

    public ContactAddressCriteria(ContactAddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.defaultAddress = other.defaultAddress == null ? null : other.defaultAddress.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.street = other.street == null ? null : other.street.copy();
        this.streetNo = other.streetNo == null ? null : other.streetNo.copy();
        this.street2 = other.street2 == null ? null : other.street2.copy();
        this.postcode = other.postcode == null ? null : other.postcode.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.hideOnDocuments = other.hideOnDocuments == null ? null : other.hideOnDocuments.copy();
        this.defaultPrepage = other.defaultPrepage == null ? null : other.defaultPrepage.copy();
    }

    @Override
    public ContactAddressCriteria copy() {
        return new ContactAddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRemoteId() {
        return remoteId;
    }

    public IntegerFilter remoteId() {
        if (remoteId == null) {
            remoteId = new IntegerFilter();
        }
        return remoteId;
    }

    public void setRemoteId(IntegerFilter remoteId) {
        this.remoteId = remoteId;
    }

    public BooleanFilter getDefaultAddress() {
        return defaultAddress;
    }

    public BooleanFilter defaultAddress() {
        if (defaultAddress == null) {
            defaultAddress = new BooleanFilter();
        }
        return defaultAddress;
    }

    public void setDefaultAddress(BooleanFilter defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getStreet() {
        return street;
    }

    public StringFilter street() {
        if (street == null) {
            street = new StringFilter();
        }
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public StringFilter getStreetNo() {
        return streetNo;
    }

    public StringFilter streetNo() {
        if (streetNo == null) {
            streetNo = new StringFilter();
        }
        return streetNo;
    }

    public void setStreetNo(StringFilter streetNo) {
        this.streetNo = streetNo;
    }

    public StringFilter getStreet2() {
        return street2;
    }

    public StringFilter street2() {
        if (street2 == null) {
            street2 = new StringFilter();
        }
        return street2;
    }

    public void setStreet2(StringFilter street2) {
        this.street2 = street2;
    }

    public StringFilter getPostcode() {
        return postcode;
    }

    public StringFilter postcode() {
        if (postcode == null) {
            postcode = new StringFilter();
        }
        return postcode;
    }

    public void setPostcode(StringFilter postcode) {
        this.postcode = postcode;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public BooleanFilter getHideOnDocuments() {
        return hideOnDocuments;
    }

    public BooleanFilter hideOnDocuments() {
        if (hideOnDocuments == null) {
            hideOnDocuments = new BooleanFilter();
        }
        return hideOnDocuments;
    }

    public void setHideOnDocuments(BooleanFilter hideOnDocuments) {
        this.hideOnDocuments = hideOnDocuments;
    }

    public BooleanFilter getDefaultPrepage() {
        return defaultPrepage;
    }

    public BooleanFilter defaultPrepage() {
        if (defaultPrepage == null) {
            defaultPrepage = new BooleanFilter();
        }
        return defaultPrepage;
    }

    public void setDefaultPrepage(BooleanFilter defaultPrepage) {
        this.defaultPrepage = defaultPrepage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactAddressCriteria that = (ContactAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(defaultAddress, that.defaultAddress) &&
            Objects.equals(country, that.country) &&
            Objects.equals(street, that.street) &&
            Objects.equals(streetNo, that.streetNo) &&
            Objects.equals(street2, that.street2) &&
            Objects.equals(postcode, that.postcode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(hideOnDocuments, that.hideOnDocuments) &&
            Objects.equals(defaultPrepage, that.defaultPrepage)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            defaultAddress,
            country,
            street,
            streetNo,
            street2,
            postcode,
            city,
            hideOnDocuments,
            defaultPrepage
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (defaultAddress != null ? "defaultAddress=" + defaultAddress + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (street != null ? "street=" + street + ", " : "") +
            (streetNo != null ? "streetNo=" + streetNo + ", " : "") +
            (street2 != null ? "street2=" + street2 + ", " : "") +
            (postcode != null ? "postcode=" + postcode + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (hideOnDocuments != null ? "hideOnDocuments=" + hideOnDocuments + ", " : "") +
            (defaultPrepage != null ? "defaultPrepage=" + defaultPrepage + ", " : "") +
            "}";
    }
}
