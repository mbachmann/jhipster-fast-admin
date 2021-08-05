package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.CurrencyType;
import ch.united.fastadmin.domain.enumeration.LanguageType;
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
 * Criteria class for the {@link ch.united.fastadmin.domain.Owner} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.OwnerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /owners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OwnerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LanguageType
     */
    public static class LanguageTypeFilter extends Filter<LanguageType> {

        public LanguageTypeFilter() {}

        public LanguageTypeFilter(LanguageTypeFilter filter) {
            super(filter);
        }

        @Override
        public LanguageTypeFilter copy() {
            return new LanguageTypeFilter(this);
        }
    }

    /**
     * Class for filtering CurrencyType
     */
    public static class CurrencyTypeFilter extends Filter<CurrencyType> {

        public CurrencyTypeFilter() {}

        public CurrencyTypeFilter(CurrencyTypeFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyTypeFilter copy() {
            return new CurrencyTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter email;

    private LanguageTypeFilter language;

    private StringFilter companyName;

    private StringFilter companyAddition;

    private StringFilter companyCountry;

    private StringFilter companyStreet;

    private StringFilter companyStreetNo;

    private StringFilter companyStreet2;

    private StringFilter companyPostcode;

    private StringFilter companyCity;

    private StringFilter companyPhone;

    private StringFilter companyFax;

    private StringFilter companyEmail;

    private StringFilter companyWebsite;

    private StringFilter companyVatId;

    private CurrencyTypeFilter companyCurrency;

    public OwnerCriteria() {}

    public OwnerCriteria(OwnerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.companyAddition = other.companyAddition == null ? null : other.companyAddition.copy();
        this.companyCountry = other.companyCountry == null ? null : other.companyCountry.copy();
        this.companyStreet = other.companyStreet == null ? null : other.companyStreet.copy();
        this.companyStreetNo = other.companyStreetNo == null ? null : other.companyStreetNo.copy();
        this.companyStreet2 = other.companyStreet2 == null ? null : other.companyStreet2.copy();
        this.companyPostcode = other.companyPostcode == null ? null : other.companyPostcode.copy();
        this.companyCity = other.companyCity == null ? null : other.companyCity.copy();
        this.companyPhone = other.companyPhone == null ? null : other.companyPhone.copy();
        this.companyFax = other.companyFax == null ? null : other.companyFax.copy();
        this.companyEmail = other.companyEmail == null ? null : other.companyEmail.copy();
        this.companyWebsite = other.companyWebsite == null ? null : other.companyWebsite.copy();
        this.companyVatId = other.companyVatId == null ? null : other.companyVatId.copy();
        this.companyCurrency = other.companyCurrency == null ? null : other.companyCurrency.copy();
    }

    @Override
    public OwnerCriteria copy() {
        return new OwnerCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSurname() {
        return surname;
    }

    public StringFilter surname() {
        if (surname == null) {
            surname = new StringFilter();
        }
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LanguageTypeFilter getLanguage() {
        return language;
    }

    public LanguageTypeFilter language() {
        if (language == null) {
            language = new LanguageTypeFilter();
        }
        return language;
    }

    public void setLanguage(LanguageTypeFilter language) {
        this.language = language;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getCompanyAddition() {
        return companyAddition;
    }

    public StringFilter companyAddition() {
        if (companyAddition == null) {
            companyAddition = new StringFilter();
        }
        return companyAddition;
    }

    public void setCompanyAddition(StringFilter companyAddition) {
        this.companyAddition = companyAddition;
    }

    public StringFilter getCompanyCountry() {
        return companyCountry;
    }

    public StringFilter companyCountry() {
        if (companyCountry == null) {
            companyCountry = new StringFilter();
        }
        return companyCountry;
    }

    public void setCompanyCountry(StringFilter companyCountry) {
        this.companyCountry = companyCountry;
    }

    public StringFilter getCompanyStreet() {
        return companyStreet;
    }

    public StringFilter companyStreet() {
        if (companyStreet == null) {
            companyStreet = new StringFilter();
        }
        return companyStreet;
    }

    public void setCompanyStreet(StringFilter companyStreet) {
        this.companyStreet = companyStreet;
    }

    public StringFilter getCompanyStreetNo() {
        return companyStreetNo;
    }

    public StringFilter companyStreetNo() {
        if (companyStreetNo == null) {
            companyStreetNo = new StringFilter();
        }
        return companyStreetNo;
    }

    public void setCompanyStreetNo(StringFilter companyStreetNo) {
        this.companyStreetNo = companyStreetNo;
    }

    public StringFilter getCompanyStreet2() {
        return companyStreet2;
    }

    public StringFilter companyStreet2() {
        if (companyStreet2 == null) {
            companyStreet2 = new StringFilter();
        }
        return companyStreet2;
    }

    public void setCompanyStreet2(StringFilter companyStreet2) {
        this.companyStreet2 = companyStreet2;
    }

    public StringFilter getCompanyPostcode() {
        return companyPostcode;
    }

    public StringFilter companyPostcode() {
        if (companyPostcode == null) {
            companyPostcode = new StringFilter();
        }
        return companyPostcode;
    }

    public void setCompanyPostcode(StringFilter companyPostcode) {
        this.companyPostcode = companyPostcode;
    }

    public StringFilter getCompanyCity() {
        return companyCity;
    }

    public StringFilter companyCity() {
        if (companyCity == null) {
            companyCity = new StringFilter();
        }
        return companyCity;
    }

    public void setCompanyCity(StringFilter companyCity) {
        this.companyCity = companyCity;
    }

    public StringFilter getCompanyPhone() {
        return companyPhone;
    }

    public StringFilter companyPhone() {
        if (companyPhone == null) {
            companyPhone = new StringFilter();
        }
        return companyPhone;
    }

    public void setCompanyPhone(StringFilter companyPhone) {
        this.companyPhone = companyPhone;
    }

    public StringFilter getCompanyFax() {
        return companyFax;
    }

    public StringFilter companyFax() {
        if (companyFax == null) {
            companyFax = new StringFilter();
        }
        return companyFax;
    }

    public void setCompanyFax(StringFilter companyFax) {
        this.companyFax = companyFax;
    }

    public StringFilter getCompanyEmail() {
        return companyEmail;
    }

    public StringFilter companyEmail() {
        if (companyEmail == null) {
            companyEmail = new StringFilter();
        }
        return companyEmail;
    }

    public void setCompanyEmail(StringFilter companyEmail) {
        this.companyEmail = companyEmail;
    }

    public StringFilter getCompanyWebsite() {
        return companyWebsite;
    }

    public StringFilter companyWebsite() {
        if (companyWebsite == null) {
            companyWebsite = new StringFilter();
        }
        return companyWebsite;
    }

    public void setCompanyWebsite(StringFilter companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public StringFilter getCompanyVatId() {
        return companyVatId;
    }

    public StringFilter companyVatId() {
        if (companyVatId == null) {
            companyVatId = new StringFilter();
        }
        return companyVatId;
    }

    public void setCompanyVatId(StringFilter companyVatId) {
        this.companyVatId = companyVatId;
    }

    public CurrencyTypeFilter getCompanyCurrency() {
        return companyCurrency;
    }

    public CurrencyTypeFilter companyCurrency() {
        if (companyCurrency == null) {
            companyCurrency = new CurrencyTypeFilter();
        }
        return companyCurrency;
    }

    public void setCompanyCurrency(CurrencyTypeFilter companyCurrency) {
        this.companyCurrency = companyCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OwnerCriteria that = (OwnerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(language, that.language) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(companyAddition, that.companyAddition) &&
            Objects.equals(companyCountry, that.companyCountry) &&
            Objects.equals(companyStreet, that.companyStreet) &&
            Objects.equals(companyStreetNo, that.companyStreetNo) &&
            Objects.equals(companyStreet2, that.companyStreet2) &&
            Objects.equals(companyPostcode, that.companyPostcode) &&
            Objects.equals(companyCity, that.companyCity) &&
            Objects.equals(companyPhone, that.companyPhone) &&
            Objects.equals(companyFax, that.companyFax) &&
            Objects.equals(companyEmail, that.companyEmail) &&
            Objects.equals(companyWebsite, that.companyWebsite) &&
            Objects.equals(companyVatId, that.companyVatId) &&
            Objects.equals(companyCurrency, that.companyCurrency)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            name,
            surname,
            email,
            language,
            companyName,
            companyAddition,
            companyCountry,
            companyStreet,
            companyStreetNo,
            companyStreet2,
            companyPostcode,
            companyCity,
            companyPhone,
            companyFax,
            companyEmail,
            companyWebsite,
            companyVatId,
            companyCurrency
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (surname != null ? "surname=" + surname + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (companyAddition != null ? "companyAddition=" + companyAddition + ", " : "") +
            (companyCountry != null ? "companyCountry=" + companyCountry + ", " : "") +
            (companyStreet != null ? "companyStreet=" + companyStreet + ", " : "") +
            (companyStreetNo != null ? "companyStreetNo=" + companyStreetNo + ", " : "") +
            (companyStreet2 != null ? "companyStreet2=" + companyStreet2 + ", " : "") +
            (companyPostcode != null ? "companyPostcode=" + companyPostcode + ", " : "") +
            (companyCity != null ? "companyCity=" + companyCity + ", " : "") +
            (companyPhone != null ? "companyPhone=" + companyPhone + ", " : "") +
            (companyFax != null ? "companyFax=" + companyFax + ", " : "") +
            (companyEmail != null ? "companyEmail=" + companyEmail + ", " : "") +
            (companyWebsite != null ? "companyWebsite=" + companyWebsite + ", " : "") +
            (companyVatId != null ? "companyVatId=" + companyVatId + ", " : "") +
            (companyCurrency != null ? "companyCurrency=" + companyCurrency + ", " : "") +
            "}";
    }
}
