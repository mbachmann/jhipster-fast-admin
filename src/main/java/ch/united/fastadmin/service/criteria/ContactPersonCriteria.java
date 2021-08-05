package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.GenderType;
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
 * Criteria class for the {@link ch.united.fastadmin.domain.ContactPerson} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ContactPersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactPersonCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private BooleanFilter defaultPerson;

    private StringFilter name;

    private StringFilter surname;

    private GenderTypeFilter gender;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter department;

    private StringFilter salutation;

    private BooleanFilter showTitle;

    private BooleanFilter showDepartment;

    private BooleanFilter wantsNewsletter;

    private LongFilter customFieldsId;

    public ContactPersonCriteria() {}

    public ContactPersonCriteria(ContactPersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.defaultPerson = other.defaultPerson == null ? null : other.defaultPerson.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.department = other.department == null ? null : other.department.copy();
        this.salutation = other.salutation == null ? null : other.salutation.copy();
        this.showTitle = other.showTitle == null ? null : other.showTitle.copy();
        this.showDepartment = other.showDepartment == null ? null : other.showDepartment.copy();
        this.wantsNewsletter = other.wantsNewsletter == null ? null : other.wantsNewsletter.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
    }

    @Override
    public ContactPersonCriteria copy() {
        return new ContactPersonCriteria(this);
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

    public BooleanFilter getDefaultPerson() {
        return defaultPerson;
    }

    public BooleanFilter defaultPerson() {
        if (defaultPerson == null) {
            defaultPerson = new BooleanFilter();
        }
        return defaultPerson;
    }

    public void setDefaultPerson(BooleanFilter defaultPerson) {
        this.defaultPerson = defaultPerson;
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

    public StringFilter getDepartment() {
        return department;
    }

    public StringFilter department() {
        if (department == null) {
            department = new StringFilter();
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
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

    public BooleanFilter getShowTitle() {
        return showTitle;
    }

    public BooleanFilter showTitle() {
        if (showTitle == null) {
            showTitle = new BooleanFilter();
        }
        return showTitle;
    }

    public void setShowTitle(BooleanFilter showTitle) {
        this.showTitle = showTitle;
    }

    public BooleanFilter getShowDepartment() {
        return showDepartment;
    }

    public BooleanFilter showDepartment() {
        if (showDepartment == null) {
            showDepartment = new BooleanFilter();
        }
        return showDepartment;
    }

    public void setShowDepartment(BooleanFilter showDepartment) {
        this.showDepartment = showDepartment;
    }

    public BooleanFilter getWantsNewsletter() {
        return wantsNewsletter;
    }

    public BooleanFilter wantsNewsletter() {
        if (wantsNewsletter == null) {
            wantsNewsletter = new BooleanFilter();
        }
        return wantsNewsletter;
    }

    public void setWantsNewsletter(BooleanFilter wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactPersonCriteria that = (ContactPersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(defaultPerson, that.defaultPerson) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(department, that.department) &&
            Objects.equals(salutation, that.salutation) &&
            Objects.equals(showTitle, that.showTitle) &&
            Objects.equals(showDepartment, that.showDepartment) &&
            Objects.equals(wantsNewsletter, that.wantsNewsletter) &&
            Objects.equals(customFieldsId, that.customFieldsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            defaultPerson,
            name,
            surname,
            gender,
            email,
            phone,
            department,
            salutation,
            showTitle,
            showDepartment,
            wantsNewsletter,
            customFieldsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (defaultPerson != null ? "defaultPerson=" + defaultPerson + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (surname != null ? "surname=" + surname + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (department != null ? "department=" + department + ", " : "") +
            (salutation != null ? "salutation=" + salutation + ", " : "") +
            (showTitle != null ? "showTitle=" + showTitle + ", " : "") +
            (showDepartment != null ? "showDepartment=" + showDepartment + ", " : "") +
            (wantsNewsletter != null ? "wantsNewsletter=" + wantsNewsletter + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            "}";
    }
}
