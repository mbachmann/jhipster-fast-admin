package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CustomField} entity.
 */
@ApiModel(description = "Additional information for a resource")
public class CustomFieldDTO implements Serializable {

    private Long id;

    /**
     * the custom field if for the domain resource
     */
    @ApiModelProperty(value = "the custom field if for the domain resource")
    private DomainArea domainArea;

    /**
     * the key to identify the custom field
     */
    @ApiModelProperty(value = "the key to identify the custom field")
    private String key;

    /**
     * a name which appears on the dialog
     */
    @ApiModelProperty(value = "a name which appears on the dialog")
    private String name;

    /**
     * the value of the resourcce
     */
    @ApiModelProperty(value = "the value of the resourcce")
    private String value;

    private ContactDTO contact;

    private ContactPersonDTO contactPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainArea getDomainArea() {
        return domainArea;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public ContactPersonDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPersonDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomFieldDTO)) {
            return false;
        }

        CustomFieldDTO customFieldDTO = (CustomFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomFieldDTO{" +
            "id=" + getId() +
            ", domainArea='" + getDomainArea() + "'" +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", contact=" + getContact() +
            ", contactPerson=" + getContactPerson() +
            "}";
    }
}
