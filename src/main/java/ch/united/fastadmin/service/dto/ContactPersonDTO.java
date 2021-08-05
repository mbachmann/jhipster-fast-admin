package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.GenderType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactPerson} entity.
 */
public class ContactPersonDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * whether it is a default contact's person
     */
    @ApiModelProperty(value = "whether it is a default contact's person")
    private Boolean defaultPerson;

    /**
     * the person first name
     */
    @ApiModelProperty(value = "the person first name")
    private String name;

    /**
     * the person surname
     */
    @ApiModelProperty(value = "the person surname")
    private String surname;

    /**
     * gender of contact (required for P type); possible values: M - Male, F - Female, O - Other
     */
    @ApiModelProperty(value = "gender of contact (required for P type); possible values: M - Male, F - Female, O - Other")
    private GenderType gender;

    /**
     * The person's eMail
     */
    @ApiModelProperty(value = "The person's eMail")
    private String email;

    /**
     * the person's phone number
     */
    @ApiModelProperty(value = "the person's phone number")
    private String phone;

    /**
     * the person's department
     */
    @ApiModelProperty(value = "the person's department")
    private String department;

    /**
     * e.g. Dear Mr. Jones
     */
    @ApiModelProperty(value = "e.g. Dear Mr. Jones")
    private String salutation;

    /**
     * whether to show Mr/Ms before name
     */
    @ApiModelProperty(value = "whether to show Mr/Ms before name")
    private Boolean showTitle;

    /**
     * whether to show department
     */
    @ApiModelProperty(value = "whether to show department")
    private Boolean showDepartment;

    /**
     * whether person whishes to receive newsletter
     */
    @ApiModelProperty(value = "whether person whishes to receive newsletter")
    private Boolean wantsNewsletter;

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

    public Boolean getDefaultPerson() {
        return defaultPerson;
    }

    public void setDefaultPerson(Boolean defaultPerson) {
        this.defaultPerson = defaultPerson;
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

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public Boolean getShowDepartment() {
        return showDepartment;
    }

    public void setShowDepartment(Boolean showDepartment) {
        this.showDepartment = showDepartment;
    }

    public Boolean getWantsNewsletter() {
        return wantsNewsletter;
    }

    public void setWantsNewsletter(Boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPersonDTO)) {
            return false;
        }

        ContactPersonDTO contactPersonDTO = (ContactPersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactPersonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPersonDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultPerson='" + getDefaultPerson() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", department='" + getDepartment() + "'" +
            ", salutation='" + getSalutation() + "'" +
            ", showTitle='" + getShowTitle() + "'" +
            ", showDepartment='" + getShowDepartment() + "'" +
            ", wantsNewsletter='" + getWantsNewsletter() + "'" +
            "}";
    }
}
