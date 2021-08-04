package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DomainResource;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CustomField} entity.
 */
@ApiModel(description = "Additional information for a resource")
public class CustomFieldDTO implements Serializable {

    private Long id;

    private DomainResource domainResource;

    private String key;

    private String name;

    private String value;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainResource getDomainResource() {
        return domainResource;
    }

    public void setDomainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
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
            ", domainResource='" + getDomainResource() + "'" +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
