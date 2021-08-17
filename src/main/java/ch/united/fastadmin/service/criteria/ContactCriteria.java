package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.CommunicationChannel;
import ch.united.fastadmin.domain.enumeration.CommunicationNewsletter;
import ch.united.fastadmin.domain.enumeration.ContactType;
import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.GenderType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.Contact} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContactType
     */
    public static class ContactTypeFilter extends Filter<ContactType> {

        public ContactTypeFilter() {}

        public ContactTypeFilter(ContactTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContactTypeFilter copy() {
            return new ContactTypeFilter(this);
        }
    }

    /**
     * Class for filtering GenderType
     */
    public static class GenderTypeFilter extends Filter<GenderType> {

        public GenderTypeFilter() {}

        public GenderTypeFilter(GenderTypeFilter filter) {
            super(filter);
        }

        @Override
        public GenderTypeFilter copy() {
            return new GenderTypeFilter(this);
        }
    }

    /**
     * Class for filtering CommunicationChannel
     */
    public static class CommunicationChannelFilter extends Filter<CommunicationChannel> {

        public CommunicationChannelFilter() {}

        public CommunicationChannelFilter(CommunicationChannelFilter filter) {
            super(filter);
        }

        @Override
        public CommunicationChannelFilter copy() {
            return new CommunicationChannelFilter(this);
        }
    }

    /**
     * Class for filtering CommunicationNewsletter
     */
    public static class CommunicationNewsletterFilter extends Filter<CommunicationNewsletter> {

        public CommunicationNewsletterFilter() {}

        public CommunicationNewsletterFilter(CommunicationNewsletterFilter filter) {
            super(filter);
        }

        @Override
        public CommunicationNewsletterFilter copy() {
            return new CommunicationNewsletterFilter(this);
        }
    }

    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {}

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }
    }

    /**
     * Class for filtering DiscountType
     */
    public static class DiscountTypeFilter extends Filter<DiscountType> {

        public DiscountTypeFilter() {}

        public DiscountTypeFilter(DiscountTypeFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeFilter copy() {
            return new DiscountTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter number;

    private ContactTypeFilter type;

    private GenderTypeFilter gender;

    private BooleanFilter genderSalutationActive;

    private StringFilter name;

    private StringFilter nameAddition;

    private StringFilter salutation;

    private StringFilter phone;

    private StringFilter fax;

    private StringFilter email;

    private StringFilter website;

    private StringFilter notes;

    private StringFilter communicationLanguage;

    private CommunicationChannelFilter communicationChannel;

    private CommunicationNewsletterFilter communicationNewsletter;

    private CurrencyFilter currency;

    private StringFilter ebillAccountId;

    private StringFilter vatIdentification;

    private DoubleFilter vatRate;

    private DoubleFilter discountRate;

    private DiscountTypeFilter discountType;

    private IntegerFilter paymentGrace;

    private DoubleFilter hourlyRate;

    private ZonedDateTimeFilter created;

    private IntegerFilter mainAddressId;

    private LocalDateFilter birthDate;

    private StringFilter birthPlace;

    private StringFilter placeOfOrigin;

    private StringFilter citizenCountry1;

    private StringFilter citizenCountry2;

    private StringFilter socialSecurityNumber;

    private StringFilter hobbies;

    private StringFilter dailyWork;

    private StringFilter contactAttribute01;

    private StringFilter imageType;

    private BooleanFilter inactiv;

    private LongFilter customFieldsId;

    private LongFilter relationsId;

    private LongFilter groupsId;

    public ContactCriteria() {}

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.genderSalutationActive = other.genderSalutationActive == null ? null : other.genderSalutationActive.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.nameAddition = other.nameAddition == null ? null : other.nameAddition.copy();
        this.salutation = other.salutation == null ? null : other.salutation.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.fax = other.fax == null ? null : other.fax.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.communicationLanguage = other.communicationLanguage == null ? null : other.communicationLanguage.copy();
        this.communicationChannel = other.communicationChannel == null ? null : other.communicationChannel.copy();
        this.communicationNewsletter = other.communicationNewsletter == null ? null : other.communicationNewsletter.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.ebillAccountId = other.ebillAccountId == null ? null : other.ebillAccountId.copy();
        this.vatIdentification = other.vatIdentification == null ? null : other.vatIdentification.copy();
        this.vatRate = other.vatRate == null ? null : other.vatRate.copy();
        this.discountRate = other.discountRate == null ? null : other.discountRate.copy();
        this.discountType = other.discountType == null ? null : other.discountType.copy();
        this.paymentGrace = other.paymentGrace == null ? null : other.paymentGrace.copy();
        this.hourlyRate = other.hourlyRate == null ? null : other.hourlyRate.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.mainAddressId = other.mainAddressId == null ? null : other.mainAddressId.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.birthPlace = other.birthPlace == null ? null : other.birthPlace.copy();
        this.placeOfOrigin = other.placeOfOrigin == null ? null : other.placeOfOrigin.copy();
        this.citizenCountry1 = other.citizenCountry1 == null ? null : other.citizenCountry1.copy();
        this.citizenCountry2 = other.citizenCountry2 == null ? null : other.citizenCountry2.copy();
        this.socialSecurityNumber = other.socialSecurityNumber == null ? null : other.socialSecurityNumber.copy();
        this.hobbies = other.hobbies == null ? null : other.hobbies.copy();
        this.dailyWork = other.dailyWork == null ? null : other.dailyWork.copy();
        this.contactAttribute01 = other.contactAttribute01 == null ? null : other.contactAttribute01.copy();
        this.imageType = other.imageType == null ? null : other.imageType.copy();
        this.inactiv = other.inactiv == null ? null : other.inactiv.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
        this.relationsId = other.relationsId == null ? null : other.relationsId.copy();
        this.groupsId = other.groupsId == null ? null : other.groupsId.copy();
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
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

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public ContactTypeFilter getType() {
        return type;
    }

    public ContactTypeFilter type() {
        if (type == null) {
            type = new ContactTypeFilter();
        }
        return type;
    }

    public void setType(ContactTypeFilter type) {
        this.type = type;
    }

    public GenderTypeFilter getGender() {
        return gender;
    }

    public GenderTypeFilter gender() {
        if (gender == null) {
            gender = new GenderTypeFilter();
        }
        return gender;
    }

    public void setGender(GenderTypeFilter gender) {
        this.gender = gender;
    }

    public BooleanFilter getGenderSalutationActive() {
        return genderSalutationActive;
    }

    public BooleanFilter genderSalutationActive() {
        if (genderSalutationActive == null) {
            genderSalutationActive = new BooleanFilter();
        }
        return genderSalutationActive;
    }

    public void setGenderSalutationActive(BooleanFilter genderSalutationActive) {
        this.genderSalutationActive = genderSalutationActive;
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

    public StringFilter getNameAddition() {
        return nameAddition;
    }

    public StringFilter nameAddition() {
        if (nameAddition == null) {
            nameAddition = new StringFilter();
        }
        return nameAddition;
    }

    public void setNameAddition(StringFilter nameAddition) {
        this.nameAddition = nameAddition;
    }

    public StringFilter getSalutation() {
        return salutation;
    }

    public StringFilter salutation() {
        if (salutation == null) {
            salutation = new StringFilter();
        }
        return salutation;
    }

    public void setSalutation(StringFilter salutation) {
        this.salutation = salutation;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getFax() {
        return fax;
    }

    public StringFilter fax() {
        if (fax == null) {
            fax = new StringFilter();
        }
        return fax;
    }

    public void setFax(StringFilter fax) {
        this.fax = fax;
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

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getCommunicationLanguage() {
        return communicationLanguage;
    }

    public StringFilter communicationLanguage() {
        if (communicationLanguage == null) {
            communicationLanguage = new StringFilter();
        }
        return communicationLanguage;
    }

    public void setCommunicationLanguage(StringFilter communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }

    public CommunicationChannelFilter getCommunicationChannel() {
        return communicationChannel;
    }

    public CommunicationChannelFilter communicationChannel() {
        if (communicationChannel == null) {
            communicationChannel = new CommunicationChannelFilter();
        }
        return communicationChannel;
    }

    public void setCommunicationChannel(CommunicationChannelFilter communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public CommunicationNewsletterFilter getCommunicationNewsletter() {
        return communicationNewsletter;
    }

    public CommunicationNewsletterFilter communicationNewsletter() {
        if (communicationNewsletter == null) {
            communicationNewsletter = new CommunicationNewsletterFilter();
        }
        return communicationNewsletter;
    }

    public void setCommunicationNewsletter(CommunicationNewsletterFilter communicationNewsletter) {
        this.communicationNewsletter = communicationNewsletter;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public CurrencyFilter currency() {
        if (currency == null) {
            currency = new CurrencyFilter();
        }
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public StringFilter getEbillAccountId() {
        return ebillAccountId;
    }

    public StringFilter ebillAccountId() {
        if (ebillAccountId == null) {
            ebillAccountId = new StringFilter();
        }
        return ebillAccountId;
    }

    public void setEbillAccountId(StringFilter ebillAccountId) {
        this.ebillAccountId = ebillAccountId;
    }

    public StringFilter getVatIdentification() {
        return vatIdentification;
    }

    public StringFilter vatIdentification() {
        if (vatIdentification == null) {
            vatIdentification = new StringFilter();
        }
        return vatIdentification;
    }

    public void setVatIdentification(StringFilter vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public DoubleFilter getVatRate() {
        return vatRate;
    }

    public DoubleFilter vatRate() {
        if (vatRate == null) {
            vatRate = new DoubleFilter();
        }
        return vatRate;
    }

    public void setVatRate(DoubleFilter vatRate) {
        this.vatRate = vatRate;
    }

    public DoubleFilter getDiscountRate() {
        return discountRate;
    }

    public DoubleFilter discountRate() {
        if (discountRate == null) {
            discountRate = new DoubleFilter();
        }
        return discountRate;
    }

    public void setDiscountRate(DoubleFilter discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountTypeFilter getDiscountType() {
        return discountType;
    }

    public DiscountTypeFilter discountType() {
        if (discountType == null) {
            discountType = new DiscountTypeFilter();
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeFilter discountType) {
        this.discountType = discountType;
    }

    public IntegerFilter getPaymentGrace() {
        return paymentGrace;
    }

    public IntegerFilter paymentGrace() {
        if (paymentGrace == null) {
            paymentGrace = new IntegerFilter();
        }
        return paymentGrace;
    }

    public void setPaymentGrace(IntegerFilter paymentGrace) {
        this.paymentGrace = paymentGrace;
    }

    public DoubleFilter getHourlyRate() {
        return hourlyRate;
    }

    public DoubleFilter hourlyRate() {
        if (hourlyRate == null) {
            hourlyRate = new DoubleFilter();
        }
        return hourlyRate;
    }

    public void setHourlyRate(DoubleFilter hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ZonedDateTimeFilter getCreated() {
        return created;
    }

    public ZonedDateTimeFilter created() {
        if (created == null) {
            created = new ZonedDateTimeFilter();
        }
        return created;
    }

    public void setCreated(ZonedDateTimeFilter created) {
        this.created = created;
    }

    public IntegerFilter getMainAddressId() {
        return mainAddressId;
    }

    public IntegerFilter mainAddressId() {
        if (mainAddressId == null) {
            mainAddressId = new IntegerFilter();
        }
        return mainAddressId;
    }

    public void setMainAddressId(IntegerFilter mainAddressId) {
        this.mainAddressId = mainAddressId;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            birthDate = new LocalDateFilter();
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getBirthPlace() {
        return birthPlace;
    }

    public StringFilter birthPlace() {
        if (birthPlace == null) {
            birthPlace = new StringFilter();
        }
        return birthPlace;
    }

    public void setBirthPlace(StringFilter birthPlace) {
        this.birthPlace = birthPlace;
    }

    public StringFilter getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public StringFilter placeOfOrigin() {
        if (placeOfOrigin == null) {
            placeOfOrigin = new StringFilter();
        }
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(StringFilter placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public StringFilter getCitizenCountry1() {
        return citizenCountry1;
    }

    public StringFilter citizenCountry1() {
        if (citizenCountry1 == null) {
            citizenCountry1 = new StringFilter();
        }
        return citizenCountry1;
    }

    public void setCitizenCountry1(StringFilter citizenCountry1) {
        this.citizenCountry1 = citizenCountry1;
    }

    public StringFilter getCitizenCountry2() {
        return citizenCountry2;
    }

    public StringFilter citizenCountry2() {
        if (citizenCountry2 == null) {
            citizenCountry2 = new StringFilter();
        }
        return citizenCountry2;
    }

    public void setCitizenCountry2(StringFilter citizenCountry2) {
        this.citizenCountry2 = citizenCountry2;
    }

    public StringFilter getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public StringFilter socialSecurityNumber() {
        if (socialSecurityNumber == null) {
            socialSecurityNumber = new StringFilter();
        }
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(StringFilter socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public StringFilter getHobbies() {
        return hobbies;
    }

    public StringFilter hobbies() {
        if (hobbies == null) {
            hobbies = new StringFilter();
        }
        return hobbies;
    }

    public void setHobbies(StringFilter hobbies) {
        this.hobbies = hobbies;
    }

    public StringFilter getDailyWork() {
        return dailyWork;
    }

    public StringFilter dailyWork() {
        if (dailyWork == null) {
            dailyWork = new StringFilter();
        }
        return dailyWork;
    }

    public void setDailyWork(StringFilter dailyWork) {
        this.dailyWork = dailyWork;
    }

    public StringFilter getContactAttribute01() {
        return contactAttribute01;
    }

    public StringFilter contactAttribute01() {
        if (contactAttribute01 == null) {
            contactAttribute01 = new StringFilter();
        }
        return contactAttribute01;
    }

    public void setContactAttribute01(StringFilter contactAttribute01) {
        this.contactAttribute01 = contactAttribute01;
    }

    public StringFilter getImageType() {
        return imageType;
    }

    public StringFilter imageType() {
        if (imageType == null) {
            imageType = new StringFilter();
        }
        return imageType;
    }

    public void setImageType(StringFilter imageType) {
        this.imageType = imageType;
    }

    public BooleanFilter getInactiv() {
        return inactiv;
    }

    public BooleanFilter inactiv() {
        if (inactiv == null) {
            inactiv = new BooleanFilter();
        }
        return inactiv;
    }

    public void setInactiv(BooleanFilter inactiv) {
        this.inactiv = inactiv;
    }

    public LongFilter getCustomFieldsId() {
        return customFieldsId;
    }

    public LongFilter customFieldsId() {
        if (customFieldsId == null) {
            customFieldsId = new LongFilter();
        }
        return customFieldsId;
    }

    public void setCustomFieldsId(LongFilter customFieldsId) {
        this.customFieldsId = customFieldsId;
    }

    public LongFilter getRelationsId() {
        return relationsId;
    }

    public LongFilter relationsId() {
        if (relationsId == null) {
            relationsId = new LongFilter();
        }
        return relationsId;
    }

    public void setRelationsId(LongFilter relationsId) {
        this.relationsId = relationsId;
    }

    public LongFilter getGroupsId() {
        return groupsId;
    }

    public LongFilter groupsId() {
        if (groupsId == null) {
            groupsId = new LongFilter();
        }
        return groupsId;
    }

    public void setGroupsId(LongFilter groupsId) {
        this.groupsId = groupsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(number, that.number) &&
            Objects.equals(type, that.type) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(genderSalutationActive, that.genderSalutationActive) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nameAddition, that.nameAddition) &&
            Objects.equals(salutation, that.salutation) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(fax, that.fax) &&
            Objects.equals(email, that.email) &&
            Objects.equals(website, that.website) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(communicationLanguage, that.communicationLanguage) &&
            Objects.equals(communicationChannel, that.communicationChannel) &&
            Objects.equals(communicationNewsletter, that.communicationNewsletter) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(ebillAccountId, that.ebillAccountId) &&
            Objects.equals(vatIdentification, that.vatIdentification) &&
            Objects.equals(vatRate, that.vatRate) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(paymentGrace, that.paymentGrace) &&
            Objects.equals(hourlyRate, that.hourlyRate) &&
            Objects.equals(created, that.created) &&
            Objects.equals(mainAddressId, that.mainAddressId) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(birthPlace, that.birthPlace) &&
            Objects.equals(placeOfOrigin, that.placeOfOrigin) &&
            Objects.equals(citizenCountry1, that.citizenCountry1) &&
            Objects.equals(citizenCountry2, that.citizenCountry2) &&
            Objects.equals(socialSecurityNumber, that.socialSecurityNumber) &&
            Objects.equals(hobbies, that.hobbies) &&
            Objects.equals(dailyWork, that.dailyWork) &&
            Objects.equals(contactAttribute01, that.contactAttribute01) &&
            Objects.equals(imageType, that.imageType) &&
            Objects.equals(inactiv, that.inactiv) &&
            Objects.equals(customFieldsId, that.customFieldsId) &&
            Objects.equals(relationsId, that.relationsId) &&
            Objects.equals(groupsId, that.groupsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            number,
            type,
            gender,
            genderSalutationActive,
            name,
            nameAddition,
            salutation,
            phone,
            fax,
            email,
            website,
            notes,
            communicationLanguage,
            communicationChannel,
            communicationNewsletter,
            currency,
            ebillAccountId,
            vatIdentification,
            vatRate,
            discountRate,
            discountType,
            paymentGrace,
            hourlyRate,
            created,
            mainAddressId,
            birthDate,
            birthPlace,
            placeOfOrigin,
            citizenCountry1,
            citizenCountry2,
            socialSecurityNumber,
            hobbies,
            dailyWork,
            contactAttribute01,
            imageType,
            inactiv,
            customFieldsId,
            relationsId,
            groupsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (genderSalutationActive != null ? "genderSalutationActive=" + genderSalutationActive + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (nameAddition != null ? "nameAddition=" + nameAddition + ", " : "") +
            (salutation != null ? "salutation=" + salutation + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (fax != null ? "fax=" + fax + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (communicationLanguage != null ? "communicationLanguage=" + communicationLanguage + ", " : "") +
            (communicationChannel != null ? "communicationChannel=" + communicationChannel + ", " : "") +
            (communicationNewsletter != null ? "communicationNewsletter=" + communicationNewsletter + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (ebillAccountId != null ? "ebillAccountId=" + ebillAccountId + ", " : "") +
            (vatIdentification != null ? "vatIdentification=" + vatIdentification + ", " : "") +
            (vatRate != null ? "vatRate=" + vatRate + ", " : "") +
            (discountRate != null ? "discountRate=" + discountRate + ", " : "") +
            (discountType != null ? "discountType=" + discountType + ", " : "") +
            (paymentGrace != null ? "paymentGrace=" + paymentGrace + ", " : "") +
            (hourlyRate != null ? "hourlyRate=" + hourlyRate + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (mainAddressId != null ? "mainAddressId=" + mainAddressId + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (birthPlace != null ? "birthPlace=" + birthPlace + ", " : "") +
            (placeOfOrigin != null ? "placeOfOrigin=" + placeOfOrigin + ", " : "") +
            (citizenCountry1 != null ? "citizenCountry1=" + citizenCountry1 + ", " : "") +
            (citizenCountry2 != null ? "citizenCountry2=" + citizenCountry2 + ", " : "") +
            (socialSecurityNumber != null ? "socialSecurityNumber=" + socialSecurityNumber + ", " : "") +
            (hobbies != null ? "hobbies=" + hobbies + ", " : "") +
            (dailyWork != null ? "dailyWork=" + dailyWork + ", " : "") +
            (contactAttribute01 != null ? "contactAttribute01=" + contactAttribute01 + ", " : "") +
            (imageType != null ? "imageType=" + imageType + ", " : "") +
            (inactiv != null ? "inactiv=" + inactiv + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            (relationsId != null ? "relationsId=" + relationsId + ", " : "") +
            (groupsId != null ? "groupsId=" + groupsId + ", " : "") +
            "}";
    }
}